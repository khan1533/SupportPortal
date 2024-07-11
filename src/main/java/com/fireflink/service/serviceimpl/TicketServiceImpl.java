package com.fireflink.service.serviceimpl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fireflink.mailservice.SendEmail;
import com.fireflink.model.SearchHelper;
import com.fireflink.responsedto.TicketResponse;
import com.fireflink.service.TicketService;
import com.fireflink.service.UserService;
import com.fireflink.dto.RequestTicketDto;


import com.fireflink.enums.Access;
import com.fireflink.repository.TickerRepository;
import com.fireflink.utility.ResponseStructure;
import com.fireflink.utility.ResponseStructureAPI;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fireflink.dao.TicketDao;
//import com.fireflink.ticketmanagement.dto.TicketDto;
import com.fireflink.mailservice.EmailModel;
import com.fireflink.mailservice.SpringMailSender;
import com.fireflink.model.Ticket;
import com.fireflink.model.User;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

	private static final long MAX_FILE_SIZE = 15 * 1024 * 1024; // 15MB
	private static final int MAX_FILE_COUNT = 5;

	private final TicketDao ticketDao;
	private final ModelMapper modelMapper;
	private final UserService userService;
	private final MongoTemplate mongoTemplate;
	private final TickerRepository tickerRepository;
	private final SendEmail sendEmail;


	private final GridFsTemplate gridFsTemplate;

	public ResponseEntity<ResponseStructure> createTicket(RequestTicketDto ticketDto, String userEmail) throws IOException {

		if (ticketDto.getFiles() != null && ticketDto.getFiles().size() > MAX_FILE_COUNT) {
			throw new IllegalArgumentException("Cannot upload more than 5 files");
		}

		List<String> supportingDocumentIds = new ArrayList<>();
		for (MultipartFile file : ticketDto.getFiles()) {
			if (file.getSize() > MAX_FILE_SIZE) {
				throw new IllegalArgumentException("File size cannot exceed 15MB");
			}

			ObjectId fileId = gridFsTemplate.store(file.getInputStream(),
					file.getOriginalFilename(), file.getContentType());
			supportingDocumentIds.add(fileId.toHexString());
		}

		User user = userService.findByUseremail(userEmail);
//        User  adminUser=userService.findByRoleIgnoreCase("admin");
			Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
			ticket.setStatus("Open");
			ticket.setSupportingDocumentIds(supportingDocumentIds);
			ticket.createBaseEntity(user.getUsername(), userEmail);

		Ticket createdTicket=ticketDao.createTicket(ticket);

			String[] recivers={user.getCreateByEmail()};
;			try {
				sendEmail.createMail(recivers,"your ticket has been created","thank you !");
			} catch (MessagingException e) {

				e.printStackTrace();
			}

			return ResponseStructureAPI.createResponse(modelMapper.map(createdTicket, TicketResponse.class));


	}


