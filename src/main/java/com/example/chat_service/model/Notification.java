package com.example.chat_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification {
    private String title;
    private String content;
    private LocalDateTime timestamp;
    private Long chatId;
    private Long senderId;
}
