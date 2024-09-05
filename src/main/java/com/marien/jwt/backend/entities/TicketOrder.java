package com.marien.jwt.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TicketOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderId;

    private Date orderDate;

    @ManyToOne
    private Ticket ticket;

    private Double price;

    private Integer quantity;

    @ManyToOne
    private User user;

    private String status;

    private String paymentType;
}
