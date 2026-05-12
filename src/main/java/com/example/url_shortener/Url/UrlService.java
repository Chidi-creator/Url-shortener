package com.example.url_shortener.Url;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public void shorten(Url url) {

        boolean valid = this.validateUrl(url.getLongUrl());

        if (!valid) {
            throw new IllegalStateException("Url isn't valid, please submit valid url");
        }

        String unique = this.generateUniqueString();

        url.setShortUrl(unique);
        url.setExpiresAt(url.getCreatedAt().plusDays(7));

    }

    private boolean validateUrl(String url) {
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                .method("HEAD", HttpRequest.BodyPublishers.noBody()).build(); // HEAD is faster then GET

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() > 200 && response.statusCode() < 400;
        } catch (Exception e) {
            // Log the error and return false because the "ping" failed
            System.err.println("Error connecting to URL: " + e.getMessage());
            return false;
        }

    }

    private String generateUniqueString() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
