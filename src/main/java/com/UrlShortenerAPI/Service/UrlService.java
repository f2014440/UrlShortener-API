package com.UrlShortenerAPI.Service;

import com.UrlShortenerAPI.Entity.LongUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UrlService {
    private static final AtomicInteger count = new AtomicInteger(0);
    @Autowired
    BaseConversion conversion;
    private Jedis dataStore = new Jedis();
    public String convertToShortUrl(String clientId, LongUrlRequest request){
        long uId = count.incrementAndGet();
        String key = createKey(clientId, uId);
        String shortUrl = conversion.encode(uId);
        dataStore.lpush(key, request.getLongUrl());
        return shortUrl;
    }


    public String getOriginalUrl(String clientId, String shortUrl){
        long id = conversion.decode(shortUrl);
        List<String> longUrls = dataStore.lrange(createKey(clientId, id),0, -1);
        if (longUrls.isEmpty()){
            return null;
        }
        return longUrls.get(0);
    }

    private String createKey(String clientId, long uId){
        return clientId + '-' + uId;
    }
}
