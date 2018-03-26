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
        String inpt = getUserInput();
        if (inpt.equals("")) return; // in case first line sent in request is a '.'
        ByteBuffer buf = utf8.encode(inpt);
        while (buf.hasRemaining()) {
            socket.write(buf);
        }
        buf.clear();

        // Receive response back
        ByteBuffer readBuf = ByteBuffer.allocate(1024 * 1024);
        socket.read(readBuf);
        readBuf.flip();

        System.out.println("Replied: " + utf8.decode(readBuf));
    }

    public static void runClient(SocketAddress endpoint) throws IOException {
        try (SocketChannel socket = SocketChannel.open()) {
            socket.connect(endpoint);
            System.out.println("Type any request, write a line with a single '.' to end request. Press Ctrl+C to terminate");
            writeAndReadResponse(socket);
        }
    }

    private static String getUserInput() {
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals(".")) break;
            builder.append(line + "\r\n");
        }
        return builder.toString();
    }
}

