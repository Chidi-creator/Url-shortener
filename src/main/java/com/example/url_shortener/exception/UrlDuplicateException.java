package com.example.url_shortener.exception;

public class UrlDuplicateException extends RuntimeException {

    public UrlDuplicateException(String message){
        super(message);
    }

}
