package one.kastordriver.fakerest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cookie {

    private String domain;
    private String path;
    private String name;
    private String value;
    private int maxAge = -1;
    private boolean secure;
    private boolean httpOnly;
}
