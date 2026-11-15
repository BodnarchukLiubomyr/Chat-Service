package com.example.chat_service.api;

import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Chat;
import com.example.chat_service.model.ChatUnread;
import com.example.chat_service.model.Message;
import com.example.chat_service.payload.request.CreateChatRequest;
import com.example.chat_service.payload.request.GetChatRequest;
import com.example.chat_service.payload.response.ChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Set;

@Tag(name = "School Application", description = "Functionality management API")
@RequestMapping("/api/v1/chat-service")
public interface ChatApi {
    @Operation(summary = "Create chat between two users", tags = {"Chat"})
    @PostMapping("/create-chat/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Chat> createChat(@AuthenticationPrincipal Jwt jwt,
                                    @Valid @RequestBody CreateChatRequest request) throws ServiceException;

    @Operation(summary = "Get chats", tags = {"Chat"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Chat.class),
            mediaType = "application/json")})})
    @GetMapping(value = "/get-chats/{userId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<ChatResponse>> getUsersChat(@PathVariable("userId") Long userId) throws ServiceException;

    @Operation(summary = "Get chats", tags = {"Chat"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Chat.class),
            mediaType = "application/json")})})
    @GetMapping(value = "/get-chatsByName/{userId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Chat> getChatsByName(@PathVariable("userId") Long userId,
                                        @Valid @RequestBody GetChatRequest request) throws ServiceException;

    @Operation(summary = "Get chat history", tags = {"Message"})
    @GetMapping("/chat/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Set<Message>> getChatHistory(@PathVariable("chatId") Long chatId) throws ServiceException;

    @Operation(summary = "Get chat", tags = {"Message"})
    @GetMapping("/get-chat/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Chat> getChat(@PathVariable("messageId") Long messageId) throws ServiceException;

    @GetMapping("/chatUnread/{userId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<ChatUnread>> getUnreadChats(@PathVariable("userId") Long userId) throws ServiceException;

    @DeleteMapping(value = "/unread-message/{chatId}/unread")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> clearUnread(@PathVariable("chatId") Long chatId, @RequestParam("userId") Long userId);
}
