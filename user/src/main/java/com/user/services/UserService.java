package com.user.services;

import com.user.dto.AuthResponse;
import com.user.entities.User;
import com.user.kafka.UserEventProducer;
import com.user.repositories.UserRepository;
import com.user.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // Per criptare
    private final UserEventProducer userEventProducer;   // Per Kafka
    private final JwtService jwtService; //Per JWT


    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       UserEventProducer userEventProducer, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEventProducer = userEventProducer;
        this.jwtService = jwtService;
    }

    @Transactional
    public User register(User user) {
        log.info("inizio \n");
        log.info("Tentativo di registrazione per l'email: {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("ERRORE: Email {} già presente nel database!", user.getEmail());
            throw new RuntimeException("Email già registrata");
        }

        // CRIPTAGGIO: Mai salvare password in chiaro!
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("Utente salvato con successo. ID: {}", savedUser.getId());

        // KAFKA: Notifica gli altri microservizi
        userEventProducer.sendUserRegistered(savedUser.getId(), savedUser.getEmail());

        log.info("fine \n");
        return savedUser;
    }

    public AuthResponse login(String email, String password) {
        log.info("inizio login \n");
        log.info("Tentativo di login per l'utente: {}", email);

        // 1. Ricerca utente sul database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("ERRORE LOGIN: Utente con email {} non trovato", email);
                    return new RuntimeException("Credenziali non valide"); // Messaggio generico per sicurezza
                });

        // 2. Confronto password (Chiaro vs Hash)
        log.info("Utente trovato. Verifica della password in corso...");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("ERRORE LOGIN: Password errata per l'utente {}", email);
            throw new RuntimeException("Credenziali non valide");
        }

        // 3. Generazione del Token reale tramite il JwtService
        log.info("Credenziali corrette. Generazione del token JWT per il ruolo: {}", user.getRole());
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        log.info("Login completato con successo per {}", email);
        log.info("fine login \n");

        return new AuthResponse(user.getEmail(), token, user.getRole().name());
    }
}
