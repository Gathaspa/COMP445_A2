package com.comp445.lab2.http;

import com.comp445.lab2.file.server.FileServerHandler;
import com.comp445.lab2.file.server.DirectoryOutputMethod;

import java.io.FileNotFoundException;

public class HttpRequestHandler {
    private static FileServerHandler SFHandler = new FileServerHandler();

    public HttpResponse handleRequest(HttpRequest r)  {
        DirectoryOutputMethod method;
        String body;
        if (r.getUri().equals("/")) {
            switch (r.getHeaders().get("Accept")) {
                case "text/plain":
                    method = DirectoryOutputMethod.Plain;
                    break;
                case "JSON":
                    method = DirectoryOutputMethod.JSON;
                    break;
                case "XML":
                    method = DirectoryOutputMethod.XML;
                    break;
                default:
                    method = DirectoryOutputMethod.HTML;
            }
            body = SFHandler.fetchDirectory(method);
        } else {
            try {
                body = SFHandler.fetchFile(r.getUri().substring(1)); // strip path of its initial "/"
            } catch (FileNotFoundException e){
                return HttpResponse.builder()
                        .httpVersion("HTTP/1.0")
                        .statusCode(400)
                        .reasonPhrase("Bad Request")
                        .header("Content-Type", "text/plain")
                        .body(String.format("File %s not found", r.getUri()))
                        .build();
            }
        }
        return HttpResponse.builder()
                .httpVersion("HTTP/1.0")
                .statusCode(200)
                .reasonPhrase("OK")
                .header("Content-Type", "text/plain")
                .body(body)
                .build();
    }
}
