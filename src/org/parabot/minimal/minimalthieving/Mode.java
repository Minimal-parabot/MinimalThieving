package org.parabot.minimal.minimalthieving;

public enum Mode
{
    BOT(0),
    MULE(1);

    @Override
    public String toString()
    {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }

    int modeId;

    Mode(int modeId)
    {
        this.modeId = modeId;
    }
}