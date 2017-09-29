package commands;

public class ReverseCommand implements IServerCommand
{

    @Override
    public String execute(String input)
    {
        return new StringBuilder(input).reverse().toString();
    }

}
