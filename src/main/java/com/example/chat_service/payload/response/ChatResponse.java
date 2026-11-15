package com.example.chat_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private Long id;

    private Long otherUserId;

    private String otherUserFirstname;

    private String otherUserLastname;
}
