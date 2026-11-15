package com.example.chat_service.service;

import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Message;

public interface MessageService {
    Message sendMessage(Long chatId, Long userId, String content) throws ServiceException;

    Message updateMessage(Long messageId, String content) throws ServiceException;

    void deleteMessage(Long messageId, String content) throws ServiceException;
}
