package com.example.userposts.controller;

import com.example.userposts.dto.FullUserDTO;
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
import com.example.userposts.util.KeycloakProvider;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
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

    @GetMapping("/{username}")
    public ResponseEntity<FullUserDTO> getByUsername(@PathVariable("username") String username) {
        FullUserDTO fullUserDTO = KeycloakProvider.getUser(username);

        Optional<User> optUser = usersService.getUserById(fullUserDTO.getId());
        UserDTO userDTO = convertToUserDTO(optUser.orElseThrow(() -> new UserNotFoundException(fullUserDTO.getId())));

        fullUserDTO.setBirthdate(userDTO.getBirthdate());
        fullUserDTO.setImageUrl(userDTO.getImageUrl());
        return ResponseEntity.ok(fullUserDTO);
    }
    @GetMapping("/profile")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<FullUserDTO> getProfile() {
        SecurityContext context = SecurityContextHolder.getContext();
        Jwt jwt = (Jwt) context.getAuthentication().getPrincipal();

        String userId = jwt.getClaims().get("sub").toString();
        String username = jwt.getClaims().get("preferred_username").toString();

        Optional<User> optUser = usersService.getUserById(userId);
        UserDTO userDTO = convertToUserDTO(optUser.orElseThrow(() -> new UserNotFoundException(userId)));

        FullUserDTO fullUserDTO = KeycloakProvider.getUser(username);
        fullUserDTO.setBirthdate(userDTO.getBirthdate());
        fullUserDTO.setImageUrl(userDTO.getImageUrl());
        return ResponseEntity.ok(fullUserDTO);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('admin')")
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
