package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.*;

public class Teleport implements Strategy
{
    private static final int[] STALL_OBJECT_IDS = Stall.getObjectIds();

    @Override
    public boolean activate()
    {
        return SceneObjects.getNearest(STALL_OBJECT_IDS).length == 0;
    }

    @Override
    public void execute()
    {
        Logger.addMessage("Teleporting back to stalls");

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
                            && SceneObjects.getNearest(STALL_OBJECT_IDS).length > 0;
                }
            }, 5000);
        }
    }
}