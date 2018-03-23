package com.comp445.lab2;

import com.comp445.lab2.http.HttpRequest;
import com.comp445.lab2.http.HttpRequestParser;
import com.comp445.lab2.http.HttpRequestHandler;
import com.comp445.lab2.http.HttpResponse;
import com.comp445.lab2.http.exceptions.HttpFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final HttpRequestHandler handler = new HttpRequestHandler();


    // Uses a single buffer to demonstrate that all clients are running in a single thread
   // private final ByteBuffer readBuffer =   ByteBuffer.allocate(1024);

    private void readAndRespond(SelectionKey s) {
        SocketChannel client = (SocketChannel) s.channel();
        try {
            for (; ; ) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int n = client.read(readBuffer);
                // If the number of bytes read is -1, the peer is closed
                if (n == -1) {
                    unregisterClient(s);
                    return;
                }
                if (n == 0) {
                    return;
                }
                // ByteBuffer is tricky, you have to flip when switch from read to write, or vice-versa
                readBuffer.flip();
                HttpRequestParser parser = new HttpRequestParser();
                try {

                    byte[] remaining = new byte[readBuffer.remaining()];
                    readBuffer.get(remaining);  // make new buffer that is exactly the size of the string request
                                                // that was passed.
                    String request_string = new String(remaining, Charset.forName("UTF-8"));
                    System.out.println("request_string: " + request_string +"\nend request_string");
                    HttpRequest request = parser.parse(request_string);
                    HttpResponse response = handler.handleRequest(request);
                    logger.trace(String.format("REQUEST: \n%s \nRESPONSE: \n%s", request, response));

                    ByteBuffer writeBuffer = ByteBuffer.wrap(response.toString().getBytes("UTF-8"));
                    while(writeBuffer.hasRemaining()) {
                        client.write(writeBuffer);
                    }
                } catch(HttpFormatException e){
                    logger.error(e);
                    ByteBuffer buf = ByteBuffer.wrap(HttpResponse.getInvalidRequestResponse()
                            .toString().getBytes("UTF-8"));
                    while(buf.hasRemaining()) {
                        client.write(buf);
                    }
                }
                finally { readBuffer.clear(); }

            }
        } catch (IOException e) {
            unregisterClient(s);
            logger.error("Failed to receive/send data", e);
        }
    }

    private void newClient(ServerSocketChannel server, Selector selector) {
        try {
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            logger.info("New client from {}", client.getRemoteAddress());
            client.register(selector, OP_READ, client);
        } catch (IOException e) {
            logger.error("Failed to accept client", e);
        }
    }

    private void unregisterClient(SelectionKey s) {
        try {
            s.cancel();
            s.channel().close();
        } catch (IOException e) {
            logger.error("Failed to clean up", e);
        }
    }

    private void runLoop(ServerSocketChannel server, Selector selector) throws IOException {
        // Check if there is any event (eg. new client or new data) happened
        selector.select();

        for (SelectionKey s : selector.selectedKeys()) {
            // Acceptable means there is a new incoming
            if (s.isAcceptable()) {
                newClient(server, selector);

                // Readable means this client has sent data or closed
            } else if (s.isReadable()) {
                readAndRespond(s);
            }
        }
        // We must clear this set, otherwise the select will return the same value again
        selector.selectedKeys().clear();
    }

    public void listenAndServe(int port) throws IOException {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            Selector selector = Selector.open();

            // Register the server socket to be notified when there is a new incoming client
            logger.info("Server Listening on port " + String.valueOf(port));
            server.register(selector, OP_ACCEPT, null);
            while(true) {
                runLoop(server, selector);
            }
        }
    }
}
