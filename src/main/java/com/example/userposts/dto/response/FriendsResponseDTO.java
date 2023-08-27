package com.example.userposts.dto.response;

import com.example.userposts.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FriendsResponseDTO {
    private List<UserDTO> friends;
}
