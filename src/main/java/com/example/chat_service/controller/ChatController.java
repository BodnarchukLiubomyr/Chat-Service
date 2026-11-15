package com.example.chat_service.controller;

import com.example.chat_service.api.ChatApi;
import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Chat;
import com.example.chat_service.model.ChatUnread;
import com.example.chat_service.model.Message;
import com.example.chat_service.payload.request.CreateChatRequest;
import com.example.chat_service.payload.request.GetChatRequest;
import com.example.chat_service.payload.response.ChatResponse;
import com.example.chat_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController implements ChatApi {
    private final ChatService chatService;

    @Override
    public ResponseEntity<Chat> createChat(Jwt jwt, CreateChatRequest request) throws ServiceException {
        log.info("request to create chat by user with id: " + Long.valueOf(jwt.getSubject()));
        Chat chat = chatService.createChat(Long.valueOf(Objects.requireNonNull(jwt.getSubject())), request);
        return new ResponseEntity<Chat>(chat, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ChatResponse>> getUsersChat(Long userId) throws ServiceException {
        log.info("request to get user's chats " + userId);
        List<ChatResponse> chats = chatService.getAllUserChats(userId);
        return ResponseEntity.ok(chats);
    }

    @Override
    public ResponseEntity<Chat> getChatsByName(Long userId, GetChatRequest request) throws ServiceException {
        log.info("request to get chats by name " + request.getOtherUserId());
        Chat chat = chatService.getChats(userId,request.getOtherUserId());
        return ResponseEntity.ok(chat);
    }

    @Override
    public ResponseEntity<Set<Message>> getChatHistory(Long chatId) throws ServiceException {
        log.info("request to get chat history " + chatId);
        Set<Message> chatHistory = chatService.getChatHistory(chatId);
        return ResponseEntity.ok(chatHistory);
    }

    @Override
    public ResponseEntity<Chat> getChat(Long messageId) throws ServiceException {
        log.info("request to get chat by message with id " + messageId);
        Chat chat = chatService.getChat(messageId);
        return ResponseEntity.ok(chat);
    }

    @Override
    public ResponseEntity<List<ChatUnread>> getUnreadChats(Long userId) throws ServiceException {
        log.info("request to get chat with unread messages");
        List<ChatUnread> chats = chatService.getChatsUnread(userId);
        return ResponseEntity.ok(chats);
    }

    @Override
    public ResponseEntity<Void> clearUnread(Long chatId, Long userId) {
        log.info("message is read");
        chatService.clearUnread(chatId, userId);
        return ResponseEntity.noContent().build();
    }
}
