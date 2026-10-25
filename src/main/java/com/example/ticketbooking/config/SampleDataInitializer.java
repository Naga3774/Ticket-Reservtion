package com.example.ticketbooking.config;

import com.example.ticketbooking.entity.Ticket;
import com.example.ticketbooking.repository.TicketRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Development-only sample data. Disabled by default.
 * Enable with app.sample-data.enabled=true. It only inserts when the table is empty,
 * so restarts never create duplicates.
 */
@Component
@ConditionalOnProperty(name = "app.sample-data.enabled", havingValue = "true")
public class SampleDataInitializer implements CommandLineRunner {

    private final TicketRepository repository;

    public SampleDataInitializer(TicketRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                new Ticket("Cherry", "cherry@example.com", "Angallu", "Bangalore", "1234567890"),
                new Ticket("Arjun Rao", "arjun@example.com", "Hyderabad", "Chennai", "9876543210")
            ));
        }
    }
}
