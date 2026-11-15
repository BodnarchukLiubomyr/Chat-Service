package com.example.chat_service.payload.request;

import com.example.chat_service.payload.response.UserResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private Long chatId;
    private UserResponse user;
    @NotBlank
    private String content;
}
