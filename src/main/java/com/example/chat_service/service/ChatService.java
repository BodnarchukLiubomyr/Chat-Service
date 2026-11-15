package com.example.chat_service.service;

import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Chat;
import com.example.chat_service.model.ChatUnread;
import com.example.chat_service.model.Message;
import com.example.chat_service.payload.request.CreateChatRequest;
import com.example.chat_service.payload.response.ChatResponse;

import java.util.List;
import java.util.Set;

public interface ChatService {
    Chat createChat(Long creatorUserId, CreateChatRequest request) throws ServiceException;

    List<ChatResponse> getAllUserChats(Long userId) throws ServiceException;

    Chat getChats(Long userId, Long otherUserId) throws ServiceException;

    Set<Message> getChatHistory(Long chatId) throws ServiceException;

    List<ChatUnread> getChatsUnread(Long userId) throws ServiceException;

    void clearUnread(Long chatId, Long userId);

    Chat getChat(Long messageId) throws ServiceException;
}
