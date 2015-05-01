package org.parabot.minimal.minimalthieving;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Keyboard;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.Player;

public class Transfer implements Strategy
{
    private final String MULE_USERNAME;

    public Transfer(final String MULE_USERNAME)
    {
        this.MULE_USERNAME = MULE_USERNAME;
    }

    private Player mule;

    private static final int GOLD_PIECES_ID = 996;
    private static final int FIRST_TRADE_WINDOW = 3323;
    private static final int SECOND_TRADE_WINDOW = 3443;
    private static final int MINIMUM_GOLD_THRESHOLD = 1;

    @Override
    public boolean activate()
    {
        for (Player p : Players.getNearest())
        {
            if (MinimalThieving.secondaryTimer.getRemaining() <= 0
                    && p.getName().equalsIgnoreCase(MULE_USERNAME))
            {
                mule = p;

                return true;
            }
        }

        return Game.getOpenInterfaceId() == FIRST_TRADE_WINDOW || Game.getOpenInterfaceId() == SECOND_TRADE_WINDOW;
    }

    @Override
    public void execute()
    {
        if (MinimalThieving.mode == Mode.MULE)
        {
            if (Game.getOpenInterfaceId() == FIRST_TRADE_WINDOW)
            {
                Menu.sendAction(315, -1, -1, 3420);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return Game.getOpenInterfaceId() == SECOND_TRADE_WINDOW;
                    }
                }, 5000);
            }

            if (Game.getOpenInterfaceId() == SECOND_TRADE_WINDOW)
            {
                Menu.sendAction(315, -1, -1, 3546);

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

        if (MinimalThieving.mode == Mode.BOT)
        {
            if (Inventory.getCount(true, GOLD_PIECES_ID) < MINIMUM_GOLD_THRESHOLD)
            {
                // "Withdraw money pouch"
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

            if (Inventory.getCount(true, GOLD_PIECES_ID) >= MINIMUM_GOLD_THRESHOLD)
            {
                if (Game.getOpenInterfaceId() != FIRST_TRADE_WINDOW || Game.getOpenInterfaceId() != SECOND_TRADE_WINDOW)
                {
                    Menu.sendAction(2027, mule.getIndex(), 0, 0);

                    Time.sleep(new SleepCondition()
                    {
                        @Override
                        public boolean isValid()
                        {
                            return Game.getOpenInterfaceId() == FIRST_TRADE_WINDOW;
                        }
                    }, 10000);
                }

                if (Game.getOpenInterfaceId() == FIRST_TRADE_WINDOW)
                {
                    if (Inventory.contains(GOLD_PIECES_ID))
                    {
                        Item goldPieces = Inventory.getItem(GOLD_PIECES_ID);

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

                    if (!Inventory.contains(GOLD_PIECES_ID))
                    {
                        // IllegalAccessError - LOL?
                        // Trading.acceptOffer();
                        Menu.sendAction(315, -1, -1, 3420);

                        Time.sleep(new SleepCondition()
                        {
                            @Override
                            public boolean isValid()
                            {
                                return Game.getOpenInterfaceId() == SECOND_TRADE_WINDOW;
                            }
                        }, 10000);
                    }
                }

                if (Game.getOpenInterfaceId() == SECOND_TRADE_WINDOW)
                {
                    // Trading.acceptTrade();
                    Menu.sendAction(315, -1, -1, 3546);

                    Time.sleep(new SleepCondition()
                    {
                        @Override
                        public boolean isValid()
                        {
                            return Game.getOpenInterfaceId() == -1;
                        }
                    }, 10000);
                }

                if (Game.getOpenInterfaceId() == -1 && !Inventory.contains(GOLD_PIECES_ID))
                {
                    MinimalThieving.secondaryTimer.restart();
                }
            }
        }
    }
}