package com.UrlShortenerAPI.Service;

import com.UrlShortenerAPI.Entity.LongUrlRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {

    @Mock
    BaseConversion mockBaseConversion;
    @Mock
    Jedis jedis;
    @InjectMocks
    UrlService urlService;

    @Test
    public void convertToShortUrlTest() {
        long val = 1;
        when(jedis.lpush("123-1","https://github.com/f2014440/UrlShortener-API")).thenReturn(val);
        when(mockBaseConversion.encode(1)).thenReturn("f");

        LongUrlRequest urlRequest = new LongUrlRequest();
        urlRequest.setLongUrl("https://github.com/f2014440/UrlShortener-API");

        assertEquals("f", urlService.convertToShortUrl("123", urlRequest));
    }

    @Test
    public void getOriginalUrlTest() {
        when(mockBaseConversion.decode("h")).thenReturn((long) 7);

        List<String> val = new ArrayList<>();
        val.add("https://github.com/f2014440/UrlShortener-API");
        when(jedis.lrange("123-7",0, -1)).thenReturn(val);

        assertEquals("https://github.com/f2014440/UrlShortener-API", urlService.getOriginalUrl("123", "h"));

    }
}
