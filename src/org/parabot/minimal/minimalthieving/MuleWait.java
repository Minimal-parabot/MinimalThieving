package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.wrappers.Item;

public class MuleWait implements Strategy
{
    private final int interval;

    public MuleWait(int interval)
    {
        this.interval = interval;
    }

    @Override
    public boolean activate()
    {
        return Game.getOpenInterfaceId() == -1;
    }

    @Override
    public void execute()
    {
        Logger.addMessage("Waiting 30 seconds for bots to trade");

        if (Inventory.contains(996))
        {
            Item goldPieces = Inventory.getItem(996);

            if (goldPieces != null)
            {
                Logger.addMessage("Adding " + Inventory.getCount(true, 996) + " coins to money pouch");

                MinimalThieving.moneyGained += Inventory.getCount(true, 996);

                Menu.sendAction(493, goldPieces.getId() - 1, goldPieces.getSlot(), 3214);
            }
        }

        MinimalThieving.secondaryTimer = new Timer(30000);

        Time.sleep(new SleepCondition()
        {
            @Override
            public boolean isValid()
            {
                return Game.getOpenInterfaceId() != -1;
            }
        }, 30000);

        if (Game.getOpenInterfaceId() == -1)
        {
            Logger.addMessage("Waiting interval");

            MinimalThieving.forceLogout();

            int intervalInSeconds = interval * 3600000;

            MinimalThieving.secondaryTimer = new Timer(intervalInSeconds);

            Time.sleep(intervalInSeconds);
        }
    }
}