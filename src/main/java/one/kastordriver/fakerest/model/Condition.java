package one.kastordriver.fakerest.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {

    private String condition;
    private RouteResponse response;
}
