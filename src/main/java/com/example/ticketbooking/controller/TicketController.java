package com.example.ticketbooking.controller;

import com.example.ticketbooking.entity.Ticket;
import com.example.ticketbooking.service.TicketService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Handles all web routes. Uses Post-Redirect-Get to avoid duplicate submissions. */
@Controller
public class TicketController {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);
    private static final String DB_ERROR = "A database error occurred. Please try again.";

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/ticket";
    }

    /** Show empty booking form. */
    @GetMapping("/ticket")
    public String showForm(Model model) {
        if (!model.containsAttribute("ticket")) {
            model.addAttribute("ticket", new Ticket());
        }
        return "ticket-form";
    }

    /** Create a ticket. */
    @PostMapping("/ticket")
    public String create(@Valid @ModelAttribute("ticket") Ticket ticket,
                         BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "ticket-form"; // redisplay with validation messages
        }
        try {
            service.create(ticket);
        } catch (DataAccessException e) {
            log.error("Create failed", e);
            redirect.addFlashAttribute("error", DB_ERROR);
            return "redirect:/ticket";
        }
        redirect.addFlashAttribute("success", "Ticket booked successfully!");
        return "redirect:/tickets";
    }

    /** List all tickets. */
    @GetMapping("/tickets")
    public String list(Model model) {
        try {
            model.addAttribute("tickets", service.findAll());
        } catch (DataAccessException e) {
            log.error("List failed", e);
            model.addAttribute("tickets", java.util.List.of());
            model.addAttribute("error", DB_ERROR);
        }
        return "ticket-list";
    }

    /** Show edit form with existing data. */
    @GetMapping("/tickets/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            return service.findById(id).map(t -> {
                model.addAttribute("ticket", t);
                return "ticket-edit";
            }).orElseGet(() -> {
                redirect.addFlashAttribute("error", "Ticket #" + id + " was not found.");
                return "redirect:/tickets";
            });
        } catch (DataAccessException e) {
            log.error("Load failed", e);
            redirect.addFlashAttribute("error", DB_ERROR);
            return "redirect:/tickets";
        }
    }

    /** Update a ticket; the id always comes from the URL. */
    @PostMapping("/tickets/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("ticket") Ticket ticket,
                         BindingResult result, RedirectAttributes redirect) {
        ticket.setId(id); // keep id for redisplay on error
        if (result.hasErrors()) {
            return "ticket-edit";
        }
        try {
            if (!service.update(id, ticket)) {
                redirect.addFlashAttribute("error", "Ticket #" + id + " was not found.");
                return "redirect:/tickets";
            }
        } catch (DataAccessException e) {
            log.error("Update failed", e);
            redirect.addFlashAttribute("error", DB_ERROR);
            return "redirect:/tickets";
        }
        redirect.addFlashAttribute("success", "Ticket #" + id + " updated successfully!");
        return "redirect:/tickets";
    }

    /** Delete a ticket. */
    @PostMapping("/tickets/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            if (service.delete(id)) {
                redirect.addFlashAttribute("success", "Ticket #" + id + " deleted successfully!");
            } else {
                redirect.addFlashAttribute("error", "Ticket #" + id + " was not found.");
            }
        } catch (DataAccessException e) {
            log.error("Delete failed", e);
            redirect.addFlashAttribute("error", DB_ERROR);
        }
        return "redirect:/tickets";
    }

    /** Non-numeric id in URL, e.g. /tickets/edit/abc */
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public String invalidId(RedirectAttributes redirect) {
        redirect.addFlashAttribute("error", "Invalid ticket ID.");
        return "redirect:/tickets";
    }
}
