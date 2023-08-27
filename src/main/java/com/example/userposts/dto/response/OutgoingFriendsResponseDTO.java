package com.example.userposts.dto.response;

import com.example.userposts.dto.OutgoingFriendsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OutgoingFriendsResponseDTO {
    private List<OutgoingFriendsDTO> outgoingFriends;
}
