package one.kastordriver.fakerest.bean;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Builder
@Data
public class Cookie {
    private String path;
    private String name;
    private String value;
    private int maxAge = -1;
    private boolean secure;

    @Tolerate
    public Cookie() {}
}
