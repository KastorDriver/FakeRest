package com.fakerest.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Answer {
    private String body = "";
    private Integer status;
    private Map<String, String> headers;
    private List<Cookie> cookies;
    private List<String> removeCookies;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public List<String> getRemoveCookies() {
        return removeCookies;
    }

    public void setRemoveCookies(ArrayList<String> removeCookies) {
        this.removeCookies = removeCookies;
    }
}
