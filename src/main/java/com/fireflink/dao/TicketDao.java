package com.fireflink.dao;

import java.util.List;
import java.util.Optional;

import com.fireflink.model.Ticket;
import com.fireflink.utility.ResponseStructure;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TicketDao {

 Ticket createTicket(Ticket ticket);

Ticket findById(String ticketId);

ResponseEntity<ResponseStructure> assignTicket(Ticket ticket);

 List<Ticket> getAllTickets(Pageable pageable);


 ResponseEntity<ResponseStructure> updateById(Ticket updated);

 ResponseEntity<Ticket> changeAssignee(Ticket ticket);

 ResponseEntity<ResponseStructure> updateTicketStatus(Ticket statusUpdatedTicket);

 ResponseEntity<ResponseStructure> removeAssignee(Ticket assigeeRemoved);
}
