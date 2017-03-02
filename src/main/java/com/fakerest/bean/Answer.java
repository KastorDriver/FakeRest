package com.fakerest.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Answer {
    private String body = "";
    private Integer status;
    private Map<String, String> headers;
    private List<Cookie> cookies;
    private List<String> removeCookies;
}
