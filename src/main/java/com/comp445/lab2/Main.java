package com.comp445.lab2;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static java.util.Arrays.asList;

public class Main {
    public static void runClient(String[] args){
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("host", "h"), "EchoServer hostname")
                .withOptionalArg()
                .defaultsTo("localhost");

        parser.acceptsAll(asList("port", "p"), "EchoServer listening port")
                .withOptionalArg()
                .defaultsTo("8007");

        OptionSet opts = parser.parse(args);

        String host = (String) opts.valueOf("host");
        int port = Integer.parseInt((String) opts.valueOf("port"));

        SocketAddress endpoint = new InetSocketAddress(host, port);
        try {
            Client.runClient(endpoint);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runServer(String[] args){
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("port", "p"), "Listening port")
                .withOptionalArg()
                .defaultsTo("8007");

        OptionSet opts = parser.parse(args);
        int port = Integer.parseInt((String) opts.valueOf("port"));
        try {
            new Server().listenAndServe(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        runClient(args);
    }
}
