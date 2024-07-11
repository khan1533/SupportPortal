package com.fireflink.controller;

import com.fireflink.dto.RequestTicketDto;
import com.fireflink.model.SearchHelper;
import com.fireflink.model.Ticket;
import com.fireflink.service.TicketService;
import com.fireflink.utility.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import com.fireflink.ticketmanagement.dto.TicketDto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tickets")
public class TicketController {

	private final TicketService ticketService;
	
	@PostMapping
	public  ResponseEntity<ResponseStructure> createTicket(@ModelAttribute @Valid RequestTicketDto ticketDto,
														   @RequestParam String userEmail
														  ) throws IOException {

		return ticketService.createTicket(ticketDto, userEmail);

	}	
	
	@PutMapping("/assign")
	public ResponseEntity<ResponseStructure> assignTicket(@RequestParam String ticketId,
			@RequestParam String assigneeEmail, @RequestParam String assignerEmail){
		return ticketService.assignTicket(ticketId, assigneeEmail, assignerEmail);
		
	}
	
	
	@GetMapping
	public List<Ticket> getAllTickets() {
		return ticketService.getAllTickets();
	}
	
	
	@GetMapping("/{ticketId}")
	public ResponseEntity<ResponseStructure> getTicketById(@PathVariable(value = "ticketId") String ticketId){
		return ticketService.getTicketById(ticketId);
	}

	@PutMapping("/update/{ticketId}")
	public ResponseEntity<ResponseStructure> updateById(@PathVariable(value = "ticketId") String ticketId,
                                                        @RequestBody @Valid RequestTicketDto ticketDto,
                                                        @RequestParam String userEmail){
		return ticketService.updateById(ticketId, ticketDto, userEmail);
	}

	@PutMapping("/{ticketId}/reassign")
	public ResponseEntity<Ticket> changeAssignee(@PathVariable(value = "ticketId") String ticketId,
                                                 @RequestParam String newAssigneeEmail,
                                                 @RequestParam String assignerEmail){
		return ticketService.changeAssignee(ticketId,newAssigneeEmail,assignerEmail);
	}

	@DeleteMapping("/{deleteById}")
	public ResponseEntity<ResponseStructure> deleteTicket(@PathVariable(value = "deleteById") String ticketId){
		return ticketService.deleteTicket(ticketId);
	}

    @PutMapping("/updateStatus")
    public ResponseEntity<ResponseStructure> updateTicketStatus(@RequestParam String ticketId,
                                                                @RequestParam String userEmail){
        return ticketService.updateTicketStatus(ticketId,userEmail);
    }

    @PutMapping("/ticket_Ids")
    public  ResponseEntity<ResponseStructure> assignMultipleTickets(
            @RequestParam List<String> ticketList,
            @RequestParam String assigneeEmail, @RequestParam String assignerEmail){
        return ticketService.assignMultipleTickets(ticketList, assigneeEmail, assignerEmail);
    }

    @GetMapping("/ticketsCreatedByUser")
    public  ResponseEntity<ResponseStructure> ticketsByUser(@RequestParam String userEmail){
        return ticketService.ticketsByUser(userEmail);
    }

	@PutMapping("/removeAssignee")
	public ResponseEntity<ResponseStructure> removeAssignee(
		@RequestParam String ticketId,	@RequestParam String assigneeEmail){
		return ticketService.removeAssignee(ticketId, assigneeEmail);
	}

	@GetMapping("/searchByidOrSummary")
	public ResponseEntity<ResponseStructure> searchBy(@RequestParam Optional<String> ticketId,
													  @RequestParam Optional<String> summary){

		if (ticketId.isPresent()) {
			return ticketService.searchBy(ticketId.get(), null);
		} else if (summary.isPresent()) {
			return ticketService.searchBy(null, summary.get());
		} else {
			throw new IllegalArgumentException("Either ticketId or summary must be provided");
		}

//		return ticketService.searchBy(ticketId,summary);
	}

	@GetMapping("/filter")
	public List<Ticket> filters(@RequestBody Map<String, List<String>> filter) {
		return ticketService.filters(filter);
	}

	@GetMapping("/filterAndSearch")
	public List<Ticket> searchAndFilter(@RequestBody SearchHelper searchHelper)
	{
		return ticketService.searchAndFilter(searchHelper);
	}

}
