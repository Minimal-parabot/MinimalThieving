package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.parabot.minimal.minimalthieving.MinimalThieving;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.wrappers.Item;

public class Wait implements Strategy
{
    @Override
    public boolean activate()
    {
        return Players.getMyPlayer().getAnimation() != -1;
    }

    @Override
    public void execute()
    {
        if (Inventory.contains(996))
        {
            Logger.addMessage("Adding coins to pouch");

            Item coins = Inventory.getItem(996);

            if (coins != null)
            {
                Menu.sendAction(493, coins.getId() - 1, coins.getSlot(), 3214);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return Inventory.getCount(996) == 0;
                    }
                }, 1000);
            }
        }

        Logger.addMessage("Waiting");

        Time.sleep(new SleepCondition()
        {
            @Override
            public boolean isValid()
            {
                return Players.getMyPlayer().getAnimation() == -1;
            }
        }, 1000);

        Time.sleep(500);
    }
}