package one.kastordriver.fakerest.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Builder
@Data
public class Cookie {

    private String path;
    private String name;
    private String value;
    private int maxAge = -1;
    private boolean secure;
}
