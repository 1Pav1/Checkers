package it.ing.pajc.controller;

import it.ing.pajc.Main;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;
import it.ing.pajc.multiplayer.Server;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SettingsController implements Serializable {
    private double x,y;
    public static Stage primaryStage;
    public static final int PORT = 5555;

    public static void setPrimaryStage(Stage primaryStage) {
        SettingsController.primaryStage = primaryStage;
    }

    public void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../graphics/Home.fxml"));
            Scene scene = new Scene(root);
            Main.getPrimaryStage().setScene(scene);
            Main.getPrimaryStage().setTitle("Checker main menu");
            //we gonna drag the frame
            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void close(){
        Platform.exit();
    }

    public void server() throws IOException {
        System.out.println("S: Server is started.");
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("S: Server is waiting for client request...");

        Socket s = ss.accept();
        System.out.println("S: Client connected.");


        ItalianBoard board = new ItalianBoard();
        String brd = board.toString();
        System.out.println("FEN code sent to client is: " + brd);

        OutputStream outputStream = s.getOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);

        os.writeObject(brd);
        outputStream.close();
        os.close();
    }

    public void client() throws IOException, ClassNotFoundException {
        Socket s = new Socket("localhost", Server.PORT);
        System.out.println("Receiving data from server.....");
        InputStream is = s.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        System.out.println("Reading data.....");
        String str =(String)ois.readObject();
        System.out.println("Data received is: " + str);
        ItalianBoard board1 = new ItalianBoard(str, PiecesColors.WHITE);
        System.out.println("Converting FEN to actual representation.....");
        board1.printBoardConsole();
        s.close();
    }
}
