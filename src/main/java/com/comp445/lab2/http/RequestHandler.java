package com.comp445.lab2.http;

import com.comp445.lab2.file.server.FileServerHandler;
import com.comp445.lab2.file.server.directoryOutputMethod;

import java.io.UnsupportedEncodingException;

public class RequestHandler {
    private static FileServerHandler SFHandler = new FileServerHandler();

    public byte[] handleRequest(HttpRequest r) throws Exception {
        directoryOutputMethod method;
        String body;
        if (r.getUri().equals("/")) {
            switch (r.getHeaders().get("Accept")) {
                case "text/plain":
                    method = directoryOutputMethod.Plain;
                    break;
                case "JSON":
                    method = directoryOutputMethod.JSON;
                    break;
                case "XML":
                    method = directoryOutputMethod.XML;
                    break;
                default:
                    method = directoryOutputMethod.HTML;
            }
            body = SFHandler.fetchDirectory(method);
        } else {
            body = SFHandler.fetchFile(r.getUri().substring(1)); // strip path of its initial "/"
        }
        return HttpResponse.builder()
                .httpVersion("HTTP/1.0")
                .statusCode(200)
                .reasonPhrase("OK")
                .header("Content-Type", "text/plain")
                .body(body)
                .build().toString().getBytes("UTF-8");
    }

    public byte[] getErrorResponse() throws UnsupportedEncodingException {
        return HttpResponse.builder()
                .httpVersion("HTTP/1.0")
                .statusCode(400)
                .reasonPhrase("Bad Request")
                .header("Content-Type", "text/plain")
                .body("This is an error body. Your request was invalid!")
                .build().toString().getBytes("UTF-8");
    }
}
