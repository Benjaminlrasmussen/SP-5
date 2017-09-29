package observer;

public class MessageObserver implements IMessageObserver
{
    @Override
    public void dataReady(String message)
    {
        System.out.println(message);
    }
}
