package org.example.requestmanagementservice.dto;

import java.util.Map;

public class CardRequestDto {
    private String userId;
    private String promptImage;
    private String promptText;
    private Float hp;
    private Float attack;
    private Float defense;
    private Float energy;

    // Getters et Setters
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
