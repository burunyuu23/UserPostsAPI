package com.example.userposts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;

@Data
public class UserDTO {
    @UUID(message = "User id should be in UUID format.")
    private String id;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    @JsonProperty("banner_image_url")
    private String bannerImageUrl;
    private LocalDate birthdate;
}
