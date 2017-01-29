package com.fakerest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Server.class)
public class ConditionTest {

    private static final String URL = "http://localhost:4567";
    private static String PATH = "/some-path";
    private static final String RESPONSE_TEXT = "response text";

    @Before
    public void before() {
        PATH = PATH + System.currentTimeMillis();
    }

    @Test
    public void correctStatusAndResponseBodyForSimpleGetRequest() throws Exception {
        final int STATUS_CODE = 200;

        String route = "---\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer:\n" +
                "  status: " + STATUS_CODE + "\n" +
                "  body: " + RESPONSE_TEXT + "\n" +
                "conditions:\n" +
                "  -\n" +
                "    condition: ip == \"127.0.0.1\"\n" +
                "    answer:\n" +
                "      status: 200\n" +
                "      body: condition response text\n";

        PowerMockito.stub(PowerMockito.method(Server.class, "loadRoutesFilesIntoString")).toReturn(route);
        Server.main(null);
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        assertEquals(STATUS_CODE, response.getStatusCodeValue());
        assertEquals(RESPONSE_TEXT, response.getBody());
    }
}
