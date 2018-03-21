package com.comp445.lab2.file.server;

public enum DirectoryOutputMethod {
    Plain("Plain"),
    JSON("JSON"),
    XML("XML"),
    HTML("HTML");

    private final String method;

    DirectoryOutputMethod(final String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
