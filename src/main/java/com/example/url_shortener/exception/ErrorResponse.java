package com.example.url_shortener.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
    LocalDateTime timestamp,
    int code,
    String message,
    String error,
    String path
) {}
