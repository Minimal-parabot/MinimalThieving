package org.parabot.minimal.minimalthieving;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;

import java.lang.reflect.Field;
import java.util.UUID;

public class CreateAccount implements Strategy
{
    private String randomUsername;

    @Override
    public boolean activate()
    {
        return isBanned();
    }

    @Override
    public void execute()
    {
        MinimalThieving.bans++;

        randomUsername = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 11);

        System.out.println("Your new username is " + randomUsername);

        setUsername();

        setBanned();

        Time.sleep(1000);
    }

    public boolean isBanned()
    {
        try
        {
            Field f = Loader.getClient().getClass().getDeclaredField("cO");
            f.setAccessible(true);

            String message = (String) f.get(Loader.getClient());

            return message.contains("Your account has been disabled");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void setBanned()
    {
        try
        {
            Field f = Loader.getClient().getClass().getDeclaredField("cO");
            f.setAccessible(true);

            f.set(Loader.getClient(), "Resetting cO field");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setUsername()
    {
        try
        {
            Field f = Loader.getClient().getClass().getDeclaredField("it");
            f.setAccessible(true);

            f.set(Loader.getClient(), randomUsername);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}