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
        response.append("\r\n\r\n");
        return response.toString();
    }

    public static HttpResponse getValidResponseWithMessage(String message) {
        return HttpResponse.builder()
                .httpVersion("HTTP/1.0")
                .statusCode(200)
                .reasonPhrase("OK")
                .header("Content-Type", "text/plain")
                .body(message)
                .build();
    }

    public static HttpResponse getInvalidRequestResponse() {
        return HttpResponse.builder()
                .httpVersion("HTTP/1.0")
                .statusCode(400)
                .reasonPhrase("Bad Request")
                .header("Content-Type", "text/plain")
                .body("Invalid request!")
                .build();
    }

    public static HttpResponse getUnauthorisedResponse() {
        return HttpResponse.builder()
                .httpVersion("HTTP/1.0")
                .statusCode(403)
                .reasonPhrase("Forbidden")
                .header("Content-Type", "text/plain")
                .body("You are not allowed to access this directory!")
                .build();
    }

    public static HttpResponse getFileNoteFoundResponse() {
        return HttpResponse.builder()
                .httpVersion("HTTP/1.0")
                .statusCode(404)
                .reasonPhrase("Not Found")
                .header("Content-Type", "text/plain")
                .body("File not found!")
                .build();
    }
}
