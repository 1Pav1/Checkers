package it.ing.pajc.multiplayer;

import it.ing.pajc.controller.MultiplayerController;
import it.ing.pajc.data.board.Fen;
import it.ing.pajc.data.board.MultiplayerItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Manages the creation and the communication between 2 players.
 */
public class MultiplayerManager {
    private int port;
    private Socket socket;
    private MultiplayerItalianBoard board;
    private PiecesColors color;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructor of MultiplayerManager.
     *
     * @param color Selected by the player.
     * @param port  Port on which the communication happens.
     */
    public MultiplayerManager(PiecesColors color, int port) {
        this.port = port;
        this.color = color;
    }

    /**
     * Getter of the board.
     *
     * @return The current board.
     */
    public MultiplayerItalianBoard getBoard() {
        return board;
    }

    /**
     * Creates new board and a thread that manages the connection.
     */
    public void serverStartup() throws IOException {

        Fen fen = new Fen("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new MultiplayerItalianBoard(fen, color, this);
        Thread Server = new Thread(new Runnable() {

            private void tryToConnect() {
                try {
                    System.out.println("Trying to connect");
                    ServerSocket serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                } catch (IOException ignored) {
                }
            }

            @Override
            public void run() {

                try {
                    tryToConnect();
                    System.out.println("Connected");
                    Platform.runLater(() -> {
                        try {
                            draw();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
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

    private void draw() throws IOException {
        MultiplayerController.drawBoard(board, color);
    }

    /**
     * Creates a new board and a thread that manages the connection to the server.
     */
    public void clientStartup() {
        Fen fen = new Fen("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        board = new MultiplayerItalianBoard(fen, color, this);
        MultiplayerManager mm = this;
        Thread Client = new Thread(new Runnable() {
            private void tryToConnect() {
                try {
                    System.out.println("Trying to connect");
                    socket = new Socket("localhost", port);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void run() {
                try {
                    tryToConnect();
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Fen fen = new Fen(in.readLine());
                    System.out.println("Connected");
                    board = new MultiplayerItalianBoard(fen, color, mm);
                    Platform.runLater(() -> {
                        try {
                            draw();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Error receiving board");
                }
            }
        });
        Client.setName("Client");
        Client.start();
        waitForMove();
    }

    /**
     * Creates a thread that sends the current board and waits for the other players turn to finish.
     */
    private void waitForMove() {
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
    }

    /**
     * Sends the current board in form of FEN notation.
     *
     * @throws IOException In case the FEN can't be sent.
     */
    public void sendFen() throws IOException {
        board.lock();
        MultiplayerController.drawBoard(board, color);
        System.out.println("FEN code sent to client is: " + new Fen("eeeeeeee/ememeeee/eeMeeeee/eeeeeeee/eeeeeeee/eeeeeeee/eeeeeeee/eeeeeeeM").getFen());
        out.println(board.toString());
        waitForMove();
    }
}