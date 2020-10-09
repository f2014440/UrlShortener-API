package com.UrlShortenerAPI.Service;

import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
@RunWith(MockitoJUnitRunner.class)
public class BaseConversionTest {

    private BaseConversion baseConversion = new BaseConversion();

    @Test
    public void encode_lessThan62() {
        assertEquals("k", baseConversion.encode(10));
    }

    @Test
    public void encode_moreThan62() {
        assertEquals("bq", baseConversion.encode(78));
    }

    @Test
    public void decode_singleCharacter() {
        assertEquals(11, baseConversion.decode("l"));
    }
}

