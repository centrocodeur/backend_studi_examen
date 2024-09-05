package com.marien.jwt.backend.models;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String paymentType;

}
