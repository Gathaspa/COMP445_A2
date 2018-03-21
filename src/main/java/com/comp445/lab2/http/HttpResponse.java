package com.comp445.lab2.http;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Map;

@Data
@Builder
public class HttpResponse {
    private String httpVersion;
    private int statusCode;
    private String reasonPhrase;
    @Singular
    private Map<String, String> headers;
    private String body;

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder(httpVersion + " " + statusCode + " " + reasonPhrase + "\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        response.append("\r\n");
        response.append(body);
        return response.toString();
    }
}