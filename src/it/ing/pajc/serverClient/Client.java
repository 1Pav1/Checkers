package it.ing.pajc.serverClient;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.scene.Scene;

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

    public Client(int port) {
        this.port = port;
    }

    public void clientStartup(ItalianBoard board, Scene scene, Player player) {

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
                    drawBoard(new ItalianBoard(readMessage()),scene,player);
                    waitForMove(scene,player);
                } catch (IOException e) {
                    System.err.println("Error receiving board");
                }
            }
        });
        Client.setName("Client");
        Client.start();

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
        System.out.println("Client has sent : "+message);
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
                } catch (Exception e) {
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
