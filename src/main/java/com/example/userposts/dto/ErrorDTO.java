package com.example.userposts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ErrorDTO {
    private String message;
    private LocalDateTime timestamp;

    public static ErrorDTO withCurrentTimestamp(String message) {
        return new ErrorDTO(message, LocalDateTime.now());
    }
}
