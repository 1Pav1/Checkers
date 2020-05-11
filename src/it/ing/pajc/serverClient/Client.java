    package it.ing.pajc.serverClient;

    import it.ing.pajc.controller.Controller;
    import it.ing.pajc.data.board.ItalianBoard;
    import it.ing.pajc.manager.MultiplayerManager;
    import it.ing.pajc.manager.Player;
    import javafx.application.Platform;
    import javafx.scene.Scene;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.io.PrintWriter;
    import java.net.Socket;

    public class Client {
        private int port;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Client(int port) {
            this.port = port;
        }

        public void clientStartup(ItalianBoard board, Scene scene, Player player) {

            Thread Client = new Thread(new Runnable() {
                private void tryToConnect() {
                    try {
                        System.out.println("Client is trying to connect...");
                        socket = new Socket("localhost", port);
                    } catch (Exception ignored) {
                    }
                }


                @Override
                public void run() {
                    try {
                        tryToConnect();
                        createCommunicationChannels();
                        System.out.println("Client is connected!");

                        //ItalianBoard b = new ItalianBoard();
                        /*Ricezione prima board
                        ItalianBoard b = new ItalianBoard(readMessage());
                        drawBoard(b,scene,player);
                        */

                    } catch (Exception e) {System.out.println("error receiving");}
                }
            });
            Client.setName("Client");
            Client.start();

        }

        public void createCommunicationChannels() throws IOException {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public boolean sendMessage(StringBuilder message){
            try {
                out.println(message.toString());
            }catch (Exception e){return false;}
            System.out.println("Client has sent : "+message);
            return true;

        }
        public StringBuilder readMessage(){
            try {
                return new StringBuilder(in.readLine());
            }catch (Exception e){}
            return null;
        }


    }
