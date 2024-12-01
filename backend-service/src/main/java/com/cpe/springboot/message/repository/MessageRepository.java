package com.cpe.springboot.message.repository;

import com.cpe.springboot.message.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Long> {
}