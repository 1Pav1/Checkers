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
                    /*Send first fen
                    sendMessage(board.getFen().reverse());
                    System.out.println("First fen sent");
                    drawBoard(board,scene,player);

                     */
                } catch (IOException e) {}
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
        try {
            return new StringBuilder(in.readLine());
        }catch (Exception e){}
        return null;
    }


}
