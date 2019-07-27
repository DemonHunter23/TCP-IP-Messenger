package com.company;

import network.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = new Server(4000);
        server.start();
    }
}
