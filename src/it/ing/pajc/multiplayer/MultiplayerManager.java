package it.ing.pajc.multiplayer;

import it.ing.pajc.controller.CheckerBoardController;
import it.ing.pajc.controller.MultiplayerController;
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

    public MultiplayerManager(PiecesColors color,int port) {
        this.port = port;
        this.color = color;
    }


    public MultiplayerItalianBoard getBoard() {
        return board;
    }

    public void serverStartup() throws IOException {
        board = new MultiplayerItalianBoard("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM",color,this);
        ServerSocket serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(board.toString());

    }

    public void clientStartup() throws IOException {
        socket = new Socket("10.243.23.208", port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        board = new MultiplayerItalianBoard(in.readLine(),color,this);
        waitForMove();
    }

    private void waitForMove() {
        //String fen = readFen();
        MultiplayerManager mm = this;
        Platform.runLater(new Runnable() {
            private String readFen(){
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
                String fen = readFen();
                board = new MultiplayerItalianBoard(fen,color,mm);
                try {
                    MultiplayerController.drawBoard(board,color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*board = new MultiplayerItalianBoard(fen,color,this);
        MultiplayerController.drawBoard(board,color);*/
    }

    public void sendFen() throws IOException {
        String brd = board.toString();
        board = new MultiplayerItalianBoard(brd,color,this);
        board.lock();
        if(Move.noMovesLeft(board,color)){
            System.out.println("You won");
        }
        MultiplayerController.drawBoard(board,color);
        System.out.println("FEN code sent to client is: " + brd);
        out.println(brd);
        waitForMove();
    }

}

