package com.example.userposts.controller;

import com.example.userposts.dto.IncomingFriendsDTO;
import com.example.userposts.dto.OutgoingFriendsDTO;
import com.example.userposts.dto.UserDTO;
import com.example.userposts.dto.response.FriendsResponseDTO;
import com.example.userposts.dto.response.IncomingFriendsResponseDTO;
import com.example.userposts.dto.response.OutgoingFriendsResponseDTO;
import com.example.userposts.model.Friends;
import com.example.userposts.model.User;
import com.example.userposts.service.UsersService;
import com.example.userposts.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    public UsersController(UsersService usersService, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(usersService.getAll().stream().map(this::convertToUserDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") String id) {
        Optional<User> optUser = usersService.getUserById(id);
        return ResponseEntity.ok(convertToUserDTO(optUser.orElseThrow(() -> new UserNotFoundException(id))));
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody UserDTO user) {
        usersService.addUser(user);
        return ResponseEntity.ok("User added!");
    }

    @GetMapping("/{id}/friends/incoming")
    public ResponseEntity<IncomingFriendsResponseDTO> getIncomingFriendRequests(@PathVariable("id") String id) {
        Optional<User> optUser = usersService.getUserById(id);
        User user = optUser.orElseThrow(() -> new UserNotFoundException(id));
        List<Friends> incoming = user.getIncoming();
        return ResponseEntity.ok(new IncomingFriendsResponseDTO(incoming.stream().map(this::convertToIncomingFriendsDTO).toList()));
    }

    @GetMapping("/{id}/friends/outgoing")
    public ResponseEntity<OutgoingFriendsResponseDTO> getOutgoingFriendRequests(@PathVariable("id") String id) {
        Optional<User> optUser = usersService.getUserById(id);
        User user = optUser.orElseThrow(() -> new UserNotFoundException(id));
        List<Friends> outgoing = user.getOutgoing();
        return ResponseEntity.ok(new OutgoingFriendsResponseDTO(outgoing.stream().map(this::convertToOutgoingFriendsDTO).toList()));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<FriendsResponseDTO> getFriends(@PathVariable("id") String id) {
        return ResponseEntity.ok(new FriendsResponseDTO(usersService.getUserFriendsById(id).stream().map(this::convertToUserDTO).toList()));
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private IncomingFriendsDTO convertToIncomingFriendsDTO(Friends friends) {
        return modelMapper.map(friends, IncomingFriendsDTO.class);
    }

    private OutgoingFriendsDTO convertToOutgoingFriendsDTO(Friends friends) {
        return modelMapper.map(friends, OutgoingFriendsDTO.class);
    }
}
