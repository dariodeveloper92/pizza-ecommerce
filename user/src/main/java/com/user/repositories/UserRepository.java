package com.user.repositories;

import com.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Fondamentale per il Login e per evitare registrazioni doppie
    Optional<User> findByEmail(String email);

    // Per controllare velocemente se un'email è già usata
    Boolean existsByEmail(String email);
}