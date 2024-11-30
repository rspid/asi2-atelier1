package com.cpe.springboot.game.model;

import jakarta.persistence.*;

@Entity
public class GameResultModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer gameId;
    private Integer idGagnant;
    private Integer idPerdant;

    public GameResultModel() {
    }

    public GameResultModel(Integer gameId, Integer idGagnant, Integer idPerdant) {
        this.gameId = gameId;
        this.idGagnant = idGagnant;
        this.idPerdant = idPerdant;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
