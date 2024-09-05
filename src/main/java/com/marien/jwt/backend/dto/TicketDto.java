package com.marien.jwt.backend.dto;

import com.marien.jwt.backend.models.TicketCategory;
import com.marien.jwt.backend.models.TicketStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class TicketDto {

    @Size(min=10, message = "The description should be at least 10 characters")
    @Size(max=2000, message = "The description cannot exceed 2000 characters")
    private String descriptionEven;

    private Date evenDate;

    private String localisation;

    @NotEmpty(message = "obligatoire")
    private TicketCategory category;

    @Min(0)
    private Double price;

    private TicketStatus status;

    private MultipartFile imageFile;




}
