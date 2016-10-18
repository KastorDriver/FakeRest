package com.fakerest;

import spark.Spark;

public class Main {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        Spark.get("/hello", (req, res) -> "hello");
    }
}
