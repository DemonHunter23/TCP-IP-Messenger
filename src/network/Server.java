package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Commands {

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    private ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void start() throws IOException, InterruptedException {
        System.out.println("Creating server socket...");
        Socket socket = getServerSocket().accept();

        System.out.println("Server successfully started on: " + serverSocket.getInetAddress() +
                            ":" + serverSocket.getLocalPort());

        System.out.println("Waiting for someone to connect");
        System.out.println("Connecting to: " + socket.getInetAddress() + ":" + socket.getPort());

        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        Thread firstThread = new Thread(() -> {
            while (true) {
                try {
                    sendMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        firstThread.start();

        Thread secondThread = new Thread(() -> {
            while (true) {
                try {
                    receiveMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        secondThread.start();

        firstThread.join();
        secondThread.join();
    }

    //This method is receiving messages from the client

    @Override
    public void receiveMessage() throws IOException {
        String message;

        while (true) {
            message = dataInputStream.readUTF();
            System.out.println("Client says: " + message);

            if (message.equals("$exit")) {
                disconnect();
                break;
            }

            dataOutputStream.flush();
        }
    }

    // This method is used for sending messages to the client

    @Override
    public void sendMessage() throws IOException {
        String message;

        while (true) {
            Scanner scanner = new Scanner(System.in);
            message = scanner.nextLine();

            dataOutputStream.writeUTF(message);

            if (message.equals("$exit")) {
                disconnect();
                break;
            }
        }
    }

    //Method is called to disconnect the server

    @Override
    public void disconnect() {
        System.out.println("Exiting");
        System.exit(0);
    }
}