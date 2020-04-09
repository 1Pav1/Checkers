package it.ing.pajc.serverClient;

import it.ing.pajc.controller.Controller;
import it.ing.pajc.data.board.ItalianBoard;
import javafx.application.Platform;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
/*
    public static Socket serverStartup(int port, ItalianBoard board) throws ExecutionException, InterruptedException {

        FutureTask task = new FutureTask(new Callable<Socket>() {

            private Socket tryToConnect() {
                try {
                    System.out.println("Trying to connect");
                    ServerSocket serverSocket = new ServerSocket(port);
                    return serverSocket.accept();
                } catch (IOException ignored) {
                }
                return null;
            }

            @Override
            public Socket call() {
                Socket socket = tryToConnect();
                System.out.println("Connected");

                Platform.runLater(() -> {
                    Controller.placeBoard(board);
                });
                return socket;
            }
        });

        Thread Server = new Thread(task);
        Server.setName("Server");
        Server.start();
        return (Socket) task.get();

    }
*/

}
