package com.marien.jwt.backend.entities;


import com.marien.jwt.backend.models.TicketCategory;
import com.marien.jwt.backend.models.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 500)
    private String descriptionEven;

    private TicketCategory category;

    private Double price;

    private String imageFileName;

    private Date evenDate;

    private String localisation;

    private TicketStatus status;

}
