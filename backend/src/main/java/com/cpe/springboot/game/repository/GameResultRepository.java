package com.cpe.springboot.game.repository;

import com.cpe.springboot.game.model.GameResultModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResultModel, Integer> {
}