package it.ing.pajc.multiplayer;

import it.ing.pajc.data.board.ItalianBoard;

import java.io.*;
import java.net.Socket;

public class Client implements java.io.Serializable{

    public static void main(String[] args)throws Exception {

        Socket s = new Socket("localhost", Server.PORT);
        System.out.println("Receiving data from server.....");
        InputStream is = s.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        System.out.println("Reading data.....");
        String str =(String)ois.readObject();
        System.out.println("Data recevied is: " + str);
        ItalianBoard board1 = new ItalianBoard(str);
        System.out.println("Converting FEN to actual rappresentation.....");
        board1.printBoardConsole();
        s.close();




    }

}
