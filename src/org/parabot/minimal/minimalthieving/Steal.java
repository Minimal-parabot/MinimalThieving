package org.parabot.minimal.minimalthieving;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;

public class Steal implements Strategy
{
    private Stall stall;

    private SceneObject stallObject;

    @Override
    public boolean activate()
    {
        stall = Stall.getStall();

        for (SceneObject so : SceneObjects.getNearest(stall.getId()))
        {
            stallObject = so;

            return true;
        }

        return false;
    }

    @Override
    public void execute()
    {
        if (stallObject != null)
        {
            MinimalThieving.status = "Thieving " + stall;

            stallObject.interact(0);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Players.getMyPlayer().getAnimation() != -1;
                }
            }, 1000);
        }
    }
}