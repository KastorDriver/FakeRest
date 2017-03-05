package one.kastordriver.fakerest.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Route {
    private String method;
    private String url;
    private Answer defaultAnswer = new Answer();
    private List<Condition> conditions = new ArrayList<>();
}
