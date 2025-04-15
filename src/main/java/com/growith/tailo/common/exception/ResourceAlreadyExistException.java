package com.growith.tailo.common.exception;

public class ResourceAlreadyExistException extends RuntimeException {
    private String message;
    public ResourceAlreadyExistException(String message){
        super(message);
    }
}
