package one.kastordriver.fakerest.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Answer {
    private static final String EMPTY_ANSWER_BODY = "";

    private String body = EMPTY_ANSWER_BODY;
    private Integer status;
    private Map<String, String> headers;
    private List<Cookie> cookies;
    private List<String> removeCookies;
}
