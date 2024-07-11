package com.fireflink.repository;

import com.fireflink.model.Ticket;
import com.fireflink.utility.ResponseStructure;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TickerRepository extends MongoRepository<Ticket, String>{
    List<Ticket> findByIdStartingWith(String ticketPrefix);

    List<Ticket>  findBySummaryStartingWith(String summaryPrefix);
}
