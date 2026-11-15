package com.example.chat_service.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetChatRequest {
    private Long otherUserId;
}
