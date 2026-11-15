package com.example.chat_service.repository;

import com.example.chat_service.model.ChatUnread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ChatUnreadRepository extends JpaRepository<ChatUnread, Long> {
    List<ChatUnread> findAllByUserId(Long userId);
    void deleteByChatIdAndUserId(Long chatId, Long userId);
}
