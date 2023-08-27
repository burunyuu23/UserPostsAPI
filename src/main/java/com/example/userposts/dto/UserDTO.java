package com.example.userposts.dto;

import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;

@Data
public class UserDTO {
    @UUID(message = "User id should be in UUID format.")
    private String id;
    private String imageUrl;
    private LocalDate birthdate;
}
