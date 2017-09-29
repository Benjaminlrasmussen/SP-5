package server;

public class Initializer 
{
    public static void main(String[] args)
    {
        Server server = new Server("GreatServer", 444);
        server.start();
    }
}