//	private void createMail(String user1, String subject, String text) throws MessagingException {
//		EmailModel emailModel=new EmailModel();
//		String [] arr= {user1};
//		emailModel.setTo(arr);
//		emailModel.setSubject(subject);
//		emailModel.setText(text);
//
//		springMailSender.sendMail(emailModel);
//
//
//	}


	@Override
	public ResponseEntity<ResponseStructure> assignTicket(String ticketId, String assigneeEmail, String assignerEmail) {

		User assignee = userService.findByUseremail(assigneeEmail);
		User assigner = userService.findByUseremail(assignerEmail);

		if(  !assignee.getPrivilege().equalsIgnoreCase("customer") &&
				assigner.getPrivilege().equalsIgnoreCase(Access.FULL_ACCESS.toString())
		) {
			Ticket ticket = ticketDao.findById(ticketId);
			ticket.setAssignedToEmail(assignee.getUseremail());
			ticket.setAssignedTo(assignee.getUsername());
			ticket.setAssignedToPrivilege(assignee.getPrivilege());
			ticket.modifiedBaseEntity(assigner.getUsername(), assigner.getUseremail());
			return ticketDao.assignTicket(ticket);
		}

		throw new NoSuchElementException("invalid email id , no user found");

	}


	@Override
	public List<Ticket> getAllTickets() {
        Pageable pageable= PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdOn"));
        return  ticketDao.getAllTickets(pageable);

	}


	@Override
	public ResponseEntity<ResponseStructure> getTicketById(String id) {
		 Ticket ticket = mongoTemplate.findById(id, Ticket.class);
		 return ResponseStructureAPI.foundResponse(ticket);
	}

	@Override
	public ResponseEntity<ResponseStructure> updateById(String id, RequestTicketDto ticketDto, String email) {
		Ticket ticket = mongoTemplate.findById(id, Ticket.class);
		User user = userService.findByUseremail(email);
		if(ticket!=null  && user.getAccess().equalsIgnoreCase(Access.FULL_ACCESS.toString())){

			Ticket updatedticket=modelMapper.map(ticketDto, Ticket.class);
			updatedticket.setId(id);
			updatedticket.modifiedBaseEntity(user.getUsername(), email);
			 return ticketDao.updateById(updatedticket);

		}


		throw new NoSuchElementException("No such Ticket is Present with provided Id");
	}

	@Override
	public ResponseEntity<Ticket> changeAssignee(String id, String newAssigneeEmail,String assignerEmail) {

		Ticket ticket = mongoTemplate.findById(id, Ticket.class);

		User newAssignee = userService.findByUseremail(newAssigneeEmail);
		User assigner = userService.findByUseremail(assignerEmail);

		if(ticket!=null  && !newAssignee.getPrivilege().equals("customer") &&
				assigner.getAccess().equalsIgnoreCase(Access.FULL_ACCESS.toString())){

			Ticket newTicket=modelMapper.map(ticket, Ticket.class);
//			newTicket.setId(id);
			newTicket.setAssignedTo(newAssignee.getUsername());
			newTicket.setAssignedToEmail(newAssignee.getUseremail());
			newTicket.setAssignedToPrivilege(newAssignee.getPrivilege());

			newTicket.modifiedBaseEntity(assigner.getUsername(), assigner.getUseremail());

			return ticketDao.changeAssignee(newTicket);

		}

	throw new NoSuchElementException("invalid TicketId or email");

	}

	@Override
	public ResponseEntity<ResponseStructure> deleteTicket(String id) {

		Ticket ticket = mongoTemplate.findById(id, Ticket.class);
		if(ticket!=null){
			 mongoTemplate.remove(ticket);
			return ResponseStructureAPI.okResponse("deleted");
		}
		throw new NoSuchElementException("invalid  id");
	}

	@Override
	public ResponseEntity<ResponseStructure> updateTicketStatus(String id, String email) {
		Ticket ticket=mongoTemplate.findById(id, Ticket.class);
		User user=userService.findByUseremail(email);
		if(user.getAccess().equalsIgnoreCase(Access.FULL_ACCESS.toString())){
			Ticket statusUpdatedTicket=modelMapper.map(ticket, Ticket.class);
			statusUpdatedTicket.setStatus("In progress");

			return ticketDao.updateTicketStatus(statusUpdatedTicket);
		}


		throw new NoSuchElementException("No such Ticket is Present with provided Id");
	}

	@Override
	public ResponseEntity<ResponseStructure> assignMultipleTickets(List<String> ticketList,
																   String assigneeEmail,
																   String assignerEmail) {

		User assignee = userService.findByUseremail(assigneeEmail);
		User assigner = userService.findByUseremail(assignerEmail);
		if (!assignee.getPrivilege().equalsIgnoreCase("customer") &&
				assigner.getPrivilege().equalsIgnoreCase("admin")) {

			Set<Ticket> tickets = ticketList.stream().map(id -> {
				Ticket ticket= ticketDao.findById(id);
				ticket.setAssignedTo(assignee.getUsername());
				ticket.setAssignedToEmail(assignee.getUseremail());
				ticket.setAssignedToPrivilege(assignee.getPrivilege());
				ticket.modifiedBaseEntity(assigner.getUsername(), assigner.getUseremail());
				return ticket;
			}).collect(Collectors.toSet());

			return ResponseStructureAPI.okResponse(tickets);
		}


		throw new NoSuchElementException("No such Ticket is Present with provided Id's");
	}

	@Override
	public ResponseEntity<ResponseStructure> ticketsByUser(String email) {

		User user=userService.findByUseremail(email);
        Query query=new Query(Criteria.where("createdByName").is(user.getUsername()));

		List<Ticket> tickets = mongoTemplate.find(query, Ticket.class);
//		tickets.stream().filter(ticket -> user.getUsername().equals(ticket.getCreatedByName()))
//				.collect(Collectors.toList());

        if(!tickets.isEmpty())
		    return ResponseStructureAPI.foundResponse(tickets);

        throw new NoSuchElementException("No tickets found");

	}

	@Override
	public ResponseEntity<ResponseStructure> removeAssignee(String ticketId,String assigneeEmail) {

		Ticket ticket = mongoTemplate.findById(ticketId, Ticket.class);
		User assignee = userService.findByUseremail(assigneeEmail);

		if(assignee.getPrivilege().equals(Access.FULL_ACCESS.toString())){
			Ticket assigeeRemoved = modelMapper.map(ticket, Ticket.class);
			assigeeRemoved.setAssignedTo(null);
			assigeeRemoved.setAssignedToEmail(null);
			assigeeRemoved.setAssignedToPrivilege(null);
			assigeeRemoved.modifiedBaseEntity(assignee.getUsername(), assignee.getUseremail());
			return ticketDao.removeAssignee(assigeeRemoved);

		}


		throw new NoSuchElementException("No such Ticket found");
	}

    @Override
    public ResponseEntity<ResponseStructure> searchBy(String id,String summary) {

		Criteria criteria=new Criteria();

		if(id!=null){
			criteria=criteria.and("id").regex(id,"i");
		}
		if(summary!=null){
			criteria=criteria.and("summary").regex(summary,"i");
		}
Query query=new Query(criteria);
		List<Ticket> tickets=mongoTemplate.find(query, Ticket.class);
		if(!tickets.isEmpty()){
			return ResponseStructureAPI.foundResponse(tickets);
		}
        throw  new IllegalArgumentException("Either ticketId or summary must be provided");


    }

	@Override
	public List<Ticket> filters(Map<String, List<String>> filter) {

		Set<String> keys = filter.keySet();
		Criteria criteria=new Criteria();
		for(String key : keys){
			if(key.equalsIgnoreCase("status")) {
				criteria = criteria.and(key).in(filter.get(key));
			}
			if(key.equalsIgnoreCase("issueRelated")) {
				criteria = criteria.and(key).in(filter.get(key));
			}
			if(key.equalsIgnoreCase("severity")) {
				criteria = criteria.and(key).in(filter.get(key));
			}
			if(key.equalsIgnoreCase("priority")) {
				criteria = criteria.and(key).in(filter.get(key));
			}
		}

		Query query=new Query(criteria);


		List<Ticket> tickets=mongoTemplate.find(query, Ticket.class);
		if(!tickets.isEmpty()){
			return tickets;
		}
		throw new NoSuchElementException("No Tickets Found ");
	}

	@Override
	public List<Ticket> searchAndFilter(SearchHelper searchHelper) {
		Map<String, List<String>> filters = searchHelper.getFilter();
		String idOrSummary = searchHelper.getSearchByIdAndSummary();

 		List<Criteria> listOfCriteria=new ArrayList<>();
		if(filters!=null && !filters.isEmpty()) {
			Set<String> keys=filters.keySet();
			for (String key : keys) {
				if (key.equalsIgnoreCase("status") ||
						key.equalsIgnoreCase("issueRelated") ||
						key.equalsIgnoreCase("severity") ||
						key.equalsIgnoreCase("priority")) {
					listOfCriteria.add(Criteria.where(key).in(filters.get(key)));
				}
			}
		}

		if(idOrSummary!=null && !idOrSummary.isEmpty()){
			Criteria criteria=new Criteria().orOperator(Criteria.where("id").regex(idOrSummary, "i"),
					Criteria.where("summary").regex(idOrSummary, "i"));
			listOfCriteria.add(criteria);
		}

		if (!listOfCriteria.isEmpty()) {
			Query query=new Query();
			query.addCriteria(new Criteria().andOperator(listOfCriteria.toArray(new Criteria[1])));
			return mongoTemplate.find(query, Ticket.class);
		}
		throw new NoSuchElementException("No tickets found");





	}

//		Map<String, List<String>> filters = searchHelper.getFilter();
//		String idOrSummary=searchHelper.getSearchByIdAndSummary();
//
//        Criteria criteria=new Criteria().andOperator();
//
//        if(filters!=null){
//            Set<String> keys = filters.keySet();
//            for(String key : keys){
//                if(key.equalsIgnoreCase("status")) {
//                    criteria = criteria.and(key).in(filters.get(key));
//                }
//                if(key.equalsIgnoreCase("issueRelated")) {
//                    criteria = criteria.and(key).in(filters.get(key));
//                }
//                if(key.equalsIgnoreCase("severity")) {
//                    criteria = criteria.and(key).in(filters.get(key));
//                }
//                if(key.equalsIgnoreCase("priority")) {
//                    criteria = criteria.and(key).in(filters.get(key));
//                }
//            }
//
//        }
//
//        if (idOrSummary!=null){
//            criteria.orOperator(Criteria.where("id").regex(idOrSummary, "i"),
//                    Criteria.where("summary").regex(idOrSummary, "i"));
//        }
//
//        Query query=new Query(criteria);
//        return  mongoTemplate.find(query, Ticket.class);
//
//
//	}


}

















