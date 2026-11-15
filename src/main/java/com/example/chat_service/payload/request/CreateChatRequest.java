package com.example.chat_service.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateChatRequest {
    private Long otherUserId;
}
