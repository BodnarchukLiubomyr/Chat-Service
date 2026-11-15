package com.example.chat_service.exceptionHandler;

import com.example.chat_service.exceptionHandler.model.ErrorType;

public class MessageNotSentException extends ServiceException {
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MESSAGE = "Message didn't sent";

    public MessageNotSentException() {
        this(DEFAULT_MESSAGE);
    }

    public MessageNotSentException(String message){
        super(message);
        setErrorType(ErrorType.MESSAGING_ERROR);
    }
}
