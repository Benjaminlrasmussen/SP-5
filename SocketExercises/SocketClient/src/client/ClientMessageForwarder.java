package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientMessageForwarder implements Runnable
{
    private PrintWriter toServer;
    private boolean stop;

    public ClientMessageForwarder(PrintWriter toServer)
    {
        this.toServer = toServer;
    }
    
    @Override
    public void run()
    {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
        while (!stop)
        {
            String message = null;
            try
            {
                while ((message = input.readLine()) == null && !stop)                
                {
                    Thread.sleep(20);
                }
            }
            catch (IOException | InterruptedException  ex)
            {
                Logger.getLogger(ClientMessageForwarder.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            toServer.println(message);
        }
    }
    
    public void stop()
    {
        stop = true;
    }

}
