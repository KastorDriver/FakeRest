package one.kastordriver.fakerest.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Builder
@Getter
@Setter
public class Condition {
    private String condition;
    private Answer answer;

    @Tolerate
    public Condition() {}
}
