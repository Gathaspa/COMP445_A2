package com.comp445.lab2.http;

public enum HttpMethod {
    GET("GET"),
    POST("POST")
    ;

    private final String method;

    HttpMethod(final String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}