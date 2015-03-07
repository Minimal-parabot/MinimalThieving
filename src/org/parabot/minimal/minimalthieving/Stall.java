package org.parabot.minimal.minimalthieving;

import org.rev317.min.api.methods.Skill;

public class Stall
{
    public static final Stall FOOD_STALL = new Stall("Food stall", 4875, 1);
    public static final Stall GENERAL_STALL = new Stall("General org.parabot.minimal.minimalthieving.Stall", 4876, 60);
    public static final Stall MAGIC_STALL = new Stall("Magic stall", 4877, 65);
    public static final  Stall SCIMITAR_STALL = new Stall("Scimitar stall", 4878, 80);

    private String name;
    private int id;
    private int level;

    public Stall(String name, int id, int level)
    {
        this.name = name;
        this.id = id;
        this.level = level;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public int getLevel()
    {
        return level;
    }

    public static Stall getBestStall()
    {
        int level = Skill.THIEVING.getLevel();

        if (level < GENERAL_STALL.getLevel())
        {
            return FOOD_STALL;
        }
        else if (level >= GENERAL_STALL.getLevel()
                && level < MAGIC_STALL.getLevel())
        {
            return GENERAL_STALL;
        }
        else if (level >= MAGIC_STALL.getLevel()
                && level < SCIMITAR_STALL.getLevel())
        {
            return MAGIC_STALL;
        }
        else
        {
            return SCIMITAR_STALL;
        }
    }
}