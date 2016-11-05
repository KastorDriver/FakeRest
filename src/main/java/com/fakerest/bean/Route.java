package com.fakerest.bean;

import spark.Request;

public class Route implements spark.Route{
    private String type;
    private String path;
    private Response response = new Response();

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

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public Object handle(Request request, spark.Response response) throws Exception {
        return this.response.getBody();
    }
}
