package org.parabot.minimal.minimalthieving;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.parabot.minimal.minimalthieving.MinimalThieving;
import org.rev317.min.Loader;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

public class Relog implements Strategy
{
    @Override
    public boolean activate()
    {
        return !Loader.getClient().isLoggedIn();
    }

    @Override
    public void execute()
    {
        if (!Loader.getClient().isLoggedIn())
        {
            MinimalThieving.status = "Logging in";

            System.out.println("[" + MinimalThieving.timer.toString() + "]: Attempting to relog");

            Time.sleep(5000);

            Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);

            MinimalThieving.status = "Waiting before login";

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Loader.getClient().isLoggedIn();
                }
            }, 5000);

            if (Loader.getClient().isLoggedIn())
            {
                MinimalThieving.status = "Waiting after login";

                Time.sleep(4000);
            }
        }
    }
}