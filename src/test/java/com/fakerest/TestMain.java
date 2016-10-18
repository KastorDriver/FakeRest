package com.fakerest;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

/**
 * Created by Kastor on 28.09.2016.
 */
public class TestMain {

    @Test
    public void testHello() {
        Main.start();
        String result = new RestTemplate().getForObject("http://localhost:4567/hello", String.class);
        System.out.println(result);
        assertEquals("hello", result);
    }
}
