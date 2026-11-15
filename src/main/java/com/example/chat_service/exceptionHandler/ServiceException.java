package com.example.chat_service.exceptionHandler;

import com.example.chat_service.exceptionHandler.model.ErrorType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends Exception{
    private static final long serialVersionUID = 1L;

    private ErrorType errorType = ErrorType.FATAL_ERROR;

    ServiceException(String message){
        super(message);
    }
}
