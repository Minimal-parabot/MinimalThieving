package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.Player;

public class BotTransfer implements Strategy
{
    private final String muleUsername;

    public BotTransfer(String muleUsername)
    {
        this.muleUsername = muleUsername;
    }

    private Player mule;

    private static final int GOLD_PIECES_ID = 996;
    private static final int GOLD_THRESHOLD = 1000000;

    @Override
    public boolean activate()
    {
        try
        {
            for (Player p : Players.getNearest())
            {
                if (p.getName().equalsIgnoreCase(muleUsername)
                        && MinimalThieving.secondaryTimer.getRemaining() <= 0)
                {
                    mule = p;

                    return true;
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();

            Logger.addMessage("Error looping through Players");
        }

        return false;
    }

    @Override
    public void execute()
    {
        if (Inventory.getCount(true, GOLD_PIECES_ID) < GOLD_THRESHOLD)
        {
            Logger.addMessage("Inventory gold (" + Inventory.getCount(true, GOLD_PIECES_ID) + ") is below threshold");
            Logger.addMessage("Withdrawing gold from money pouch");

            Menu.sendAction(713, -1, -1, 60);

            Time.sleep(500);

            Keyboard.getInstance().sendKeys("2b");

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Inventory.contains(GOLD_PIECES_ID);
                }
            }, 1500);
        }

        if (Inventory.getCount(true, GOLD_PIECES_ID) >= GOLD_THRESHOLD)
        {
            if (!Trading.isOpen())
            {
                Logger.addMessage("Trading " + mule.getName());

                mule.interact(Players.Option.TRADE);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return Trading.isOpen();
                    }
                }, 10000);
            }

            if (Trading.isOpen(true))
            {
                if (Inventory.contains(GOLD_PIECES_ID))
                {
                    Item goldPieces = Inventory.getItem(GOLD_PIECES_ID);

                    if (goldPieces != null)
                    {
                        Logger.addMessage("Putting gold into trade window");

                        Menu.sendAction(431, goldPieces.getId() - 1, goldPieces.getSlot(), 3322);

                        Time.sleep(new SleepCondition()
                        {
                            @Override
                            public boolean isValid()
                            {
                                return !Inventory.contains(GOLD_PIECES_ID);
                            }
                        }, 1500);
                    }
                }

                if (!Inventory.contains(GOLD_PIECES_ID))
                {
                    Logger.addMessage("Accepting offer");

                    Trading.acceptOffer();

                    Time.sleep(new SleepCondition()
                    {
                        @Override
                        public boolean isValid()
                        {
                            return Trading.isOpen(false);
                        }
                    }, 10000);
                }
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
                }, 10000);

                if (Game.getOpenInterfaceId() == -1 && !Inventory.contains(GOLD_PIECES_ID))
                {
                    Logger.addMessage("Restarting secondary timer");

                    MinimalThieving.secondaryTimer.restart();

                    Time.sleep(5000);
                }
            }
        }
    }
}