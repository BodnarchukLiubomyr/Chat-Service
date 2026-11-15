package com.example.chat_service.service;

import com.example.chat_service.config.MainServiceClient;
import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Chat;
import com.example.chat_service.model.ChatUnread;
import com.example.chat_service.model.Message;
import com.example.chat_service.model.Notification;
import com.example.chat_service.model.Role;
import com.example.chat_service.payload.request.CreateChatRequest;
import com.example.chat_service.payload.request.GetChatRequest;
import com.example.chat_service.payload.response.ChatResponse;
import com.example.chat_service.payload.response.UserResponse;
import com.example.chat_service.repository.ChatRepository;
import com.example.chat_service.repository.ChatUnreadRepository;
import com.example.chat_service.repository.MessageRepository;
import com.example.chat_service.service.impl.ChatServiceImpl;
import com.example.chat_service.service.impl.MessageServiceImpl;
import com.example.chat_service.util.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceImplTests {
    @InjectMocks
    private MessageServiceImpl messageService;
    @InjectMocks
    private ChatServiceImpl chatService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private Notification notification;
    @Mock
    private Chat chat;
    @Mock
    private Chat chat1;
    @Mock
    private ChatUnread chatUnread;
    @Mock
    private Message message;

    private Set<Message> messages = new HashSet<>();

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private ChatUnreadRepository chatUnreadRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private CreateChatRequest createChatRequest;
    @Mock
    private GetChatRequest getChatRequest;
    @Mock
    private ChatResponse chatResponse;
    @Mock
    private UserResponse userResponse;
    @Mock
    private MainServiceClient mainServiceClient;

    private static final Long ID = 1L;
    private static final Long ID1 = 2L;

    private static final Long ID2 = 3L;
    private static final Long ID3 = 4L;

    private static final String VALID_EMAIL = "client0@example.com";
    private static final String VALID_EMAIL1 = "client01@example.com";
    private static final String VALID_FIRSTNAME = "Liubomyr";
    private static final String VALID_LASTNAME = "Bodnarchuk";

    private static final String VALID_FIRSTNAME1 = "Liubomy";
    private static final String VALID_LASTNAME1 = "Bodnarchu";

    private static final String VALID_PASSWORD = "Client00";

    private static final String CONTENT = "test";
    private static final String CONTENT1 = "test1";

    @BeforeEach
    void setUp() {
        chat = Chat.builder()
                .id(ID)
                .userId1(ID)
                .userId2(ID1)
                .build();

        message = Message.builder()
                .id(ID)
                .content(CONTENT)
                .senderId(ID)
                .chat(chat)
                .timestamp(LocalDateTime.now())
                .build();

        messages.add(message);

        chat.setMessages(messages);
        chatUnread = ChatUnread.builder()
                .chatId(ID)
                .userId(ID)
                .build();
    }

    @BeforeEach
    void setChat1() {
        chat1 = Chat.builder()
                .id(ID1)
                .userId1(ID1)
                .userId2(ID2)
                .build();
    }

    @BeforeEach
    void setRequest() {
        createChatRequest = CreateChatRequest.builder()
                .otherUserId(ID1)
                .build();

        getChatRequest = GetChatRequest.builder()
                .otherUserId(ID1)
                .build();
    }

    @BeforeEach
    void setResponse() {
        chatResponse = ChatResponse.builder()
                .id(ID)
                .otherUserId(ID1)
                .otherUserFirstname(VALID_FIRSTNAME)
                .otherUserLastname(VALID_LASTNAME)
                .build();

        userResponse = UserResponse.builder()
                .id(ID1)
                .firstname(VALID_FIRSTNAME)
                .lastname(VALID_LASTNAME)
                .role(Role.STUDENT)
                .build();
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
    public void createChatSuccessfullyTest() throws ServiceException {
        when(chatRepository.save(any(Chat.class))).thenAnswer(invocation -> {
            Chat savedChat = invocation.getArgument(0);
            savedChat.setId(1L);
            return savedChat;
        });
        when(chatRepository.findByUserId1AndUserId2(ID,ID1)).thenReturn(Optional.empty());

        Chat chat1 = chatService.createChat(ID, createChatRequest);
        verify(chatRepository).save(chat1);

        assertEquals(chat, chat1);
    }

    @Test
    public void getAllUsersChatSuccessfullyTest() throws ServiceException {
        List<ChatResponse> chats = new ArrayList<>();
        chats.add(chatResponse);
        when(chatRepository.findAllByUserId(ID1)).thenReturn(List.of(chat));
        when(mainServiceClient.getUser(ID)).thenReturn(userResponse);
        List<ChatResponse> chatList = chatService.getAllUserChats(ID1);
        assertEquals(chats, chatList);
    }

    @Test
    public void getChatHistorySuccessfullyTest() throws ServiceException {
        when(chatRepository.findChatById(ID)).thenReturn(Optional.of(chat));
        when(messageRepository.findByChat(chat)).thenReturn(messages);
        Set<Message> messages1 = chatService.getChatHistory(chat.getId());
        assertEquals(1, messages1.size());
    }

    @Test
    public void getChatHistoryEmptyTest() throws ServiceException {
        when(chatRepository.findChatById(ID1)).thenReturn(Optional.of(chat1));
        Set<Message> messages1 = chatService.getChatHistory(chat1.getId());
        assertNull(messages1);
    }

    @Test
    public void sendMessageTest() throws ServiceException {
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message savedMessage = invocation.getArgument(0);
            savedMessage.setId(1L);
            return savedMessage;
        });
        when(chatRepository.findChatById(ID)).thenReturn(Optional.of(chat));
        notificationService.sendNotificationToUser(ID,notification);
        Message message1 = messageService.sendMessage(chat.getId(),ID,CONTENT);
        verify(messageRepository).save(message1);
        assertEquals(message, message1);
    }

    @Test
    public void getChatsSuccessfullyTest() throws ServiceException {
        when(chatRepository.findChatBetweenUsers(ID,ID1)).thenReturn(Optional.of(chat));
        assertEquals(chat, chatService.getChats(ID,ID1));
    }

    @Test
    public void getChatsUnreadSuccessfullyTest() throws ServiceException {
        List<ChatUnread> chatUnreadList = new ArrayList<>();
        chatUnreadList.add(chatUnread);
        when(chatUnreadRepository.findAllByUserId(ID)).thenReturn(chatUnreadList);
        List<ChatUnread> chatUnreadList1 = chatService.getChatsUnread(ID);
        assertEquals(chatUnreadList.size(), chatUnreadList1.size());
    }

    @Test
    public void clearUnreadSuccessfullyTest() throws ServiceException {
        assertDoesNotThrow(()->chatService.clearUnread(ID,ID));
    }

    @Test
    public void getChatSuccessfullyTest() throws ServiceException {
        when(messageRepository.findMessageById(ID)).thenReturn(message);
        Chat chat11 = chatService.getChat(ID);
        assertEquals(chat, chat11);
    }

    @Test
    public void updateMessageTest() throws ServiceException {
        when(messageRepository.findMessageById(ID)).thenReturn(message);
        Message message1 = messageService.updateMessage(ID,CONTENT1);
        verify(messageRepository).save(message1);
        assertEquals(CONTENT1, message1.getContent());
    }

    @Test
    public void deleteMessageTest() throws ServiceException {
        when(messageRepository.findMessageById(ID)).thenReturn(message);
        messageRepository.delete(message);
        assertDoesNotThrow(()->messageService.deleteMessage(ID,CONTENT));
    }
}
