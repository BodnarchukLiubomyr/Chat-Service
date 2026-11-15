package com.example.chat_service.api;

import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "School Application", description = "Functionality management API")
@RequestMapping("/api/v1/chat-service")
public interface MessageApi {
    @Operation(summary = "Update message", tags = {"Message"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Message.class),
            mediaType = "application/json")})})
    @PostMapping(value = "/update-message/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Message> updateMessage(@PathVariable("messageId") Long messageId,
                                          @RequestBody String content) throws ServiceException;

    @Operation(summary = "Delete message", tags = {"Message"})
    @DeleteMapping("/delete-message/{messageId}")
    ResponseEntity<Void> deleteMessage(@PathVariable("messageId") Long messageId, String content) throws ServiceException;
}
