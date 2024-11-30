package com.cpe.springboot.message.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // ID auto-généré

    private Integer senderId;
    private Integer receiverId;
    private String content;
    private LocalDateTime messageDate;

    public MessageModel() {
    }

    public MessageModel(Integer senderId, Integer receiverId, String content, LocalDateTime messageDate) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.messageDate = messageDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(LocalDateTime messageDate) {
        this.messageDate = messageDate;
    }
}
