package one.kastordriver.fakerest.bean;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Data
@Builder
public class Condition {
    private String condition;
    private Answer answer;

    @Tolerate
    public Condition() {}
}
