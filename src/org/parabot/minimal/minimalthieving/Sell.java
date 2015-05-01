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
    private static final int[] STOLEN_ITEMS = Stall.getItemIds();

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

            Inventory.getItems(STOLEN_ITEMS)[0].drop();

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

                sellAllExcept(996);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return Inventory.getCount() == 1;
                    }
                }, 1500);
            }
        }
    }

    /**
     * Sells all items except the ones we specify in the parameter
     * @param ids the ids of the items we don't want to sell
     */
    public void sellAllExcept(int... ids)
    {
        ArrayList<Integer> ignored = new ArrayList<>();
        Stack<Integer> itemsToDeposit = new Stack<>();

        for (int i : ids)
        {
            ignored.add(i);
        }

        for (Item i : Inventory.getItems())
        {
            if (!ignored.contains(i.getId())
                    && !itemsToDeposit.contains(i.getId()))
            {
                itemsToDeposit.push(i.getId());
            }
        }

        while (!itemsToDeposit.isEmpty()
                && Game.isLoggedIn()
                && Game.getOpenInterfaceId() == 3824)
        {
            int itemId = itemsToDeposit.pop();

            if (Inventory.getItems(itemId).length > 0)
            {
                Item item = Inventory.getItems(itemId)[Inventory.getItems(itemId).length - 1];

                while (Inventory.getItems(itemId).length > 0
                        && Game.isLoggedIn())
                {
                    Menu.sendAction(431, item.getId() - 1, item.getSlot(), 3823);
                    Time.sleep(500);
                }
            }
        }
    }
}