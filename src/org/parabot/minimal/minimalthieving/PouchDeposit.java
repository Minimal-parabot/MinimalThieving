package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.wrappers.Item;

public class PouchDeposit implements Strategy
{
    private static final int GOLD_PIECES_ID = 996;

    @Override
    public boolean activate()
    {
        return Game.getOpenInterfaceId() == -1 && Inventory.contains(GOLD_PIECES_ID);
    }

    @Override
    public void execute()
    {
        Item coins = Inventory.getItem(GOLD_PIECES_ID);

        if (coins != null)
        {
            Logger.addMessage("Adding " + coins.getStackSize() + " coins to pouch", true);

            Menu.sendAction(493, coins.getId() - 1, coins.getSlot(), 3214);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return !Inventory.contains(GOLD_PIECES_ID);
                }
            }, 1000);
        }
    }
}