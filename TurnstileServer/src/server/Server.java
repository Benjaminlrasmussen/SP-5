package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server
{

    private int port;
    private String serverName;
    private int spectators;

    public Server(String serverName, int port)
    {
        this.serverName = serverName;
        this.port = port;
    }

    public void start()
    {
        System.out.println("Server started.");
        try
        {
            ServerSocket socket = new ServerSocket(port);

            while (true)
            {
                Socket clientSocket = socket.accept();
                System.out.println("Client connected");
                addNewClient(clientSocket);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addNewClient(Socket clientSocket) throws IOException
    {
        PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
        toClient.println("Welcome to: " + serverName);
        toClient.println("Are you a turnstile y/n?");
        
        new Thread(new ClientHandler(clientSocket, toClient)).start();
    }

    public enum ClientType
    {
        turnstile,
        client
    }

    private class ClientHandler implements Runnable
    {

        private Socket clientSocket;
        private PrintWriter toClient;
        
        private BufferedReader fromClient;
        private ClientType type;

        public ClientHandler(Socket clientSocket, PrintWriter toClient)
        {
            this.toClient = toClient;
            this.clientSocket = clientSocket;
        }

        @Override
        public void run()
        {
            try
            {
                toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                registerAs();
                
                while (fromClient.read() > -1)
                {
                    String message = null;
                    while ((message = fromClient.readLine()) == null)
                    {
                        Thread.sleep(10);
                    }

                    if (message.toLowerCase().equals("exit"))
                        break;
                    else if (type.equals(ClientType.turnstile))
                        isTurnstile(message);
                    else
                        isClient(message);
                }

                System.out.println("Client terminating...");
                toClient.println("Connection terminating...");
                clientSocket.close();
            }
            catch (IOException | InterruptedException ex)
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void registerAs() throws IOException, InterruptedException
        {
            String input = null;
            while ((input = fromClient.readLine()) == null)
                Thread.sleep(10);
            
            if (input.equals("y"))
            {
                type = ClientType.turnstile;
                toClient.println("You are now a turnstile");
            }
            else if (input.equals("n"))
            {
                type = ClientType.client;
                toClient.println("You are now a regular client");
            }
            else
            {
                toClient.println("Please type y or n");
                registerAs();
            }
        }

        private void isClient(String message)
        {
            if (message.equals("spectators"))
            {
                toClient.println("Number of spectators: " + spectators);
            }
            else
            {
                toClient.println("Please write a valid command('spectators', 'exit')");
            }
        }

        private void isTurnstile(String message)
        {
            if (message.equals("turn"))
            {
                synchronized (this)
                {
                    spectators++;
                }
                toClient.println("Turned once, spectators are now: " + spectators);
            }
            else
            {
                toClient.println("Please write a valid command('turn', 'exit')");
            }
        }
    }
}
