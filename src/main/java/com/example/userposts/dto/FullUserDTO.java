package com.example.userposts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FullUserDTO {
    private String id;
    private String username;
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private Long createdTimestamp;
    private LocalDate birthdate;
    @JsonProperty("image_url")
    private String imageUrl;
}
