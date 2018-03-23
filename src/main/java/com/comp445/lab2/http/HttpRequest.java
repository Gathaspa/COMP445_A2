package com.comp445.lab2.http;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Map;

@Data
@Builder
public class HttpRequest {
    private HttpMethod method;
    private String uri;
    private String httpVersion;
    private int port;
    @Singular
    private Map<String, String> headers;
    private String queries;
    private String body;

    @Override
    public String toString() {

        // TODO: doesn't handle queries and port properly atm
        StringBuilder response = new StringBuilder(method + " " + uri + " " + httpVersion + "\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        response.append("\r\n");
        response.append(body);
        response.append("\r\n\r\n");
        return response.toString();
    }
}
