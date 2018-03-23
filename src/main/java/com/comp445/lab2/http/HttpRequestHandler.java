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
        String body = SFHandler.fetchDirectory(method);
        return HttpResponse.getValidResponseWithMessage(body);
    }

    private HttpResponse getFile(HttpRequest r) {
        try {
            String body = SFHandler.fetchFile(r.getUri().substring(1)); // strip path of its initial "/"
            return HttpResponse.getValidResponseWithMessage(body);
        } catch (FileNotFoundException e) { return HttpResponse.getFileNoteFoundResponse(); }
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
