package it.ing.pajc.serverClient;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.Callable;

public class ServerThread implements Callable<Socket> {

    @Override
    public Socket call() throws Exception {
        ServerSocket ss = new ServerSocket(5555);
        while (true)
        {
            Socket s = null;
            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();
                System.out.println("A new client is connected : " + s);
                System.out.println("Assigning new thread for this client");
                return s;
            }
            catch (Exception e){
                assert s != null;
                s.close();
                e.printStackTrace();
            }
        }
    }
}
