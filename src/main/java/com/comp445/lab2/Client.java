package com.comp445.lab2;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private static void writeAndReadResponse(SocketChannel socket) throws IOException {
        Charset utf8 = StandardCharsets.UTF_8;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            //ByteBuffer buf = ByteBuffer.allocate(1024); // for some reason, if we don't use a new bug in every iteration,
                                                        // client crashes. should fix.
            String line = scanner.nextLine();
            ByteBuffer buf = utf8.encode(line);
            //socket.socket().setReceiveBufferSize(1024*1024);
            //buf.put(utf8.encode(line));
            while(buf.hasRemaining()) {
                socket.write(buf);
            }
            buf.clear();

            // Receive response back
            ByteBuffer readBuf = ByteBuffer.allocate(1024*1024);
            socket.read(readBuf);
            readBuf.flip();

            System.out.println("Replied: " + utf8.decode(readBuf));
        }
    }

    public static void runClient(SocketAddress endpoint) throws IOException {
        try (SocketChannel socket = SocketChannel.open()) {
            socket.connect(endpoint);
            System.out.println("Type any thing then ENTER. Press Ctrl+C to terminate");
            writeAndReadResponse(socket);
        }
    }

}

