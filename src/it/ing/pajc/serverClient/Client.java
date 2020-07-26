package it.ing.pajc.serverClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Client constructor
     *
     * @param port in order to connect
     */
    public Client(int port) {
        this.port = port;
    }

    /**
     * Startup of the client
     */
    public void clientStartup() {
        Thread Client = new Thread(new Runnable() {
            private void tryToConnect() {
                try {
                    System.out.println("Client is trying to connect...");
                    socket = new Socket("localhost", port);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void run() {
                try {
                    tryToConnect();
                    createCommunicationChannels();
                    System.out.println("Client is connected!");

                } catch (Exception e) {
                    System.out.println("error receiving");
                }
            }
        });
        Client.setName("Client");
        Client.start();

    }

    /**
     * Create a communication channel with server
     *
     * @throws IOException Exception
     */
    private void createCommunicationChannels() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Send a message
     *
     * @param message the fen
     */
    public void sendMessage(StringBuilder message) {
        try {
            out.println(message.toString());
        } catch (Exception e) {
            return;
        }
        System.out.println("Client has sent : " + message);
    }

    /**
     * Read a message
     *
     * @return the fen if possible
     */
    public StringBuilder readMessage() {
        try {
            return new StringBuilder(in.readLine());
        } catch (Exception ignored) {
        }
        return null;
    }


}
