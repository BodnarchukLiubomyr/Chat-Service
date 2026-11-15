package com.example.chat_service.service.impl;

import com.example.chat_service.exceptionHandler.DataProcessingException;
import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.GroupMessage;
import com.example.chat_service.model.GroupMessageUnread;
import com.example.chat_service.model.Notification;
import com.example.chat_service.repository.GroupMessageRepository;
import com.example.chat_service.repository.GroupMessageUnreadRepository;
import com.example.chat_service.service.GroupMessageService;
import com.example.chat_service.util.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.example.chat_service.constant.AppConstants.EMPTY_MESSAGE;
import static com.example.chat_service.constant.AppConstants.SAME_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupMessageServiceImpl implements GroupMessageService {
    private final GroupMessageRepository groupMessageRepository;
    private final GroupMessageUnreadRepository groupUnreadRepository;
    private final NotificationService notificationService;

    @Override
    public GroupMessage sendMessage(Long groupId, Long userId, String content) throws ServiceException {
        log.info("process of sending group message");

        GroupMessage groupMessage = groupMessageBuilder(userId, groupId, content);

        groupMessageRepository.save(groupMessage);

        Notification notification = notificationBuilder(
                "New Message Added",
                content.trim(),
                LocalDateTime.now(),
                groupId, userId);
        notificationService.sendNotificationToGroup(groupId, notification);
        return groupMessage;
    }

    public Set<GroupMessage> getChatHistory(Long groupId) throws ServiceException {
        log.info("process of getting group chat messages");
        return groupMessageRepository.findByGroupId(groupId);
    }

    @Override
    public List<GroupMessageUnread> getGroupsUnread(Long userId) throws ServiceException{
        log.info("process of getting unread chats");
        return groupUnreadRepository.findAllByUserId(userId);
    }

    @Override
    public void clearUnread(Long groupId, Long userId) {
        log.info("process of clearing unread chat messages");
        groupUnreadRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public GroupMessage updateMessage(Long messageId, String content) throws ServiceException {
        log.info("process of updating message with id:" + messageId);

        GroupMessage message = groupMessageRepository.findGroupMessageById(messageId);
        if(content == null){
            throw new DataProcessingException(EMPTY_MESSAGE);
        }
        else {
            if (content.equals(message.getContent())) {
                throw new DataProcessingException(SAME_MESSAGE);
            }
            message.setContent(content);
        }

        groupMessageRepository.save(message);

        return message;
    }

    @Override
    public void deleteGroupMessage(Long messageId, String content) throws ServiceException {
        log.info("process of deleting message with id"+ messageId);
        GroupMessage message = groupMessageRepository.findGroupMessageById(messageId);
        groupMessageRepository.delete(message);
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

    private GroupMessage groupMessageBuilder(Long userId, Long groupId, String content) {
        log.info("building group message");
        return GroupMessage.builder()
                .senderId(userId)
                .groupId(groupId)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
