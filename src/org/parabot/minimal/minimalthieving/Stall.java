package org.parabot.minimal.minimalthieving;

import org.rev317.min.api.methods.Skill;

public enum Stall
{
    FOOD_STALL(4875, 1),
    GENERAL_STALL(4876, 60),
    MAGIC_STALL(4877, 65),
    SCIMITAR_STALL(4878, 80);

    @Override
    public String toString()
    {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }

    private int id;
    private int level;

    Stall(int id, int level)
    {
        this.id = id;
        this.level = level;
    }

    public int getId()
    {
        return id;
    }

    public int getLevel()
    {
        return level;
    }

    public static Stall getStall()
    {
        Stall stall = null;
        int level = Skill.THIEVING.getRealLevel();

        for (Stall s : Stall.values())
        {
            if (level >= s.getLevel())
                stall = s;
            else
                break;
        }

        return stall;
    }
}