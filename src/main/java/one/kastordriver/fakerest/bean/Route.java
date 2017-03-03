package one.kastordriver.fakerest.bean;

import lombok.Getter;
import lombok.Setter;
import one.kastordriver.fakerest.logic.AnswerLogic;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Route implements spark.Route {
    private String method;
    private String url;
    private Answer defaultAnswer = new Answer();
    private List<Condition> conditions = new ArrayList<>();

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return AnswerLogic.handle(this, request, response);
    }
}
