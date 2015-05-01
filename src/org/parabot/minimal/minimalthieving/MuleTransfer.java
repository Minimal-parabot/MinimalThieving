package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Trading;

public class MuleTransfer implements Strategy
{
    @Override
    public boolean activate()
    {
        return Trading.isOpen();
    }

    @Override
    public void execute()
    {
        if (Trading.isOpen(true))
        {
            Logger.addMessage("Accepting first offer");

            Trading.acceptOffer();

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Trading.isOpen(false);
                }
            }, 5000);
        }

        if (Trading.isOpen(false))
        {
            Logger.addMessage("Accepting trade");

            Trading.acceptTrade();

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Game.getOpenInterfaceId() == -1;
                }
            }, 5000);
        }
    }
}