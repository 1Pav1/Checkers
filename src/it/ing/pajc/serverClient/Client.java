package it.ing.pajc.serverClient;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.manager.Player;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Client {


    public static Socket clientStartup(int port, ItalianBoard board, Scene scene, Player player) throws ExecutionException, InterruptedException {

        FutureTask task = new FutureTask(new Callable<Socket>() {

            private Socket tryToConnect() {
                try {
                    System.out.println("Trying to connect");
                    return new Socket("localhost", port);
                } catch (IOException ignored) {
                }
                return null;
            }

            @Override
            public Socket call() throws IOException {
                Socket socket = tryToConnect();
                System.out.println("Connected");
                assert socket != null;
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ItalianBoard board = new ItalianBoard(new StringBuilder(in.readLine()));
                Platform.runLater(() -> Controller.placeBoard(board,scene,player));
                return socket;
            }
        });

        Thread Server = new Thread(task);
        Server.setName("Server");
        Server.start();
        return (Socket) task.get();

    }


}
