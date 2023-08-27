package com.example.userposts.dto.response;

import com.example.userposts.dto.IncomingFriendsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IncomingFriendsResponseDTO {
    private List<IncomingFriendsDTO> incomingFriends;
}
