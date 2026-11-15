package com.example.chat_service.exceptionHandler;

import com.example.chat_service.exceptionHandler.model.ErrorType;

public class DataProcessingException extends ServiceException{
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_MESSAGE = "Something happened during process of execution!";

    public DataProcessingException() {
        this(DEFAULT_MESSAGE);
    }

    public DataProcessingException(String message) {
        super(message);
        setErrorType(ErrorType.PROCESSING_ERROR);
    }
}
