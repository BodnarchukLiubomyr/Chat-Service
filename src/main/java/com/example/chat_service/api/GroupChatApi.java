package com.example.chat_service.api;

import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.GroupMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@Tag(name = "School Application", description = "Functionality management API")
@RequestMapping("/api/v1/chat-service")
public interface GroupChatApi {
    @Operation(summary = "Get group chat history", tags = {"Group Message"})
    @GetMapping("/group-history/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Set<GroupMessage>> getGroupChatHistory(@PathVariable("groupId") Long groupId) throws ServiceException;
}
