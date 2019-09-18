package it.ing.pajc.multiplayer;

import it.ing.pajc.controller.CheckerBoardController;
import it.ing.pajc.controller.MultiplayerController;
import it.ing.pajc.data.board.Fen;
import it.ing.pajc.data.board.MultiplayerItalianBoard;
import it.ing.pajc.data.movements.Move;
import it.ing.pajc.data.pieces.PiecesColors;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplayerManager {
    private int port;
    private Socket socket;
    private MultiplayerItalianBoard board;
    private PiecesColors color;
    private BufferedReader in;
    private PrintWriter out;

    public MultiplayerManager(PiecesColors color, int port) {
        this.port = port;
        this.color = color;
    }


    public MultiplayerItalianBoard getBoard() {
        return board;
    }

    public void serverStartup() throws IOException {

        Fen fen = new Fen("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new MultiplayerItalianBoard(fen, color, this);
        Thread Server = new Thread(new Runnable() {

            private void tryToConnect() {
                try {
                    System.out.println("Trying to connect");
                    ServerSocket serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                } catch (IOException e) {
                }
            }
            @Override
            public void run() {

                try {
                    tryToConnect();
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out.println(board.toString());
                } catch (IOException e) {
                    System.err.println("Error sending board");
                }
            }
        });

        Server.setName("Server");
        Server.start();

    }

    public void clientStartup() throws IOException {
        Fen fen = new Fen("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new MultiplayerItalianBoard(fen, color, this);
        MultiplayerManager mm = this;
        Thread Client = new Thread(new Runnable() {
            private void tryToConnect(){
                try {
                    System.out.println("Trying to connect");
                    socket = new Socket("localhost", port);
                } catch (Exception e) {
                }
            }
            @Override
            public void run() {
                try {
                    tryToConnect();
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Fen fen = new Fen(in.readLine());
                    board = new MultiplayerItalianBoard(fen, color, mm);
                } catch (IOException e) {
                    System.err.println("Error receiving board");
                }
            }
        });
        Client.setName("Client");
        Client.start();
        waitForMove();
    }

    private void waitForMove() {
        //String fen = readFen();
        MultiplayerManager mm = this;


        Platform.runLater(new Runnable() {
            private String readFen() {
                String fen = null;
                try {
                    fen = in.readLine();
                } catch (IOException e) {
                    readFen();
                }
                return fen;
            }

            @Override
            public void run() {
                Fen fen = new Fen(readFen());
                board = new MultiplayerItalianBoard(fen, color, mm);
                try {
                    MultiplayerController.drawBoard(board, color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*board = new MultiplayerItalianBoard(fen,color,this);
        MultiplayerController.drawBoard(board,color);*/
    }

    public void sendFen() throws IOException {
        Fen fen = new Fen(board.toString());
        board = new MultiplayerItalianBoard(fen, color, this);
        board.lock();
        if (Move.noMovesLeft(board, color)) {
            System.out.println("You won");
        }
        MultiplayerController.drawBoard(board, color);
        System.out.println("FEN code sent to client is: " + board.toString());
        out.println(board.toString());
        waitForMove();
    }

}