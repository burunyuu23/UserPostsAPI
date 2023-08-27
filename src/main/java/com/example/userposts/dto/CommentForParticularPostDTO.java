package com.example.userposts.dto;

import lombok.Data;

@Data
public class CommentForParticularPostDTO {
    private UserDTO user;
    private String body;
    private int likesNumber;
}
