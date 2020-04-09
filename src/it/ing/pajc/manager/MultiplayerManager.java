package it.ing.pajc.manager;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.serverClient.Client;
import it.ing.pajc.serverClient.Server;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

/**
 * Manages the creation and the communication between 2 players.
 */
public class MultiplayerManager {
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Player currentPlayer;
    private Player chosenPlayer;
    private ItalianBoard board;


    /**
     * @param chosenPlayer
     * @param port
     */
    public MultiplayerManager(Player chosenPlayer, int port) {
        StringBuilder fen = new StringBuilder("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new ItalianBoard(fen);
        this.chosenPlayer = chosenPlayer;
        this.currentPlayer = Player.FIRST;
        this.port = port;
        //Controller.placeBoard(board);
    }


    /**
     * Creates new board and a thread that manages the connection.
     */
    public void startServer() throws IOException, ExecutionException, InterruptedException {
        socket = Server.serverStartup(port, board);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(board.toString());
    }

    /**
     * Creates a new board and a thread that manages the connection to the server.
     */
    public void clientStartup() throws ExecutionException, InterruptedException, IOException {
        socket = Client.clientStartup(port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Creates a thread that sends the current board and waits for the other players turn to finish.
     */
    private void waitForMove() {

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
                board = new ItalianBoard(new StringBuilder(readFen()));
                try {
                    Controller.placeBoard(board);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sends the current board in form of FEN notation.
     *
     * @throws IOException In case the FEN can't be sent.
     */
    public void sendFen() throws IOException {
        out.println(board.toString());
        waitForMove();
    }
}