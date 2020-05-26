package it.ing.pajc.serverClient;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import it.ing.pajc.data.board.ItalianBoard;
import it.ing.pajc.manager.Player;
import java.io.IOException;

public class Server {
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Server(int port) {
        this.port = port;
    }

    public void serverStartup(ItalianBoard board, Player player){

        Thread Server = new Thread(new Runnable() {

            private void tryToConnect() {
                try {
                    System.out.println("Server is listening...");
                    ServerSocket serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                } catch (IOException ignored) {
                }
            }

            @Override
            public void run() {
                tryToConnect();
                System.out.println("Server is connected!");
                try {
                    createCommunicationChannels();
                    StringBuilder msg;
                    if(player==Player.WHITE_PLAYER)
                         msg = new StringBuilder(board.getFen().reverse().toString()+1);
                    else
                        msg = new StringBuilder(board.getFen().reverse().toString()+2);
                    boolean sent;
                    do{
                        sent = sendMessage(msg);
                    }while(!sent);
                    /*Send first fen
                    sendMessage(board.getFen().reverse());
                    System.out.println("First fen sent");
                    drawBoard(board,scene,player);
                     */
                } catch (Exception e) {System.out.println("error creating channels");}
            }
        });
        Server.setName("Server");
        Server.start();

    }

    private void createCommunicationChannels() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public boolean sendMessage(StringBuilder message){
        try {
            out.println(message.toString());
        }catch (Exception e){
            System.err.println("Chè ci fai quaa!?");
            return false;
        }

        System.out.println("Server has sent : "+message);
        return true;

    }

    public StringBuilder readMessage(){
        try {
            return new StringBuilder(in.readLine());
        }catch (Exception ignored){}
        return null;
    }


}
