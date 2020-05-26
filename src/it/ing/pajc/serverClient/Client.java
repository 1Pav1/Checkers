    package it.ing.pajc.serverClient;

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

        public void clientStartup() {

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

        private void createCommunicationChannels() throws IOException {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void sendMessage(StringBuilder message){
            try {
                out.println(message.toString());
            }catch (Exception e){return;}
            System.out.println("Client has sent : "+message);

        }
        public StringBuilder readMessage(){
            try {
                return new StringBuilder(in.readLine());
            }catch (Exception ignored){}
            return null;
        }


    }
