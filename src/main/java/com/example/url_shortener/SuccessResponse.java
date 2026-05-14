package com.example.url_shortener;

import java.time.LocalDateTime;

public record SuccessResponse<T>(
    LocalDateTime timestamp,
    int status,
    String message,
    T data
) {}
