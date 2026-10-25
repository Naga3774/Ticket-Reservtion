package com.example.ticketbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** JPA entity mapped to the "tickets" table. */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT
    private Long id;

    @NotBlank(message = "Passenger name is required")
    @Size(max = 100, message = "Passenger name must be at most 100 characters")
    @Column(name = "passenger_name", nullable = false, length = 100)
    private String passengerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 150, message = "Email must be at most 150 characters")
    @Column(nullable = false, length = 150)
    private String email;

    @NotBlank(message = "Source station is required")
    @Size(max = 100, message = "Source must be at most 100 characters")
    @Column(nullable = false, length = 100)
    private String source;

    @NotBlank(message = "Destination station is required")
    @Size(max = 100, message = "Destination must be at most 100 characters")
    @Column(nullable = false, length = 100)
    private String destination;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "Phone must be 10-13 digits (optional leading +)")
    @Column(nullable = false, length = 20)
    private String phone;

    public Ticket() { }

    public Ticket(String passengerName, String email, String source, String destination, String phone) {
        this.passengerName = passengerName;
        this.email = email;
        this.source = source;
        this.destination = destination;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
