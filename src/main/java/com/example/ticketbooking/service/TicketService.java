package com.example.ticketbooking.service;

import com.example.ticketbooking.entity.Ticket;
import com.example.ticketbooking.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/** Business logic layer between the controller and the repository. */
@Service
@Transactional
public class TicketService {

    private final TicketRepository repository;

    // Constructor injection
    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    /** INSERT: the id is cleared so the database always generates a new one. */
    public Ticket create(Ticket ticket) {
        ticket.setId(null);
        return repository.save(ticket);
    }

    /** SELECT all tickets sorted by id ascending. */
    @Transactional(readOnly = true)
    public List<Ticket> findAll() {
        return repository.findAllByOrderByIdAsc();
    }

    /** SELECT one ticket by id. */
    @Transactional(readOnly = true)
    public Optional<Ticket> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * UPDATE: loads the existing row and copies the five editable fields,
     * so the original id is always preserved.
     * @return true if updated, false if no ticket with that id exists
     */
    public boolean update(Long id, Ticket form) {
        return repository.findById(id).map(existing -> {
            existing.setPassengerName(form.getPassengerName());
            existing.setEmail(form.getEmail());
            existing.setSource(form.getSource());
            existing.setDestination(form.getDestination());
            existing.setPhone(form.getPhone());
            repository.save(existing);
            return true;
        }).orElse(false);
    }

    /** DELETE only the selected row. @return true if it existed */
    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
