package com.example.ticketbooking;

import com.example.ticketbooking.entity.Ticket;
import com.example.ticketbooking.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TicketCrudTest {

    @Autowired MockMvc mvc;
    @Autowired TicketRepository repo;

    @BeforeEach
    void clean() { repo.deleteAll(); }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder validPost(String url, String name) {
        return post(url).param("passengerName", name).param("email", name.toLowerCase() + "@example.com")
                .param("source", "Angallu").param("destination", "Bangalore").param("phone", "1234567890");
    }

    @Test void rootRedirectsToForm() throws Exception {
        mvc.perform(get("/")).andExpect(redirectedUrl("/ticket"));
    }

    @Test void formOpens() throws Exception {
        mvc.perform(get("/ticket")).andExpect(status().isOk()).andExpect(content().string(containsString("Ticket Booking Form")));
    }

    @Test void emptyDatabaseShowsMessage() throws Exception {
        mvc.perform(get("/tickets")).andExpect(status().isOk())
           .andExpect(content().string(containsString("No tickets available. Book your first ticket.")));
    }

    @Test void createMultipleAndList() throws Exception {
        mvc.perform(validPost("/ticket", "Cherry")).andExpect(redirectedUrl("/tickets"));
        mvc.perform(validPost("/ticket", "Arjun")).andExpect(redirectedUrl("/tickets"));
        assertEquals(2, repo.count());
        mvc.perform(get("/tickets")).andExpect(content().string(containsString("cherry@example.com")))
           .andExpect(content().string(containsString("arjun@example.com")));
    }

    @Test void invalidDataRejected() throws Exception {
        mvc.perform(post("/ticket").param("passengerName", "").param("email", "bad").param("source", "")
                .param("destination", "").param("phone", "12"))
           .andExpect(status().isOk()).andExpect(view().name("ticket-form"));
        assertEquals(0, repo.count());
    }

    @Test void updateKeepsIdAndOnlyChangesTarget() throws Exception {
        Ticket a = repo.save(new Ticket("A", "a@example.com", "X", "Y", "1111111111"));
        Ticket b = repo.save(new Ticket("B", "b@example.com", "X", "Y", "2222222222"));
        mvc.perform(get("/tickets/edit/" + a.getId())).andExpect(status().isOk())
           .andExpect(content().string(containsString("a@example.com")));
        mvc.perform(post("/tickets/update/" + a.getId()).param("id", "999").param("passengerName", "A2")
                .param("email", "a2@example.com").param("source", "P").param("destination", "Q").param("phone", "3333333333"))
           .andExpect(redirectedUrl("/tickets"));
        Ticket updated = repo.findById(a.getId()).orElseThrow();
        assertEquals("A2", updated.getPassengerName());
        assertEquals(a.getId(), updated.getId());
        assertEquals("B", repo.findById(b.getId()).orElseThrow().getPassengerName());
    }

    @Test void deleteRemovesOnlySelected() throws Exception {
        Ticket a = repo.save(new Ticket("A", "a@example.com", "X", "Y", "1111111111"));
        Ticket b = repo.save(new Ticket("B", "b@example.com", "X", "Y", "2222222222"));
        mvc.perform(post("/tickets/delete/" + a.getId())).andExpect(redirectedUrl("/tickets"));
        assertFalse(repo.existsById(a.getId()));
        assertTrue(repo.existsById(b.getId()));
    }

    @Test void missingAndInvalidIdsHandled() throws Exception {
        mvc.perform(get("/tickets/edit/9999")).andExpect(redirectedUrl("/tickets"));
        mvc.perform(get("/tickets/edit/abc")).andExpect(redirectedUrl("/tickets"));
        mvc.perform(post("/tickets/delete/9999")).andExpect(redirectedUrl("/tickets"));
    }
}
