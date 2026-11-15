package com.example.chat_service.service.impl;

import com.example.chat_service.exceptionHandler.DataProcessingException;
import com.example.chat_service.exceptionHandler.EntityNotFoundException;
import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Chat;
import com.example.chat_service.model.ChatUnread;
import com.example.chat_service.model.Message;
import com.example.chat_service.model.Notification;
import com.example.chat_service.repository.ChatRepository;
import com.example.chat_service.repository.ChatUnreadRepository;
import com.example.chat_service.repository.MessageRepository;
import com.example.chat_service.service.MessageService;
import com.example.chat_service.util.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.chat_service.constant.AppConstants.CHAT_NOT_FOUND_BY_ID;
import static com.example.chat_service.constant.AppConstants.EMPTY_MESSAGE;
import static com.example.chat_service.constant.AppConstants.SAME_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;
    private final ChatUnreadRepository chatUnreadRepository;

    @Transactional
    @Override
    public Message sendMessage(Long chatId, Long userId, String content) throws ServiceException {
        log.info("process of sending message");

        Chat chat = chatRepository.findChatById(chatId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_NOT_FOUND_BY_ID));

        if (content == null || content.isBlank()) {
            throw new DataProcessingException(EMPTY_MESSAGE);
        }

        validateChatParticipant(chat, userId);
        Long recipientId = getRecipientId(chat, userId);

        chatUnreadRepository.save(
                ChatUnread.builder()
                        .chatId(chatId)
                        .userId(recipientId)
                        .build()
        );

        Message message = messageBuilder(userId, chat, content);
        Notification notification = notificationBuilder(
                "New Message Added",
                content.trim(),
                LocalDateTime.now(),
                chatId, userId);
        notificationService.sendNotificationToUser(recipientId, notification);
        return messageRepository.save(message);
    }

    @Override
    public Message updateMessage(Long messageId, String content) throws ServiceException {
        log.info("process of updating message with id:" + messageId);

        Message message = messageRepository.findMessageById(messageId);
        if (content == null) {
            throw new DataProcessingException(EMPTY_MESSAGE);
        } else {
            if (content.equals(message.getContent())) {
                throw new DataProcessingException(SAME_MESSAGE);
            }
            message.setContent(content);
        }

        messageRepository.save(message);

        return message;
    }

    @Override
    public void deleteMessage(Long messageId, String content) throws ServiceException {
        log.info("process of deleting message with id" + messageId);
        Message message = messageRepository.findMessageById(messageId);
        messageRepository.delete(message);
    }

    private void validateChatParticipant(Chat chat, Long userId) throws ServiceException {
        boolean participant = Objects.equals(chat.getUserId1(), userId) ||
                Objects.equals(chat.getUserId2(), userId);
        if (!participant) {
            throw new DataProcessingException(
                    "User is not a participant of this chat"
            );
        }
    }

    private Long getRecipientId(Chat chat, Long senderId) throws ServiceException {
        if (Objects.equals(chat.getUserId1(), senderId)) {
            return chat.getUserId2();
        }
        if (Objects.equals(chat.getUserId2(), senderId)) {
            return chat.getUserId1();
        }
        throw new DataProcessingException(
                "User is not a member of this chat"
        );
    }

    private Notification notificationBuilder(String title, String content, LocalDateTime timestamp,
                                             Long chatId, Long senderId) {
        log.info("building notification");
        return Notification.builder()
                .title(title)
                .content(content)
                .timestamp(timestamp)
                .chatId(chatId)
                .senderId(senderId)
                .build();
    }

    private Message messageBuilder(Long senderId, Chat chat, String content) {
        log.info("building message");
        return Message.builder()
                .senderId(senderId)
                .chat(chat)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
