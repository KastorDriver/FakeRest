package one.kastordriver.fakerest.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Route {
    private String method;
    private String url;
    private Answer answer = new Answer();
    private List<Condition> conditions = new ArrayList<>();
}
