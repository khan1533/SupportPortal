package com.fireflink.service;

import com.fireflink.dto.RequestTicketDto;
import com.fireflink.model.SearchHelper;
import com.fireflink.utility.ResponseStructure;
import org.springframework.http.ResponseEntity;

//import com.fireflink.ticketmanagement.dto.TicketDto;
import com.fireflink.model.Ticket;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TicketService {

	ResponseEntity<ResponseStructure> createTicket(RequestTicketDto ticketDto, String userEmail) throws IOException;

	ResponseEntity<ResponseStructure> assignTicket(String ticketId, String assigneeEmail, String assignerEmail);

	List<Ticket> getAllTickets();

	ResponseEntity<ResponseStructure> getTicketById(String id);

	ResponseEntity<ResponseStructure> updateById(String id, RequestTicketDto ticketDto, String email);

	ResponseEntity<Ticket> changeAssignee(String id, String newAssigneeEmail,String assignerEmail);

	ResponseEntity<ResponseStructure> deleteTicket(String id);

    ResponseEntity<ResponseStructure> updateTicketStatus(String id, String email);

    ResponseEntity<ResponseStructure> assignMultipleTickets(List<String> ticketList, String assigneeEmail, String assignerEmail);

    ResponseEntity<ResponseStructure> ticketsByUser(String email);

	ResponseEntity<ResponseStructure> removeAssignee(String ticketId, String assigneeEmail);

	ResponseEntity<ResponseStructure> searchBy(String id, String summary);

	List<Ticket> filters(Map<String, List<String>> filter);

	List<Ticket> searchAndFilter(SearchHelper searchHelper);


//	List<Ticket> filters(String status, String issueRelated, String createdOn, String severity, String priority);


//	Ticket assignTicketToUser(long ticketId, String username, String adminusername);

	
}
