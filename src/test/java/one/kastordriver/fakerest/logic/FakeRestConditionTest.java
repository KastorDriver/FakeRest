package one.kastordriver.fakerest.logic;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class FakeRestConditionTest {

    private static final String URL = "http://localhost:4567";
    private static String PATH = "/some-path";
    private FakeRest fakeRest;

    @Before
    public void before() {
        PATH = PATH + System.currentTimeMillis();
        fakeRest = spy(FakeRest.class);
    }

    @Test
    public void correctAnswerFromIpCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "--- !Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !Answer\n" +
                "  status: 200\n" +
                "  body: response text\n" +
                "conditions:\n" +
                "  - !one.kastordriver.fakerest.bean.Condition\n" +
                "    condition: @ip == \"127.0.0.1\"\n" +
                "    answer:\n" +
                "      status: " + CONDITION_STATUS_CODE + "\n" +
                "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerFromPortCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "--- !Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !Answer\n" +
                "  status: 200\n" +
                "  body: response text\n" +
                "conditions:\n" +
                "  - !one.kastordriver.fakerest.bean.Condition\n" +
                "    condition: @port == 4567\n" +
                "    answer:\n" +
                "      status: " + CONDITION_STATUS_CODE + "\n" +
                "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();
        ResponseEntity<String> response = new RestTemplate().getForEntity(URL + PATH, String.class);
        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerFromContentLengthCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;
        final String REQUEST_BODY = "requestBody";

        String route = "--- !Route\n" +
                "method: post\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !Answer\n" +
                "  status: 200\n" +
                "  body: response text\n" +
                "conditions:\n" +
                "  - !one.kastordriver.fakerest.bean.Condition\n" +
                "    condition: @contentLength == " + REQUEST_BODY.length() + "\n" +
                "    answer:\n" +
                "      status: " + CONDITION_STATUS_CODE + "\n" +
                "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();
        ResponseEntity<String> response = new RestTemplate().postForEntity(URL + PATH, REQUEST_BODY, String.class);
        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerFromCookieCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "--- !Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !Answer\n" +
                "  status: 200\n" +
                "  body: response text\n" +
                "conditions:\n" +
                "  - !one.kastordriver.fakerest.bean.Condition\n" +
                "    condition: @cookie(someCookie) == \"cookieValue\" && @cookie(someCookie2) == \"cookieValue2\"\n" +
                "    answer:\n" +
                "      status: " + CONDITION_STATUS_CODE + "\n" +
                "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "someCookie=cookieValue");
        headers.add("Cookie", "someCookie2=cookieValue2");
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.GET, httpEntity, String.class);

        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }

    @Test
    public void correctAnswerFromHeaderCondition() throws Exception {
        final String CONDITION_RESPONSE_TEXT = "condition response text";
        final int CONDITION_STATUS_CODE = 201;

        String route = "--- !Route\n" +
                "method: get\n" +
                "url: " + PATH + "\n" +
                "defaultAnswer: !Answer\n" +
                "  status: 200\n" +
                "  body: response text\n" +
                "conditions:\n" +
                "  - !one.kastordriver.fakerest.bean.Condition\n" +
                "    condition: @header(Accept) == \"application/json\"\n" +
                "    answer:\n" +
                "      status: " + CONDITION_STATUS_CODE + "\n" +
                "      body: " + CONDITION_RESPONSE_TEXT + "\n";

        doReturn(route).when(fakeRest).loadRoutesFilesIntoString(anyString());
        fakeRest.start();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = new RestTemplate().exchange(URL + PATH, HttpMethod.GET, httpEntity, String.class);

        assertEquals(CONDITION_STATUS_CODE, response.getStatusCodeValue());
        assertEquals(CONDITION_RESPONSE_TEXT, response.getBody());
    }
}
