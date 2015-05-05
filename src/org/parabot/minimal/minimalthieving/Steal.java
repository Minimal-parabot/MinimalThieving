package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;

public class Steal implements Strategy
{
    private Stall stall = null;

    @Override
    public boolean activate()
    {
        stall = Stall.getStall();

        return SceneObjects.getNearest(stall.getObjectId()).length > 0;
    }

    @Override
    public void execute()
    {
        SceneObject stallObject = SceneObjects.getClosest(stall.getObjectId());

        if (stallObject != null)
        {
            Logger.addMessage("Stealing from " + stall);

            stallObject.interact(SceneObjects.Option.STEAL_FROM);

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