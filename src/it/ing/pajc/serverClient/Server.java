package it.ing.pajc.serverClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.manager.Player;

import java.io.IOException;

public class Server {
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Server constructor
     *
     * @param port of the server
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Startup of the server
     *
     * @param board  chosen
     * @param player chosen
     */
    public void serverStartup(ItalianBoard board, Player player) {
        Thread Server = new Thread(new Runnable() {
            private void tryToConnect() {
                try {
                    System.out.println("Server is listening...");
                    ServerSocket serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                } catch (IOException ignored) {
                }
            }

            @Override
            public void run() {
                tryToConnect();
                System.out.println("Server is connected!");
                try {
                    createCommunicationChannels();
                    StringBuilder msg;
                    if (player == Player.WHITE_PLAYER)
                        msg = new StringBuilder(board.getFen().reverse().toString() + 1);
                    else
                        msg = new StringBuilder(board.getFen().reverse().toString() + 2);
                    boolean sent;
                    do {
                        sent = sendMessage(msg);
                    } while (!sent);
                } catch (Exception e) {
                    System.out.println("error creating channels");
                }
            }
        });
        Server.setName("Server");
        Server.start();
    }

    /**
     * Create a communication channel with client
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
     * @return true if it has send the message
     */
    public boolean sendMessage(StringBuilder message) {
        try {
            out.println(message.toString());
        } catch (Exception e) {
            return false;
        }
        System.out.println("Server has sent : " + message);
        return true;
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
