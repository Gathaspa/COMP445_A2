package com.comp445.lab2.http;

import com.comp445.lab2.http.exceptions.HttpFormatException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class HttpRequestParser {
    private HttpRequest.HttpRequestBuilder builder;
    private StringBuffer messageBuffer;

    public HttpRequest parse(String request) throws HttpFormatException, IOException{
        builder = new HttpRequest.HttpRequestBuilder();
        messageBuffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new StringReader(request));

        setRequestLine(reader.readLine());

        String header = reader.readLine();
        while (header.length() > 0) {
            appendHeaderParameter(header);
            header = reader.readLine();
        }

        String bodyLine = reader.readLine();
        while (bodyLine != null) {
            appendMessageBody(bodyLine);
            bodyLine = reader.readLine();
        }

        builder.body(messageBuffer.toString());

        return builder.build();
    }

    private void setRequestLine(String requestLine) throws HttpFormatException {
        if (requestLine == null || requestLine.length() == 0) {
            throw new HttpFormatException("Invalid Request-Line: " + requestLine);
        }
        String[] splited = requestLine.split("\\s+");
        if(splited.length != 3){
            throw new HttpFormatException("Invalid Request-Line: " + requestLine);
        }
        builder.method(getMethod(splited[0]));
        builder.uri(splited[1]);
        builder.httpVersion(splited[2]);
    }

    private HttpMethod getMethod(String method) throws HttpFormatException{
        if(method.equals("GET"))
            return HttpMethod.GET;
        else if(method.equals("POST"))
            return HttpMethod.POST;
        else
            throw new HttpFormatException("Invalid HTTP method: " + method);
    }

    private void appendHeaderParameter(String header) throws HttpFormatException {
        int idx = header.indexOf(":");
        if (idx == -1) {
            throw new HttpFormatException("Invalid Header Parameter: " + header);
        }
        builder.header(header.substring(0, idx), header.substring(idx + 1, header.length()).trim());
    }

    private void appendMessageBody(String bodyLine) {
        if(messageBuffer.length() != 0)
            messageBuffer.append("\r\n");
        messageBuffer.append(bodyLine);
    }
}
