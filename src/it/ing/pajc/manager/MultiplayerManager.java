package it.ing.pajc.manager;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.movements.Move;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.serverClient.Client;
import it.ing.pajc.serverClient.Server;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;

import static it.ing.pajc.controller.FXUtility.changeScene;

/**
 * Manages the creation and the communication between 2 players.
 */
public class MultiplayerManager {
    private Player currentPlayer;
    private Player chosenPlayer;
    private ItalianBoard board;
    private Scene scene;
    private Server server;
    private Client client;

    /**
     * Constructor of multyplayer manager
     *
     * @param scene taken in consideration
     */
    public MultiplayerManager(Scene scene) {
        this.currentPlayer = Player.WHITE_PLAYER;
        this.scene = scene;
        server = null;
        client = null;
        Controller.activateTurnIndicator(scene);
        multiplayerGame();
    }

    /**
     * Play the music
     *
     * @param musicLocation the path
     */
    private static void playMusic(String musicLocation) {
        try {
            File musicPath = new File(musicLocation);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Can't find file");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Start the multiplayerGame
     */
    private void multiplayerGame() {
        Controller.timeToChangePlayer = new SimpleBooleanProperty(false);
        Controller.timeToChangePlayer.addListener((observable, oldValue, newValue) -> {
            if (!oldValue) {
                changePlayer();
                System.err.println(currentPlayer);
            }
        });
    }

    /**
     * Change the player
     */
    private void changePlayer() {
        StringBuilder msg = new StringBuilder(board.getFen().reverse().toString() + 3);
        if (server == null) {
            Platform.runLater(() -> Controller.placeBoardWithDisabledClicks(board, scene, chosenPlayer));
            Platform.runLater(() -> Controller.changeTurnIndicator(scene, "It's enemies turn", 0));
            client.sendMessage(msg);
        } else {
            Platform.runLater(() -> Controller.placeBoardWithDisabledClicks(board, scene, chosenPlayer));
            Platform.runLater(() -> Controller.changeTurnIndicator(scene, "It's enemies turn", 0));
            server.sendMessage(msg);
        }

        Controller.timeToChangePlayer.setValue(false);
        currentPlayer = currentPlayer == Player.WHITE_PLAYER ? Player.BLACK_PLAYER : Player.WHITE_PLAYER;
        changePlayerFX();
    }

    /**
     * Change player
     */
    private void changePlayerFX() {
        Controller.placeBoard(board, scene, chosenPlayer);
    }

    /**
     * Check if the game is lost
     */
    private void checkLost() {
        if (!Move.canSomebodyDoSomething(board, chosenPlayer)) {
            Parent root = null;
            try {
                if (chosenPlayer != Player.WHITE_PLAYER) {
                    root = FXMLLoader.load(getClass().getResource("../GUI/WhiteWins.fxml"));
                    StringBuilder msg = new StringBuilder(board.getFen().reverse().toString() + 4);
                    if (server == null) {
                        client.sendMessage(msg);
                    } else {
                        server.sendMessage(msg);
                    }
                } else {
                    root = FXMLLoader.load(getClass().getResource("../GUI/BlackWins.fxml"));
                    StringBuilder msg = new StringBuilder(board.getFen().reverse().toString() + 5);
                    if (server == null) {
                        client.sendMessage(msg);
                    } else {
                        server.sendMessage(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            Scene scene = new Scene(root);
            changeScene(root, scene);
        }
    }

    /**
     * Creates new board and a thread that manages the connection.
     *
     * @param chosenPlayer the player chosen
     * @param port         in order to start the server
     */
    public void startServer(Player chosenPlayer, int port) {
        StringBuilder fen = new StringBuilder("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        if (chosenPlayer == Player.BLACK_PLAYER)
            fen = fen.reverse();
        board = new ItalianBoard(fen);
        this.chosenPlayer = chosenPlayer;
        server = new Server(port);
        server.serverStartup(board, chosenPlayer);

        if (chosenPlayer == Player.WHITE_PLAYER) {
            Platform.runLater(() -> Controller.changeTurnIndicator(scene, "It's your turn", 1));
            Controller.placeBoard(board, scene, chosenPlayer);
        } else {
            Platform.runLater(() -> Controller.changeTurnIndicator(scene, "It's enemies turn", 0));
            Controller.placeBoardWithDisabledClicks(board, scene, chosenPlayer);
        }
        waitForMove();
    }

    /**
     * Creates a new board and a thread that manages the connection to the server.
     *
     * @param port in order to start a client
     */
    public void startClient(int port) {
        client = new Client(port);
        client.clientStartup();
        waitForMove();
    }

    /**
     * Creates a thread that sends the current board and waits for the other players turn to finish.
     */
    private void waitForMove() {

        Thread receive = new Thread(new Runnable() {

            private StringBuilder readMsg() {
                StringBuilder fen;
                if (server == null) {
                    fen = client.readMessage();
                } else {
                    fen = server.readMessage();
                }
                return fen;
            }

            @Override
            public void run() {
                do {
                    StringBuilder received;
                    do {
                        received = readMsg();
                    } while (received == null);


                    int serverPlayer = 0;
                    try {
                        serverPlayer = Integer.parseInt((received.substring(71)));
                    } catch (NumberFormatException ignored) {
                    }

                    System.out.println(serverPlayer);
                    StringBuilder fen = new StringBuilder(received.substring(0, 71));
                    System.out.println(fen);

                    board = new ItalianBoard(fen);

                    try {
                        System.out.println("trying to placeboard " + received);

                        if (serverPlayer == 1 || serverPlayer == 0) {
                            StringBuilder f = new StringBuilder("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
                            board = new ItalianBoard(f);
                            board.rotate();
                            chosenPlayer = Player.BLACK_PLAYER;
                            System.err.println("Placeboard without clicks " + serverPlayer);
                            Platform.runLater(() -> Controller.placeBoardWithDisabledClicks(board, scene, chosenPlayer));
                            Platform.runLater(() -> Controller.changeTurnIndicator(scene, "It's enemies turn", 0));
                        } else if (serverPlayer == 2) {
                            chosenPlayer = Player.WHITE_PLAYER;
                            StringBuilder f = new StringBuilder("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
                            board = new ItalianBoard(f);
                            Platform.runLater(() -> Controller.placeBoard(board, scene, chosenPlayer));
                            Platform.runLater(() -> Controller.changeTurnIndicator(scene, "It's your turn", 1));
                        } else if (serverPlayer == 3) {
                            String filepath = "src/it/ing/pajc/Audio/yourTurn.wav";
                            playMusic(filepath);
                            System.err.println("Placeboard with clicks " + serverPlayer);
                            Platform.runLater(() -> Controller.placeBoard(board, scene, chosenPlayer));
                            Platform.runLater(() -> Controller.changeTurnIndicator(scene, "It's your turn", 1));

                            Platform.runLater(() -> checkLost());
                        } else if (serverPlayer == 4) {
                            Platform.runLater(() -> {
                                        Parent root = null;
                                        try {
                                            root = FXMLLoader.load(getClass().getResource("../GUI/WhiteWins.fxml"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        assert root != null;
                                        Scene scene = new Scene(root);
                                        changeScene(root, scene);
                                    }
                            );
                        } else if (serverPlayer == 5) {
                            Platform.runLater(() -> {
                                        Parent root = null;
                                        try {
                                            root = FXMLLoader.load(getClass().getResource("../GUI/BlackWins.fxml"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        assert root != null;
                                        Scene scene = new Scene(root);
                                        changeScene(root, scene);
                                    }
                            );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (true);
            }
        });
        receive.setName("rec");
        receive.start();
    }
}