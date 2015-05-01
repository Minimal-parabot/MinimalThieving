package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;

import java.awt.event.KeyEvent;

public class Relog implements Strategy
{
    @Override
    public boolean activate()
    {
        return !Game.isLoggedIn();
    }

    @Override
    public void execute()
    {
        Logger.addMessage("Relogging");

        Keyboard.getInstance().clickKey(KeyEvent.VK_ENTER);

        Time.sleep(new SleepCondition()
        {
            @Override
            public boolean isValid()
            {
                return Game.isLoggedIn();
            }
        }, 3000);

        if (Game.isLoggedIn())
        {
            Logger.addMessage("Waiting after login..");

            Time.sleep(4000);
        }
    }
}