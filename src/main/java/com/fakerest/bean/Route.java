package com.fakerest.bean;

import com.fakerest.logic.AnswerLogic;
import spark.Request;
import spark.Response;

public class Route implements spark.Route{
    private String method;
    private String url;
    private Answer defaultAnswer = new Answer();

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

    public Answer getDefaultAnswer() {
        return defaultAnswer;
    }

    public void setDefaultAnswer(Answer defaultAnswer) {
        this.defaultAnswer = defaultAnswer;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return AnswerLogic.handle(getDefaultAnswer(), request, response);
    }
}
