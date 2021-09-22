package one.kastordriver.fakerest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {

    private static final String EMPTY_RESPONSE_BODY = "";

    private String body = EMPTY_RESPONSE_BODY;
    private Integer status = 200;
    private Map<String, String> headers = new HashMap<>();
    private List<Cookie> cookies = new ArrayList<>();
    private List<String> removeCookies = new ArrayList<>();
}
