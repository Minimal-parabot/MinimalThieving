package org.parabot.minimal.minimalthieving;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.methods.*;

public class Teleport implements Strategy
{
    private final int BANDIT_LEADER_ID = 1878;

    @Override
    public boolean activate()
    {
        return Npcs.getNearest(BANDIT_LEADER_ID).length == 0;
    }

    @Override
    public void execute()
    {
        MinimalThieving.status = "Teleporting back to stalls";

//        System.out.println("Message: " + Loader.getClient().getInterfaceCache()[372].getMessage());

        if (Game.getOpenBackDialogId() != 2459)
        {
            Menu.clickButton(1195);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Game.getOpenBackDialogId() == 2459;
                }
            }, 2500);
        }

        if (Game.getOpenBackDialogId() == 2459)
        {
            Menu.sendAction(315, -1, -1, 2461);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Players.getMyPlayer().getAnimation() == -1
                            && Npcs.getNearest(BANDIT_LEADER_ID).length > 0;
                }
            }, 5000);
        }
    }
}