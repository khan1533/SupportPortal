package com.fireflink.dao.daoimpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.fireflink.dao.TicketDao;
import com.fireflink.model.Ticket;
import com.fireflink.service.SequenceGeneratorService;
import com.fireflink.utility.ResponseStructure;
import com.fireflink.utility.ResponseStructureAPI;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fireflink.repository.TickerRepository;
import com.fireflink.responsedto.TicketResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TicketDaoImpl implements TicketDao {
	
	private final SequenceGeneratorService generatorService;
	private final ModelMapper modelMapper;
	private final MongoTemplate mongoTemplate;
	private final TickerRepository tickerRepository;

	@Override
	public Ticket createTicket(Ticket ticket) {
		ticket.setId(generatorService.generetedSequence(Ticket.SEQUENCE_NAME));
//modelMapper.getConfiguration().setSkipNullEnabled()
		ticket=mongoTemplate.insert(ticket);
		return ticket;
		
	}

	@Override
	public Ticket findById(String ticketId) {
		return tickerRepository.findById(ticketId).orElseThrow(()->new NoSuchElementException("invalid email id, couldn't find ticket "));

	}

	@Override
	public ResponseEntity<ResponseStructure> assignTicket(Ticket ticket) {
		ticket=mongoTemplate.save(ticket);
		return ResponseStructureAPI.createResponse(modelMapper.map(ticket, TicketResponse.class));
	}

	@Override
	public List<Ticket> getAllTickets(Pageable pageable) {
//		List<TicketResponse> list = tickerRepository.findAll()
//				.stream().map(ticket -> modelMapper.map(ticket, TicketResponse.class))
//				.sorted((t1, t2) -> t2.getCreatedOn().compareTo(t1.getCreatedOn())).toList();
		return tickerRepository.findAll(pageable).getContent();

	}

	@Override
	public ResponseEntity<ResponseStructure> updateById(Ticket updated) {
		Ticket ticket = mongoTemplate.save(updated);
		return ResponseStructureAPI.okResponse(modelMapper.map(ticket, TicketResponse.class));
	}

	@Override
	public ResponseEntity<Ticket> changeAssignee(Ticket ticket) {
		ticket= tickerRepository.save(ticket);
		return ResponseEntity.ok(ticket);
	}

	@Override
	public ResponseEntity<ResponseStructure> updateTicketStatus(Ticket statusUpdatedTicket) {
		Ticket ticket=mongoTemplate.save(statusUpdatedTicket);
		return ResponseStructureAPI.okResponse(ticket);
	}

	@Override
	public ResponseEntity<ResponseStructure> removeAssignee(Ticket assigeeRemoved) {
	Ticket ticket=	mongoTemplate.save(assigeeRemoved);
		return ResponseStructureAPI.okResponse(ticket);
	}


}
