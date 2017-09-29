package server;

public class Initializer 
{
    public static void main(String[] args)
    {
        MultipleClientServer server = new MultipleClientServer("GreatServer", 444);
        server.start();
    }
}
