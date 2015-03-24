package org.parabot.minimal.minimalthieving;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;

public class Steal implements Strategy
{
    @Override
    public boolean activate()
    {
        return SceneObjects.getNearest(Stall.getIds()).length > 0;
    }

    @Override
    public void execute()
    {
        SceneObject stallObject = SceneObjects.getClosest(Stall.getStallId());

        if (stallObject != null)
        {
            MinimalThieving.status = "Stealing";

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