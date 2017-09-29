package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import observer.IMessageObserver;
import observer.MessageObserver;

public class Client
{

    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", 444);
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            IMessageObserver observer = new MessageObserver();

            ClientMessageForwarder forwarder = new ClientMessageForwarder(toServer);
            ClientMessageReceiver receiver = new ClientMessageReceiver(observer, fromServer);
            new Thread(forwarder).start();
            new Thread(receiver).start();

            while (socket.getInputStream().read() > -1)
            {
                Thread.sleep(20);
            }

            System.out.println("Client closing...");
            forwarder.stop();
            receiver.stop();
            System.exit(0);
        }
        catch (IOException | InterruptedException ex)
        {
            System.err.println(ex);
        }
    }
}
