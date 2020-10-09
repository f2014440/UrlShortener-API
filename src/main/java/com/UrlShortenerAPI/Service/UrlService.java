package com.UrlShortenerAPI.Service;

import com.UrlShortenerAPI.Entity.LongUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UrlService {
    private static final AtomicInteger count = new AtomicInteger(0);
    private HashMap<String, String> longUrlMap = new HashMap<String, String>();
    private HashMap<String, Integer> countHits = new HashMap<String, Integer>();
    @Autowired
    BaseConversion conversion;
    private Jedis dataStore = new Jedis();
    public String convertToShortUrl(String clientId, LongUrlRequest request){
        int longUrlHash = request.getLongUrl().hashCode();
        int clientIdHash = clientId.hashCode();
        if (longUrlMap.containsKey(longUrlHash + "-" + clientIdHash)){
            return longUrlMap.get(longUrlHash + "-" + clientIdHash);
        }
        long uId = count.incrementAndGet();
        String key = createKey(clientId, uId);
        String shortUrl = conversion.encode(uId);
        dataStore.lpush(key, request.getLongUrl());
        longUrlMap.put(longUrlHash + "-" + clientIdHash, shortUrl);
        return shortUrl;
    }


    public String getOriginalUrl(String clientId, String shortUrl){
        long id = conversion.decode(shortUrl);
        List<String> longUrls = dataStore.lrange(createKey(clientId, id),0, -1);
        if (longUrls.isEmpty()){
            return null;
        }
        incrementCount(shortUrl);
        return longUrls.get(0);
    }

    private String createKey(String clientId, long uId){
        return clientId + '-' + uId;
    }

    private void incrementCount(String shortUrl){
        if (countHits.containsKey(shortUrl)){
            int count = countHits.get(shortUrl)+1;
            countHits.put(shortUrl, count);
        }
        else{
            countHits.put(shortUrl, 1);
        }
    }

    public Integer getHits(String shortUrl){
        if (countHits.containsKey(shortUrl)){
            return countHits.get(shortUrl);
        }
        return 0;
    }
}
