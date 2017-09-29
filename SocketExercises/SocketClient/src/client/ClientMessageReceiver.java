package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import observer.IMessageObserver;

public class ClientMessageReceiver implements Runnable
{

    private boolean stop;
    private IMessageObserver observer;
    private BufferedReader fromServer;

    public ClientMessageReceiver(IMessageObserver observer, BufferedReader fromServer)
    {
        this.observer = observer;
        this.fromServer = fromServer;
    }

    @Override
    public void run()
    {
        while (!stop)
        {
            try
            {
                String message = null;

                while ((message = fromServer.readLine()) == null && !stop)
                {
                    Thread.sleep(10);
                }

                observer.dataReady(message);
            }
            catch (IOException | InterruptedException ex)
            {
                Logger.getLogger(ClientMessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stop()
    {
        stop = true;
    }

}
