package com.comp445.lab2.http;

import com.comp445.lab2.file.server.FileServerHandler;
import com.comp445.lab2.file.server.DirectoryOutputMethod;

import java.io.FileNotFoundException;

public class HttpRequestHandler {
    private static FileServerHandler SFHandler = new FileServerHandler();

    public HttpResponse handleRequest(HttpRequest r) {
        if (r.getUri().contains("../")) return HttpResponse.getUnauthorisedResponse();
        if (r.getUri().equals("/") && r.getMethod() == HttpMethod.GET) return getRoot(r);
        else if (r.getMethod() == HttpMethod.GET) return getFile(r);
        else if (r.getMethod() == HttpMethod.POST) return postFile(r);
        else return HttpResponse.getInvalidRequestResponse();
    }

    private HttpResponse getRoot(HttpRequest r) {
        DirectoryOutputMethod method;
        switch (r.getHeaders().getOrDefault("Accept", "text/plain")) {
            case "application/json":
                method = DirectoryOutputMethod.JSON;
                break;
            case "application/xml":
                method = DirectoryOutputMethod.XML;
                break;
            case "text/html":
                method = DirectoryOutputMethod.HTML;
                break;
            default:
                method = DirectoryOutputMethod.Plain;
        }
        // method = DirectoryOutputMethod.JSON; // FOR THE LAB
        String body = SFHandler.fetchDirectory(method);
        return HttpResponse.builder()
                .httpVersion("HTTP/1.0")
                .statusCode(200)
                .reasonPhrase("OK")
                .header("Content-Type", method.toString())
                .body(body)
                .build();
    }

    private HttpResponse getFile(HttpRequest req) {
        try {
            String path = req.getUri().substring(1);
            String body = SFHandler.fetchFile(path); // strip path of its initial "/"
            HttpResponse.HttpResponseBuilder builder = HttpResponse.builder()
                    .httpVersion("HTTP/1.0")
                    .statusCode(200)
                    .reasonPhrase("OK")
                    .header("Content-Type", "text/plain");
            if (req.getHeaders().getOrDefault("Content-Disposition", "inline").equals("attachment")) {
                builder.header("Content-Disposition", String.format("attachment; filename=%s", path));
            } else {
                builder.header("Content-Disposition", "inline");
            }
            builder.body(body);
            builder.header("Content-Length", String.valueOf(body.length()));
            return builder.build();
        } catch (FileNotFoundException e) {
            return HttpResponse.getFileNoteFoundResponse();
        }
    }

    private HttpResponse postFile(HttpRequest r) {
        try {

            String path = r.getUri().substring(1); // strip path of its initial "/"
            SFHandler.writeFile(path, r.getBody());
            return HttpResponse.getValidResponseWithMessage("File " + path + " successfully written.");
        } catch (Exception e) {
            return HttpResponse.getInvalidRequestResponse();
        }
    }

}
