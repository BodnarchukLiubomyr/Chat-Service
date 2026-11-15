package com.example.chat_service.service;

import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.GroupMessage;
import com.example.chat_service.model.Notification;
import com.example.chat_service.repository.GroupMessageRepository;
import com.example.chat_service.repository.GroupMessageUnreadRepository;
import com.example.chat_service.service.impl.GroupMessageServiceImpl;
import com.example.chat_service.util.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupMessageServiceImplTests {
    @InjectMocks
    private GroupMessageServiceImpl groupMessageService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Notification notification;
    @Mock
    private GroupMessageUnreadRepository groupUnreadRepository;
    @Mock
    private GroupMessageRepository groupMessageRepository;
    @Mock
    private GroupMessage groupMessage;

    private Set<GroupMessage> messages = new HashSet<>();

    private static final Long ID = 1L;
    private static final String VALID_EMAIL1 = "client0@example.com";
    private static final String VALID_PASSWORD = "Client00";
    private static final String VALID_SUBJECT_NAME = "TEST";
    private static final String VALID_SCHOOL_CLASS_NAME = "7-B";
    private static final String VALID_GROUP_NAME = "TEST";
    private static final String VALID_FIRSTNAME = "Liubomyr";
    private static final String VALID_LASTNAME = "Bodnarchuk";
    private static final String CONTENT = "test";
    private static final String CONTENT1 = "test1";

    @BeforeEach
    void setGroupMessage() {
        groupMessage = GroupMessage.builder()
                .id(ID)
                .senderId(ID)
                .groupId(ID)
                .content(CONTENT)
                .timestamp(LocalDateTime.now())
                .build();

        messages.add(groupMessage);
    }

    @BeforeEach
    void setUpNotification() {
        notification = Notification.builder()
                .title(CONTENT)
                .content(CONTENT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    public void getChatHistorySuccessfullyTest() throws ServiceException {
        when(groupMessageRepository.findByGroupId(ID)).thenReturn(messages);
        Set<GroupMessage> messages1 = groupMessageService.getChatHistory(ID);
        assertEquals(1, messages1.size());
    }

    @Test
    public void sendMessageTest() throws ServiceException {
        when(groupMessageRepository.save(any(GroupMessage.class))).thenAnswer(invocation -> {
            GroupMessage savedMessage = invocation.getArgument(0);
            savedMessage.setId(1L);
            return savedMessage;
        });
        notificationService.sendNotificationToGroup(ID,notification);
        GroupMessage groupMessage1 = groupMessageService.sendMessage(ID,ID,CONTENT);
        verify(groupMessageRepository).save(groupMessage1);
        assertEquals(groupMessage, groupMessage1);
    }

    @Test
    public void updateGroupMessageSuccessfullyTest() throws ServiceException {
        when(groupMessageRepository.findGroupMessageById(ID)).thenReturn(groupMessage);
        GroupMessage groupMessage1 = groupMessageService.updateMessage(ID,CONTENT1);
        verify(groupMessageRepository).save(groupMessage1);
        assertEquals(CONTENT1, groupMessage1.getContent());
    }

    @Test
    public void deleteGroupMessageSuccessfullyTest(){
        when(groupMessageRepository.findGroupMessageById(ID)).thenReturn(groupMessage);
        assertDoesNotThrow(()->groupMessageService.deleteGroupMessage(ID,CONTENT));
    }
}
