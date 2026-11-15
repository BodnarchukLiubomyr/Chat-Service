package com.example.chat_service.controller;

import com.example.chat_service.api.GroupChatApi;
import com.example.chat_service.api.GroupMessageApi;
import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.GroupMessage;
import com.example.chat_service.payload.request.UpdateGroupMessageRequest;
import com.example.chat_service.service.GroupMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GroupMessageController implements GroupMessageApi, GroupChatApi {
    private final GroupMessageService groupChatService;

    @Override
    public ResponseEntity<Set<GroupMessage>> getGroupChatHistory(Long groupId) throws ServiceException {
        log.info("request to get history of group chat with id " + groupId);
        Set<GroupMessage> chatHistory = groupChatService.getChatHistory(groupId);
        return ResponseEntity.ok(chatHistory);
    }

    @Override
    public ResponseEntity<GroupMessage> updateMessage(Long messageId, UpdateGroupMessageRequest request) throws ServiceException {
        log.info("request to update group message with id" + messageId);
        GroupMessage message = groupChatService.updateMessage(messageId, request.getContent());
        return ResponseEntity.ok(message);
    }

    @Override
    public ResponseEntity<Void> deleteMessage(Long messageId, String content) throws ServiceException {
        log.info("request to delete group message by id - {}", messageId);
        groupChatService.deleteGroupMessage(messageId, content);
        return ResponseEntity.noContent().build();
    }
}
