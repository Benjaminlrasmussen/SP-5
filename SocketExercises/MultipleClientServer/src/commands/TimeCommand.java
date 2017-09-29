package commands;

import java.util.Date;

public class TimeCommand implements IServerCommand
{

    @Override
    public String execute(String input)
    {
        return new Date().toString();
    }

}
