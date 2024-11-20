package org.example.requestmanagementservice.repository;

import org.example.requestmanagementservice.entity.CardRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardRequestRepository extends JpaRepository<CardRequest, UUID> { // Utilise UUID comme type d'ID
                                                                                  // principal
    // `findById(UUID uuid)` est déjà disponible via JpaRepository
}
