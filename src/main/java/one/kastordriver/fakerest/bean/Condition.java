package one.kastordriver.fakerest.bean;

import lombok.*;
import lombok.experimental.Tolerate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {

    private String condition;
    private Answer answer;
}
