package it.ing.pajc.multiplayer;

import it.ing.pajc.data.board.ItalianBoard;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    public static final int PORT = 5555;

    public static void main(String[] args) throws Exception{

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


}
