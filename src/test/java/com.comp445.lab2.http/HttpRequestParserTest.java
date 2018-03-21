package com.comp445.lab2.http;

import com.comp445.lab2.http.exceptions.HttpFormatException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HttpRequestParserTest {
    @Test
    public void testParseRequest_withValidRequestString_shouldReturnValidRequest() throws Exception{
        final String requestString = "POST /cgi-bin/process.cgi HTTP/1.1\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
                "Host: www.tutorialspoint.com\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: length\n" +
                "Accept-Language: en-us\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: Keep-Alive\n" +
                "\n" +
                "licenseID=string&content=string&/paramsXML=string";
        HttpRequest expectedRequest = HttpRequest.builder()
                .method(HttpMethod.POST)
                .uri("/cgi-bin/process.cgi")
                .httpVersion("HTTP/1.1")
                .header("User-Agent", "Mozilla/4.0 (compatible; MSIE5.01; Windows NT)")
                .header("Host", "www.tutorialspoint.com")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Content-Length", "length")
                .header("Accept-Language",  "en-us")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Connection", "Keep-Alive")
                .body("licenseID=string&content=string&/paramsXML=string").build();

        assertEquals(expectedRequest, new HttpRequestParser().parse(requestString));
    }

    @Test(expected = HttpFormatException.class)
    public void testParseRequest_withInvalidMethod_shouldThrow() throws Exception{
        final String requestString = "FUCK /cgi-bin/process.cgi HTTP/1.1\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
                "Host: www.tutorialspoint.com\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: length\n" +
                "Accept-Language: en-us\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: Keep-Alive\n" +
                "\n" +
                "licenseID=string&content=string&/paramsXML=string";
        new HttpRequestParser().parse(requestString);
    }

    @Test(expected = HttpFormatException.class)
    public void testParseRequest_withInvalidRequestLine_shouldThrow() throws Exception{
        final String requestString = "POST   HTTP/1.1\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\n" +
                "Host: www.tutorialspoint.com\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Content-Length: length\n" +
                "Accept-Language: en-us\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: Keep-Alive\n" +
                "\n" +
                "licenseID=string&content=string&/paramsXML=string";
        new HttpRequestParser().parse(requestString);
    }
}
