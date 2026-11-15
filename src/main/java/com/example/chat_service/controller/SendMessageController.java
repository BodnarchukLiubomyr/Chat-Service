package com.example.chat_service.controller;

import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.GroupMessage;
import com.example.chat_service.model.Message;
import com.example.chat_service.payload.request.GroupMessageRequest;
import com.example.chat_service.payload.request.MessageRequest;
import com.example.chat_service.payload.response.MessageResponse;
import com.example.chat_service.service.GroupMessageService;
import com.example.chat_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SendMessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GroupMessageService groupMessageService;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload MessageRequest request,
                            Principal principal) throws ServiceException {
        log.info("request to send message by user: " + principal.getName());
        Long userId = Long.valueOf(principal.getName());
        Message savedMessage = messageService.sendMessage(
                request.getChatId(), userId, request.getContent());

        MessageResponse response = new MessageResponse(
                savedMessage.getContent(),
                savedMessage.getSenderId(),
                savedMessage.getTimestamp()
        );

        messagingTemplate.convertAndSend("/topic/chat/" + request.getChatId(), response);
    }

    @MessageMapping("/group/group-send")
    public void sendGroupMessage(@Payload GroupMessageRequest request,
                                 Principal principal) throws ServiceException {
        log.info("request to send message by user with id: " + principal.getName());
        Long userId = Long.valueOf(principal.getName());
        GroupMessage savedMessage = groupMessageService.sendMessage(
                request.getGroupId(), userId, request.getContent());

        MessageResponse response = new MessageResponse(
                savedMessage.getContent(),
                userId,
                savedMessage.getTimestamp()
        );

        messagingTemplate.convertAndSend("/topic/group/" + request.getGroupId(), response);
    }
}
