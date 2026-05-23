package com.example.url_shortener.Url;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.InvalidUrlException;

import com.example.url_shortener.cache.CacheService;
import com.example.url_shortener.exception.NotFoundException;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    public static final String DOMAIN_URL = "http://localhost:8080/api/v1/";
    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);
    private final CacheService cacheService;

    public UrlService(UrlRepository urlRepository, CacheService cacheService) {
        this.urlRepository = urlRepository;
        this.cacheService = cacheService;
    }

    public Url shorten(Url url) {

        logger.info("Sanitizing " + url.getLongUrl());
        String sanitizedUrl = this.sanitizeUrl(url.getLongUrl());
        logger.info("The sanitized url is " + sanitizedUrl);

        //retriever from cache 
        

        Optional<Url> existingUrl = urlRepository.findByLongUrl(sanitizedUrl);

        if (existingUrl.isPresent()) {
            logger.info("Retreiving existing code for " + existingUrl.get().getLongUrl());
            // cache this after retreival
            logger.info("Caching retrieved url");
            cacheService.set(existingUrl.get().getShortUrl(), existingUrl.get().getLongUrl(), Duration.ofSeconds(3600));
            return existingUrl.get();

        }

        String unique = this.generateUniqueString();

        url.setShortUrl(unique);
        url.setCreatedAt(LocalDateTime.now());
        url.setExpiresAt(url.getCreatedAt().plusDays(7));
        url.setLongUrl(sanitizedUrl);

        Url savedUrl = urlRepository.save(url);

        // 2. CACHE TO REDIS SECOND (Guarantees DB synchronization is finished)
        logger.info("Caching the finalized url code: " + unique);
        cacheService.set(unique, sanitizedUrl, Duration.ofSeconds(3600));

        return savedUrl;

    }

    private String sanitizeUrl(String url) {

        url = url.trim();

        if (url.startsWith("www")) {
            url = url.substring(4);
        }

        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "https://" + url;
        }

        URI uri = URI.create(url);

        String scheme = uri.getScheme();
        String host = uri.getHost();

        if (!scheme.equals("http") && !scheme.equals("https")) {
            throw new InvalidUrlException("Only sites with http are allowed ");
        }

        if (host == null || host.isBlank()) {
            throw new InvalidUrlException("Invalid Domain Name");
        }

        return url;
    }

    private String generateUniqueString() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public String getOriginalUrl(String code) {
        boolean exists = cacheService.exists(code);
        if (exists) {
            logger.info("Retrieving cached url");
            return cacheService.get(code);
        }

        // fallback to db for code
        Optional<Url> existingUrl = urlRepository.findByShortUrl(code);

        if (!existingUrl.isPresent()) {
            throw new NotFoundException("Couldn't get code associated to url");
        }

        return existingUrl.get().getLongUrl();

    }

}
