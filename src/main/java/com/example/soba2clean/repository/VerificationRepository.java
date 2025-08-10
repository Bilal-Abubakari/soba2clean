package com.example.soba2clean.repository;

import com.example.soba2clean.model.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, String> {
    Optional<Verification> findByToken(String token);
}
