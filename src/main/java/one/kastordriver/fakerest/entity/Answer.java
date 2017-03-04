package one.kastordriver.fakerest.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Answer {
    private static final String EMPTY_ANSWER_BODY = "";

    private String body = EMPTY_ANSWER_BODY;
    private Integer status = 200;
    private Map<String, String> headers = new HashMap<>();
    private List<Cookie> cookies = new ArrayList<>();
    private List<String> removeCookies = new ArrayList<>();
}
