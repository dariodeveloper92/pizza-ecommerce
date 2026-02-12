package com.user.controllers;

import com.user.dto.AuthResponse;
import com.user.entities.User;
import com.user.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        log.info("API: Ricevuta richiesta di registrazione per l'email: {}", user.getEmail());

        User savedUser = userService.register(user);

        log.info("API: Registrazione completata con successo per l'utente ID: {}", savedUser.getId());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        log.info("API: Tentativo di login per l'utente: {}", loginRequest.getEmail());

        // Il Service controllerà le credenziali e genererà il token
        AuthResponse authResponse = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        log.info("API: Login effettuato con successo per: {}", loginRequest.getEmail());
        return ResponseEntity.ok(authResponse);
    }
}
