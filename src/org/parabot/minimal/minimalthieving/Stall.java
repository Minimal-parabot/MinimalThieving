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

    /**
     * Gets the id of the stall
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Gets the required level of the stall
     * @return level
     */
    public int getLevel()
    {
        return level;
    }

    public static Stall getStall()
    {
        Stall stall = null;

        int level = Skill.THIEVING.getRealLevel();

        for (Stall s : values())
        {
            if (level >= s.level)
            {
                stall = s;
            }
            else
            {
                break;
            }
        }

        return stall;
    }

    /**
     * Gets the best stall to thieve
     * @return stall
     */
    public static int getStallId()
    {
        int level = Skill.THIEVING.getRealLevel();
        int stallId = 0;

        for (Stall s : values())
        {
            if (level >= s.level)
            {
                stallId = s.id;
            }
            else
            {
                break;
            }
        }

        return stallId;
    }

    /**
     * Gets the object ids of all stalls
     * @return stallIds
     */
    public static int[] getIds()
    {
        int[] stallIds = new int[values().length];

        for (int i = 0; i < values().length; i++)
        {
            stallIds[i] = values()[i].getId();
        }

        return stallIds;
    }
}