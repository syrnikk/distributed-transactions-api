package com.syrnik.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syrnik.dto.RegisterDTO;
import com.syrnik.dto.UserClientDTO;
import com.syrnik.security.UserDetailsImpl;
import com.syrnik.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")

public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
        log.info("REST Register user: {}", dto);
        userService.registerUser(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserClientDTO> getLoggedInUser(Authentication authentication) {
        log.info("REST Get currently logged in user");
        if(authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            UserClientDTO userClientDTO = userService.getFullUserInfo(userDetails.getId());
            return ResponseEntity.ok(userClientDTO);
        }
        return ResponseEntity.internalServerError().build();
    }
}
