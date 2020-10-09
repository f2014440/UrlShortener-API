package com.UrlShortenerAPI.Controller;

import com.UrlShortenerAPI.Entity.LongUrlRequest;
import com.UrlShortenerAPI.Service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
@RestController
@RequestMapping("/api/v1")
public class UrlShortenerController {

    @Autowired
    UrlService urlService;
    @PostMapping("create-short/{clientId}")
    public String convertToShortUrl(@RequestBody LongUrlRequest request, @PathVariable("clientId") String clientId) {
        return urlService.convertToShortUrl(clientId, request);
    }


    @GetMapping(value = "{shortUrl}/{clientId}")
    public String getAndRedirect(@PathVariable("shortUrl") String shortUrl, @PathVariable("clientId") String clientId) {
        String url = urlService.getOriginalUrl(clientId, shortUrl);
        if (null == url){
            ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return url;
    }

    @GetMapping(value = "getHits/{shortUrl}")
    public Integer getHitsforUrl(@PathVariable("shortUrl") String shortUrl){
        return urlService.getHits(shortUrl);
    }
}
