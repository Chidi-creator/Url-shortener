package com.example.url_shortener.Url;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.url_shortener.SuccessResponse;

@RestController
@RequestMapping("api/v1/url")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<SuccessResponse<String>> createUrl(@RequestBody Url url) {

        Url newUrl = urlService.shorten(url);

        SuccessResponse<String> response = new SuccessResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Code created successfully",
                UrlService.DOMAIN_URL + newUrl.getShortUrl()
            );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
