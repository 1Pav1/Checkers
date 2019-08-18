package it.ing.pajc.multiplayer;

import it.ing.pajc.data.board.Board;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.data.pieces.PiecesColors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiplayerManager {
    private int port;
    private Socket socket;
    private ItalianBoard board;

    public MultiplayerManager(PiecesColors color,int port) {
        this.port = port;
        board = new ItalianBoard("memememe/emememem/memememe/eeeeeeee/eeeeeeee/eMeMeMeM/MeMeMeMe/eMeMeMeM",color);
    }


    public ItalianBoard getBoard() {
        return board;
    }

    public void serverStartup() throws IOException {
        System.out.println("S: Server is started.");
        ServerSocket ss = new ServerSocket(port);

        System.out.println("S: Server is waiting for client request...");

        Socket s = ss.accept();
        System.out.println("S: Client connected.");

        String brd = board.toString();
        System.out.println("FEN code sent to client is: " + brd);
        OutputStream outputStream = s.getOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(brd);
        outputStream.close();
        os.close();

    }

    public void clientStartup() throws IOException, ClassNotFoundException {
        Socket s = new Socket("localhost", port);
        System.out.println("Receiving data from server.....");
        InputStream is = s.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        System.out.println("Reading data.....");
        String str = (String) ois.readObject();
        System.out.println("Data received is: " + str);
        board = new ItalianBoard(str, PiecesColors.BLACK);
        System.out.println("Converting FEN to actual representation.....");
        s.close();
    }

    private void sendFen() throws IOException {
        PiecesColors color = PiecesColors.BLACK;
        String brd = this.toString();
        System.out.println("FEN code sent to client is: " + brd);

        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);

        os.writeObject(brd);
        outputStream.close();
        os.close();
    }
}

