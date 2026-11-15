package com.example.chat_service.service.impl;

import com.example.chat_service.config.MainServiceClient;
import com.example.chat_service.exceptionHandler.EntityNotFoundException;
import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Chat;
import com.example.chat_service.model.ChatUnread;
import com.example.chat_service.model.Message;
import com.example.chat_service.payload.request.CreateChatRequest;
import com.example.chat_service.payload.response.ChatResponse;
import com.example.chat_service.payload.response.UserResponse;
import com.example.chat_service.repository.ChatRepository;
import com.example.chat_service.repository.ChatUnreadRepository;
import com.example.chat_service.repository.MessageRepository;
import com.example.chat_service.service.ChatService;
import com.example.chat_service.util.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.example.chat_service.constant.AppConstants.CHAT_NOT_FOUND_BY_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final NotificationService notificationService;
    private final ChatUnreadRepository chatUnreadRepository;
    private final MainServiceClient mainServiceClient;

    @Override
    public Chat createChat(Long creatorUserId, CreateChatRequest request) throws ServiceException {
        log.info("Creating chat between users {} and {}", creatorUserId, request.getOtherUserId());

        Optional<Chat> existingChat = chatRepository.findByUserId1AndUserId2(creatorUserId, request.getOtherUserId());

        if (existingChat.isPresent()) {
            log.info("Chat already exists. chatId={}", existingChat.get().getId());
            return existingChat.get();
        }

        Chat chat = Chat.builder()
                .userId1(creatorUserId)
                .userId2(request.getOtherUserId())
                .build();
        return chatRepository.save(chat);
    }

    @Override
    public List<ChatResponse> getAllUserChats(Long userId) {
        log.info("process of getting all user's chats");
        List<Chat> chats = chatRepository.findAllByUserId(userId);

        return chats.stream()
                .map(chat -> buildChatResponse(chat, userId))
                .toList();
    }

    @Override
    public Chat getChats(Long userId, Long otherUserId) throws ServiceException {
        log.info("process of getting chat between users");
        return chatRepository.findChatBetweenUsers(userId, otherUserId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_NOT_FOUND_BY_ID));
    }

    @Override
    public List<ChatUnread> getChatsUnread(Long userId) throws ServiceException {
        log.info("process of getting unread chats");
        return chatUnreadRepository.findAllByUserId(userId);
    }

    @Override
    public void clearUnread(Long chatId, Long userId) {
        log.info("process of clearing unread chat messages");
        chatUnreadRepository.deleteByChatIdAndUserId(chatId, userId);
    }

    @Override
    public Set<Message> getChatHistory(Long chatId) throws ServiceException {
        log.info("process of getting chat messages");
        Chat chat = chatRepository.findChatById(chatId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_NOT_FOUND_BY_ID));
        if (chat.getMessages() != null) {
            return messageRepository.findByChat(chat);
        } else {
            log.info("chat hasn't any messages");
            return null;
        }
    }

    @Override
    public Chat getChat(Long messageId) {
        log.info("process of getting chat");
        Message message = messageRepository.findMessageById(messageId);
        return message.getChat();
    }

    private ChatResponse buildChatResponse(Chat chat, Long currentUserId) {
        Long otherUserId = Objects.equals(chat.getUserId1(), currentUserId)
                ? chat.getUserId2()
                : chat.getUserId1();

        UserResponse otherUser =
                mainServiceClient.getUser(otherUserId);

        return ChatResponse.builder()
                .id(chat.getId())
                .otherUserId(otherUser.getId())
                .otherUserFirstname(otherUser.getFirstname())
                .otherUserLastname(otherUser.getLastname())
                .build();
    }
}
