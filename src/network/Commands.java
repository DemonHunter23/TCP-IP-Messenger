package network;

import java.io.IOException;

public interface Commands {

    // All available methods for the server

    void start() throws IOException, InterruptedException;
    void receiveMessage() throws IOException;
    void sendMessage() throws IOException;
    void disconnect() throws IOException;
}