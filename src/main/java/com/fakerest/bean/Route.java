package com.fakerest.bean;

import com.fakerest.logic.AnswerLogic;
import spark.Request;

public class Route implements spark.Route{
    private String type;
    private String path;
    private Answer answer = new Answer();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Override
    public Object handle(Request request, spark.Response response) throws Exception {
        return AnswerLogic.handle(this, request, response);
    }
}
