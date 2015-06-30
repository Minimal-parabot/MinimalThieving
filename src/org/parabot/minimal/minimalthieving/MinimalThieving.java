package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;

import javax.imageio.ImageIO;
import java.awt.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

@ScriptManifest(author = "Minimal",
        name = "Minimal Thieving",
        category = Category.THIEVING,
        description = "Steals from the stalls at Edgeville on Ikov and sells the items to the Bandit leader.",
        servers = { "Ikov" },
        version = 2.2)

public class MinimalThieving extends Script implements Paintable, MessageListener
{
    private final ArrayList<Strategy> strategies = new ArrayList<>();

    private final Image IMG = getImage("http://i.imgur.com/TNIuZ47.png");

    private Timer timer = new Timer();

    private int moneyGained;
    private int itemsStolen;
    private int randomsCompleted;

    @Override
    public boolean onExecute()
    {
        strategies.add(new Relog());
        strategies.add(new Teleport());
        strategies.add(new PouchDeposit());
        strategies.add(new Sell());
        strategies.add(new Wait());
        strategies.add(new Steal());

        provide(strategies);
        return true;
    }

    @Override
    public void paint(Graphics g)
    {
        g.setFont(new Font("Helvetica", Font.PLAIN, 14));
        g.setColor(new Color(31, 34, 50));

        g.drawImage(IMG, 546, 209, null);
        g.drawString("Time: " + timer.toString(), 555, 271);
        g.drawString("Money(hr): " + getPerHour(moneyGained), 555, 330);
        g.drawString("Steals(hr): " + getPerHour(itemsStolen), 555, 389);
        g.drawString("Randoms: " + randomsCompleted, 555, 448);
    }

    @Override
    public void messageReceived(MessageEvent m)
    {
        String message = m.getMessage().toLowerCase();

        if (m.getType() == 0)
        {
            if (message.contains("object") || message.contains("not in a")
                || message.contains("is already on") || message.contains("exist"))
            {
                Logger.addMessage("Account is nulled", true);

                forceLogout();
            }
            else if (message.contains("silk"))
            {
                moneyGained += 5120;

                itemsStolen++;
            }
            else if (message.contains("golden ring"))
            {
                moneyGained += 6000;

                itemsStolen++;
            }
            else if (message.contains("emerald ring"))
            {
                moneyGained += 12000;

                itemsStolen++;
            }
            else if (message.contains("battlestaff"))
            {
                moneyGained += 16000;

                itemsStolen++;
            }
            else if (message.contains("adamant scimitar"))
            {
                moneyGained += 20000;

                itemsStolen++;
            }
            else if (message.contains("anti-bot"))
            {
                randomsCompleted++;
            }
        }
    }

    private void forceLogout()
    {
        try
        {
            Method m = Loader.getClient().getClass().getDeclaredMethod("W");
            m.setAccessible(true);

            m.invoke(Loader.getClient());
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    private Image getImage(String str)
    {
        try
        {
            return ImageIO.read(new URL(str));
        }
        catch(Exception e)
        {
            return null;
        }
    }

    private String formatNumber(double number)
    {
        if (number >= 1000 && number < 1000000)
        {
            return new DecimalFormat("#,###.0").format(number / 1000) + "K";
        }
        else if (number >= 1000000 && number < 1000000000)
        {
            return new DecimalFormat("#,###.0").format(number / 1000000) + "M";
        }
        else if (number >= 1000000000)
        {
            return new DecimalFormat("#,###.00").format(number / 1000000000) + "B";
        }

        return "" + number;
    }

    private String getPerHour(int number)
    {
        return formatNumber(number) + "(" + formatNumber(timer.getPerHour(number)) + ")";
    }
}