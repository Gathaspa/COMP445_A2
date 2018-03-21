package com.comp445.lab2.file.server;

public enum directoryOutputMethod {
    Plain("Plain"),
    JSON("JSON"),
    XML("XML"),
    HTML("HTML");

    private final String method;

    directoryOutputMethod(final String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
