package com.example.chat_service.exceptionHandler.model;

import com.example.chat_service.exceptionHandler.MessageNotSentException;
import com.example.chat_service.exceptionHandler.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException: message {}", ex.getMessage(), ex);
        List<Error> errorList = ex.getBindingResult().getAllErrors().stream()
                .map(err -> new Error(err.getDefaultMessage(), ErrorType.VALIDATION_ERROR, LocalDateTime.now()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Error> handleServiceException(ServiceException ex, HandlerMethod hm) {
        log.error("handleServiceException: message {}, method {}", ex.getMessage(), hm.getMethod().getName(), ex);
        Error error = new Error(ex.getMessage(), ex.getErrorType(), LocalDateTime.now());
        return ResponseEntity.internalServerError().body(error);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception ex, HandlerMethod hm) {
        log.error("handleException: message {}, method {}", ex.getMessage(), hm.getMethod().getName(), ex);
        Error error = new Error(ex.getMessage(), ErrorType.FATAL_ERROR, LocalDateTime.now());
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(MessageNotSentException.class)
    public ResponseEntity<Error> handleMessageNotSentException(MessageNotSentException ex, HandlerMethod hm) {
        log.error("MessageNotSentException: message: {}, method: {}", ex.getStackTrace(), hm.getMethod().getName());
        Error error = new Error(ex.getMessage(), ErrorType.MESSAGING_ERROR, LocalDateTime.now());
        return ResponseEntity.internalServerError().body(error);
    }
}
