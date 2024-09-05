package com.marien.jwt.backend.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "validation")
public class Validation {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;
    private Instant creation;
    private Instant expiration;
    private Instant activation;
    private String code;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private User user;
}
