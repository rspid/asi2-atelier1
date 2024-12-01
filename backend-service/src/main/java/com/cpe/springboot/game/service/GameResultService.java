package com.cpe.springboot.game.service;

import com.cpe.springboot.game.model.GameResultModel;
import com.cpe.springboot.game.repository.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameResultService {

    @Autowired
    private GameResultRepository gameResultRepository;

    public GameResultModel saveGameResult(GameResultModel gameResultModel) {
        return gameResultRepository.save(gameResultModel);
    }
}