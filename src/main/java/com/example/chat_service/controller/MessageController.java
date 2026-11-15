package com.example.chat_service.controller;

import com.example.chat_service.api.MessageApi;
import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Message;
import com.example.chat_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @Override
    public ResponseEntity<Message> updateMessage(Long messageId, String content) throws ServiceException {
        log.info("request to update message with id" + messageId);
        Message message = messageService.updateMessage(messageId, content);
        return ResponseEntity.ok(message);
    }

    @Override
    public ResponseEntity<Void> deleteMessage(Long messageId, String content) throws ServiceException {
        log.info("request to delete message by id - {}", messageId);
        messageService.deleteMessage(messageId, content);
        return ResponseEntity.noContent().build();
    }
}
