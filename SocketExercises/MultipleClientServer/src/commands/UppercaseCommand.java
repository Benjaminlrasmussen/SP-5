package commands;

public class UppercaseCommand implements IServerCommand
{

    @Override
    public String execute(String input)
    {
        return input.toUpperCase();
    }

}
