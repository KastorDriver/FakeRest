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

    private static final String LOAD_ROUTES_FUNC_NAME = "loadRoutesFilesIntoString";
    private static final String URL = "http://localhost:4567";
    private static String PATH = "/some-path";

    @Before
    public void before() {
        PATH = PATH + System.currentTimeMillis();
    }

    @Test
    public void correctStatusAndResponseBodyFromCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "--- !com.fakerest.bean.Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !com.fakerest.bean.Answer\n" +
                "  status: 200\n" +
                "  body: response text\n" +
                "conditions:\n" +
                "  - !com.fakerest.bean.Condition\n" +
                "    condition: ip == \"127.0.0.1\"\n" +
                "    answer:\n" +
                "      status: " + CONDITION_STATUS_CODE + "\n" +
                "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        PowerMockito.stub(PowerMockito.method(Server.class, LOAD_ROUTES_FUNC_NAME)).toReturn(route);
        Server.main(null);
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }
}
