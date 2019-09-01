package it.ing.pajc.multiplayer;

import it.ing.pajc.Main;
import it.ing.pajc.controller.CheckerBoardController;
import it.ing.pajc.controller.MultiplayerController;
import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.board.MultiplayerItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplayerManager {
    private int port;
    private Socket socket;
    private MultiplayerItalianBoard board;
    private Multiplayer type;
    private PiecesColors color;
    private OutputStream outputStream;
    private InputStream inputStream;
    private OutputStreamWriter outputStreamWriter;
    private InputStreamReader inputStreamReader;
    private BufferedReader in;
    private BufferedWriter bufferedWriter;
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

    public void clientStartup() throws IOException, ClassNotFoundException {
        socket = new Socket("localhost", port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        board = new MultiplayerItalianBoard(in.readLine(),color,this);
        waitForMove();
    }

    public void waitForMove() throws IOException {
        String fen = readFen();
        board = new MultiplayerItalianBoard(fen,color,this);
        MultiplayerController.drawBoard(board,color);
    }

    public void sendFen() throws IOException {
        String brd = board.toString();
        System.out.println("FEN code sent to client is: " + brd);
        out.println(brd);
        String fen = readFen();
        board = new MultiplayerItalianBoard(fen,color,this);
        MultiplayerController.drawBoard(board,color);
    }

    private String readFen(){
        String fen = null;
        try {
            fen = in.readLine();
            return fen;
        }catch (Exception e){readFen();};
        return fen;
    }
}

