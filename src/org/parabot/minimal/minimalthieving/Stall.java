package org.parabot.minimal.minimalthieving;

import org.rev317.min.api.methods.Skill;

public enum Stall
{
    FOOD_STALL(4875, 1, 951),
    CRAFTING_STALL(4874, 30, 1636),
    GENERAL_STALL(4876, 60, 1640),
    MAGIC_STALL(4877, 65, 1392),
    SCIMITAR_STALL(4878, 80, 1332);

    @Override
    public String toString()
    {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }

    private int objectId;
    private int level;
    private int itemId;

    Stall(int objectId, int level, int itemId)
    {
        this.objectId = objectId;
        this.level = level;
        this.itemId = itemId;
    }

    /**
     * Gets the objectId of the stall
     * @return objectId
     */
    public int getObjectId()
    {
        return objectId;
    }

    /**
     * Gets the required level of the stall
     * @return level
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Gets the item objectId of the item stolen from the stall
     * @return itemId
     */
    public int getItemId()
    {
        return itemId;
    }

    public static int[] getObjectIds()
    {
        Stall[] stalls = values();
        int[] objectIds = new int[stalls.length];

        for (int i = 0; i < objectIds.length; i++)
        {
            objectIds[i] = stalls[i].getObjectId();
        }

        return objectIds;
    }

    /**
     * Gets all the item ids of the items stolen from the stalls
     * @return itemIds
     */
    public static int[] getItemIds()
    {
        Stall[] stalls = values();
        int[] itemIds = new int[stalls.length];

        for (int i = 0; i < itemIds.length; i++)
        {
            itemIds[i] = stalls[i].getItemId();
        }

        return itemIds;
    }

    /**
     * Gets the best stall based on our thieving level
     * @return stall
     */
    public static Stall getStall()
    {
        int level = Skill.THIEVING.getRealLevel();
        Stall stall = values()[0];

        for (Stall s : values())
        {
            if (level >= s.getLevel())
            {
                stall = s;
            }
        }

        return stall;
    }
}