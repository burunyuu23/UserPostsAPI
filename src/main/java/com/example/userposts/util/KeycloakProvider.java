package com.example.userposts.util;

import com.example.userposts.dto.FullUserDTO;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class KeycloakProvider {
    public static FullUserDTO getUser(String username) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> http = new HttpEntity<>(headers);

        ResponseEntity<FullUserDTO> response = restTemplate.exchange(
                "http://localhost:5001/api/keycloak/user/search/" + username,
                HttpMethod.GET,
                http,
                FullUserDTO.class
        );

        return response.getBody();
    }
}
