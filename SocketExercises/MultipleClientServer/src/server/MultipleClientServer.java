package server;

import commands.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultipleClientServer
{

    private int port;
    private String serverName;
    
    private Map<String, IServerCommand> commands;
    private Map<Integer, ClientHandler> clients;
    private int idCounter;

    public MultipleClientServer(String serverName, int port)
    {
        this.serverName = serverName;
        this.port = port;

        commands = new HashMap();
        commands.put("TIME", new TimeCommand());
        commands.put("UPPER", new UppercaseCommand());
        commands.put("LOWER", new LowercaseCommand());
        commands.put("REVERSE", new ReverseCommand());
        commands.put("TRANSLATE", new TranslateCommand());
        
        clients = new HashMap();
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
                
                idCounter++;
                clients.put(idCounter, new ClientHandler(idCounter, clientSocket, this));
                new Thread(clients.get(idCounter)).start();
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(MultipleClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeToAllClients(String message)
    {
        for (Integer i : clients.keySet())
        {
            clients.get(i).forwardMessageToClient(message);
        }
    }

    private class ClientHandler implements Runnable
    {

        private int id;
        private Socket clientSocket;
        private MultipleClientServer server;
        
        private PrintWriter toClient;

        public ClientHandler(int id, Socket clientSocket, MultipleClientServer server)
        {
            this.id = id;
            this.clientSocket = clientSocket;
            this.server = server;
        }

        @Override
        public void run()
        {
            try
            {
                toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                while (true)
                {
                    String message = null;
                    while ((message = fromClient.readLine()) == null)
                        Thread.sleep(10);
                    
                    String[] split = message.split("#");
                    IServerCommand command = commands.get(split[0].toUpperCase());
                    if (command != null && split.length > 1)
                    {
                        server.writeToAllClients(command.execute(split[1]));
                    }
                    else
                    {
                        toClient.println("Invalid command, terminating connection...");
                        break;
                    }
                }
                
                System.out.println("Client terminating...");
                clientSocket.close();
                clients.remove(id);
            }
            catch (IOException | InterruptedException ex)
            {
                Logger.getLogger(MultipleClientServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void forwardMessageToClient(String message)
        {
            toClient.println(message);
        }
    }
}
