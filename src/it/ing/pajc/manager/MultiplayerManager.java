package it.ing.pajc.manager;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.controller.Move;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.serverClient.Client;
import it.ing.pajc.serverClient.Server;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;

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

    public MultiplayerManager(Player chosenPlayer, Scene scene) {
        StringBuilder fen = new StringBuilder("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM");
        if(chosenPlayer == Player.SECOND)
            fen = fen.reverse();

        board = new ItalianBoard(fen);
        this.chosenPlayer = chosenPlayer;
        this.currentPlayer = Player.FIRST;
        this.scene = scene;
        server = null;
        client = null;
        multiplayerGame();
    }

    public void multiplayerGame() {
        Controller.timeToChangePlayer = new SimpleBooleanProperty(false);
        Controller.timeToChangePlayer.addListener((observable, oldValue, newValue) -> {
            if (!oldValue) {
                changePlayer();
                System.err.println(currentPlayer);
            }
        });

    }

    private void changePlayer() {
        if(server==null) {
            client.drawBoard(board,scene,chosenPlayer);
            client.sendMessage(board.getFen().reverse());
            waitForMove();
        }
        else {
            server.drawBoard(board,scene,chosenPlayer);
            server.sendMessage(board.getFen().reverse());
            waitForMove();
        }

        Controller.timeToChangePlayer.setValue(false);
        currentPlayer = currentPlayer == Player.FIRST ? Player.SECOND : Player.FIRST;
        changePlayerFX();
    }

    private void changePlayerFX() {
        //if(currentPlayer!=chosenPlayer)
        //Block all pieces
        //checkLost();
        Controller.placeBoard(board, scene, chosenPlayer);
    }


    private void checkLost() {
        if (!Move.canSomebodyDoSomething(board, currentPlayer)) {
            Parent root = null;
            try {
                if (currentPlayer != Player.FIRST)
                    root = FXMLLoader.load(getClass().getResource("../GUI/WhiteWins.fxml"));
                else
                    root = FXMLLoader.load(getClass().getResource("../GUI/BlackWins.fxml"));
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
     */
    public void startServer(int port) throws IOException {
        server = new Server(port);
        server.serverStartup(board,scene,chosenPlayer);
        server.drawBoard(board,scene,chosenPlayer);
    }

    /**
     * Creates a new board and a thread that manages the connection to the server.
     */
    public void clientStartup(int port) throws ExecutionException, InterruptedException, IOException {
        client = new Client(port);
        client.clientStartup(board,scene,chosenPlayer);
        waitForMove();
    }

    /**
     * Creates a thread that sends the current board and waits for the other players turn to finish.
     */
    private void waitForMove() {
        /*
        Thread receive = new Thread(new Runnable() {

            private StringBuilder readFen() {
                StringBuilder fen = null;
                try {
                    if(server==null) {
                        fen = client.readMessage();
                        //System.out.println("Client has received: "+fen);
                    }
                    else {
                        fen = server.readMessage();
                        //System.out.println("Server has received: "+fen);
                    }

                } catch (IOException e) {
                    readFen();
                }
                return fen;
            }

            @Override
            public void run() {
                StringBuilder received;
                do{
                    received = readFen();
                }while(received==null);
                board = new ItalianBoard(received);
                try {
                    System.out.println("trying to placeboard "+received);
                    Controller.placeBoard(board, scene,chosenPlayer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        receive.setName("rec");
        receive.start();
        */


        Platform.runLater(new Runnable() {

            private StringBuilder readFen() {
                StringBuilder fen = null;
                try {
                    if(server==null) {
                        fen = client.readMessage();
                        //System.out.println("Client has received: "+fen);
                    }
                    else {
                        fen = server.readMessage();
                        //System.out.println("Server has received: "+fen);
                    }

                } catch (IOException e) {
                    readFen();
                }
                return fen;
            }

            @Override
            public void run() {
                StringBuilder received;
                do{
                    received = readFen();
                }while(received==null);
                board = new ItalianBoard(received);
                try {
                    System.out.println("trying to placeboard "+received);
                    Controller.placeBoard(board, scene,chosenPlayer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}