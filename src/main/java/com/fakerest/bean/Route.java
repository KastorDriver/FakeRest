package com.fakerest.bean;

import com.fakerest.logic.AnswerLogic;
import spark.Request;

public class Route implements spark.Route{
    private String method;
    private String url;
    private Answer answer = new Answer();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
