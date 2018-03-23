package com.comp445.lab2.http;

import com.comp445.lab2.ThreadedClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentClientsTest {
    private static final Logger logger = LogManager.getLogger(ConcurrentClientsTest.class);
    @Test
    public void testWith10ClientsWritingToFile() throws InterruptedException {

        int numofthreads = 10;

        ExecutorService executor = Executors.newFixedThreadPool(numofthreads);
        ThreadedClient[] threadpool = new ThreadedClient[numofthreads];

        // file writers
        for (int i = 0; i < numofthreads; i += 2) {
            threadpool[i] = new ThreadedClient(String.valueOf(i));
            threadpool[i].setRequest(HttpRequest.builder()
                    .method(HttpMethod.POST)
                    .uri("/test2.txt ")
                    .httpVersion("HTTP/1.0")
                    .body(threadpool[i].getName())
                    .build());
        }

        // file readers
        for (int i = 1; i < numofthreads; i += 2) {
            threadpool[i] = new ThreadedClient(String.valueOf(i));
            threadpool[i].setRequest(HttpRequest.builder()
                    .method(HttpMethod.GET)
                    .uri("/test2.txt")
                    .httpVersion("HTTP/1.0")
                    .body("")
                    .build());
        }

        for (int i = 0; i < numofthreads; i++) {
            executor.execute(threadpool[i]);
        }
    }

}
