package com.example.url_shortener.User;

public record AuthResponse(
        User user,
        String accessToken
) {
}
