package com.example.ticketbooking.repository;

import com.example.ticketbooking.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Spring Data JPA generates all SQL (parameterized) for us. */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /** SELECT * FROM tickets ORDER BY id ASC */
    List<Ticket> findAllByOrderByIdAsc();
}
