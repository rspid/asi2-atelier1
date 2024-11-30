package com.cpe.springboot.game.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class GameResultModel {

    @Id
    private Integer gameId; // Utilisé comme clé primaire

    private Integer idGagnant;
    private Integer idPerdant;

    public GameResultModel() {
    }

    public GameResultModel(Integer gameId, Integer idGagnant, Integer idPerdant) {
        this.gameId = gameId;
        this.idGagnant = idGagnant;
        this.idPerdant = idPerdant;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getIdGagnant() {
        return idGagnant;
    }

    public void setIdGagnant(Integer idGagnant) {
        this.idGagnant = idGagnant;
    }

    public Integer getIdPerdant() {
        return idPerdant;
    }

    public void setIdPerdant(Integer idPerdant) {
        this.idPerdant = idPerdant;
    }
}
