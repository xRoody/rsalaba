package com.example.demo.repositories;

import com.example.demo.entities.KeyPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KeyPairRepository extends JpaRepository<KeyPair, UUID> {
    Optional<KeyPair> findByPublicKey(String key);
    boolean existsByPublicKey(String key);
}
