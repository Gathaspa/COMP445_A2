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
}
