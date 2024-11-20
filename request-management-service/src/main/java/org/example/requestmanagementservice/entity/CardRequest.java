package org.example.requestmanagementservice.entity;

import java.util.Map;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CardRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Génère un UUID
    private UUID id; // Utilise UUID comme ID principal

    private String userId;
    private String promptImage;
    private String promptText;
    private String status;
    private Float hp;
    private Float attack;
    private Float defense;
    private Float energy;

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPromptImage() {
        return promptImage;
    }

    public void setPromptImage(String promptImage) {
        this.promptImage = promptImage;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getHp() {
        return hp;
    }

    public void setHp(Float hp) {
        this.hp = hp;
    }

    public Float getAttack() {
        return attack;
    }

    public void setAttack(Float attack) {
        this.attack = attack;
    }

    public Float getDefense() {
        return defense;
    }

    public void setDefense(Float defense) {
        this.defense = defense;
    }

    public Float getEnergy() {
        return energy;
    }

    public void setEnergy(Float energy) {
        this.energy = energy;
    }

}
