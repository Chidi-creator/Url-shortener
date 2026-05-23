package com.example.url_shortener.exception;

public class DuplicateException extends RuntimeException {

    public DuplicateException(String message){
        super(message);
    }

}
