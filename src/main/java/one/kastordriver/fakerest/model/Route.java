package one.kastordriver.fakerest.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    private String method;
    private String url;
    private Answer answer;
    private List<Condition> conditions = new ArrayList<>();
}
