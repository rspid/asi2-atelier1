package com.cpe.springboot.game.controller;

import com.cpe.springboot.game.model.GameResultModel;
import com.cpe.springboot.game.service.GameResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-results")
public class GameResultController {

    @Autowired
    private GameResultService gameResultService;

    @PostMapping
    public ResponseEntity<GameResultModel> createGameResult(@RequestBody GameResultModel gameResultModel) {
        try {
            GameResultModel savedResult = gameResultService.saveGameResult(gameResultModel);
            return new ResponseEntity<>(savedResult, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}