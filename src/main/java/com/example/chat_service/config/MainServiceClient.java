package com.example.chat_service.config;

import com.example.chat_service.payload.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "main-service",
        url = "${services.main-service.url}"
)
public interface MainServiceClient {
    @GetMapping("/api/v1/school-application/get-user/{userId}")
    UserResponse getUser(@PathVariable("userId") Long userId);
}
