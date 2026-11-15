package com.example.chat_service.util;

import com.example.chat_service.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate template;
    public void sendNotificationToClass(Long classId, Notification notification){
        template.convertAndSend("/topic/class/" + classId, notification);
    }

    public void sendNotificationToUser(Long userId, Notification notification){
        template.convertAndSend("/topic/user/" + userId, notification);
    }

    public void sendNotificationToGroup(Long groupId, Notification notification){
        template.convertAndSend("/topic/group/" + groupId, notification);
    }
}
