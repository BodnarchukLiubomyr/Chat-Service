package com.example.chat_service.repository;

import com.example.chat_service.model.Chat;
import com.example.chat_service.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message,Long> {
    Set<Message> findByChat(Chat chat);
    Message findMessageById(Long id);
}
