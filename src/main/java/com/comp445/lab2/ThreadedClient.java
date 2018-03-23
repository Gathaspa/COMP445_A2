package com.comp445.lab2;

import com.comp445.lab2.http.HttpRequest;
import lombok.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class ThreadedClient extends Thread {

    private HttpRequest request;
    private InetSocketAddress socketaddress;

    public ThreadedClient(String name) {
        super(name);
    }

    public void run() {
        if (socketaddress == null) socketaddress = new InetSocketAddress("localhost", 8007);
        System.out.println("Run: " + getName());
        try (SocketChannel socket = SocketChannel.open()) {
            socket.connect(socketaddress);
            writeAndReadResponse(socket);
        } catch (IOException e) {
            System.out.print("Unrecoverable failure encountered when trying to connect to the socket. Killing Thread "
                    + getName());
            System.exit(0);
        }
    }

    private void writeAndReadResponse(SocketChannel socket) throws IOException {
        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuffer buf = utf8.encode(getRequest().toString());
        while (buf.hasRemaining()) {
            socket.write(buf);
        }
        buf.clear();

        // Receive response back
        ByteBuffer readBuf = ByteBuffer.allocate(1024 * 1024);
        socket.read(readBuf);
        readBuf.flip();

        System.out.println("Replied to Thread " + getName() + ": " + utf8.decode(readBuf));
    }

}


