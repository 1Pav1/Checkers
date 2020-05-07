package it.ing.pajc.serverClient;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.io.IOException;

public class Server {
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Server(int port) {
        this.port = port;
    }

    public void serverStartup(ItalianBoard board, Scene scene, Player player) throws IOException {

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
                } catch (IOException e) {}

                drawBoard(board,scene,player);
                sendMessage(board.getFen().reverse());
            }
        });
        Server.setName("Server");
        Server.start();

    }

    public void createCommunicationChannels() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void drawBoard(ItalianBoard board, Scene scene, Player player){
        Platform.runLater(() -> {
            Controller.placeBoard(board,scene,player);
        });
    }

    public void sendMessage(StringBuilder message){
        System.out.println("Server has sent : "+message);
        out.println(message.toString());
    }

    public StringBuilder readMessage() throws IOException {
        return new StringBuilder(in.readLine());
    }


    public void waitForMove(Scene scene, Player player) {
        Platform.runLater(new Runnable() {
            private StringBuilder readFen() {
                StringBuilder fen = null;
                try {
                    fen = new StringBuilder(in.readLine());
                } catch (IOException e) {
                    readFen();
                }
                return fen;
            }

            @Override
            public void run() {
                StringBuilder fen = readFen();
                ItalianBoard board = new ItalianBoard(fen);
                try {
                    drawBoard(board,scene,player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
