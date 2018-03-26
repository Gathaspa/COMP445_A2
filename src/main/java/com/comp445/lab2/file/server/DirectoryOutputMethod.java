package com.comp445.lab2.file.server;

public enum DirectoryOutputMethod {
    Plain("text/plain"),
    JSON("application/json"),
    XML("application/xml"),
    HTML("text/html");

    private final String method;

    DirectoryOutputMethod(final String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
