package commands;

public class LowercaseCommand implements IServerCommand
{

    @Override
    public String execute(String input)
    {
        return input.toLowerCase();
    }

}
