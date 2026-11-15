package com.example.chat_service.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageResponse {
    private Long id;

    private String firstname;

    private String lastname;
}
