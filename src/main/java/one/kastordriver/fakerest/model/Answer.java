package one.kastordriver.fakerest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    private static final String EMPTY_ANSWER_BODY = "";

    private String body = EMPTY_ANSWER_BODY;
    private Integer status = 200;
    private Map<String, String> headers;
    private List<Cookie> cookies;
    private List<String> removeCookies;
}
