package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.Npc;

import java.util.ArrayList;
import java.util.Stack;

public class Sell implements Strategy
{
    private static final int[] STOLEN_ITEM_IDS = Stall.getItemIds();

    private static final int BANDIT_LEADER_ID = 1878;

    @Override
    public boolean activate()
    {
        return Inventory.getCount() >= 27;
    }

    @Override
    public void execute()
    {
        if (Inventory.isFull())
        {
            Logger.addMessage("Making room in inventory");

            Inventory.getItems(STOLEN_ITEM_IDS)[0].drop();

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return !Inventory.isFull();
                }
            }, 1500);
        }

        if (!Inventory.isFull())
        {
            if (Game.getOpenInterfaceId() != 3824)
            {
                Npc banditLeader = Npcs.getClosest(BANDIT_LEADER_ID);

                if (banditLeader != null)
                {
                    Logger.addMessage("Trading Bandit leader");

                    banditLeader.interact(Npcs.Option.TALK_TO);

                    Time.sleep(new SleepCondition()
                    {
                        @Override
                        public boolean isValid()
                        {
                            return Game.getOpenInterfaceId() == 3824;
                        }
                    }, 5000);
                }
            }

            if (Game.getOpenInterfaceId() == 3824)
            {
                Logger.addMessage("Selling items");

                do
                {
                    Item item = Inventory.getItems(STOLEN_ITEM_IDS)[0];

                    if (item != null)
                    {
                        Menu.sendAction(431, item.getId() - 1, item.getSlot(), 3823);

                        Time.sleep(250);
                    }
                }
                while (Inventory.contains(STOLEN_ITEM_IDS) && Game.isLoggedIn());
            }
        }
    }
}