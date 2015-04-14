package org.parabot.minimal.minimalthieving;

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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

@ScriptManifest(author = "Minimal",
        category = Category.THIEVING,
        description = "Steals from the stalls at Edgeville in Ikov and sells the items to the Bandit leader.",
        name = "Minimal Thieving",
        servers = { "Ikov" },
        version = 1.9)

public class MinimalThieving extends Script implements Paintable, MessageListener
{
    private final ArrayList<Strategy> strategies = new ArrayList<>();

    private final Image IMG = getImage("http://i.imgur.com/TNIuZ47.png");

    private Timer timer;

    public static String status = "";

    private int moneyGained;
    private int stealCount;
    private int randomCount;

    @Override
    public boolean onExecute()
    {
        timer = new Timer();

        strategies.add(new Relog());
        strategies.add(new Teleport());
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
        g.drawString(status, 15, 15);
        g.drawString("Time: " + timer.toString(), 555, 271);
        g.drawString("Money(hr): " + getPerHour(moneyGained), 555, 330);
        g.drawString("Steals(hr): " + getPerHour(stealCount), 555, 389);
        g.drawString("Randoms: " + randomCount, 555, 448);
    }

    @Override
    public void messageReceived(MessageEvent m)
    {
        String message = m.getMessage().toLowerCase();

        if (m.getType() == 0)
        {
            if (message.contains("object"))
            {
                status = "Nulled";

                forceLogout();
            }
            else if (message.contains("silk"))
            {
                moneyGained += 5120;

                stealCount++;
            }
            else if (message.contains("golden ring"))
            {
                moneyGained += 80;

                stealCount++;
            }
            else if (message.contains("emerald ring"))
            {
                moneyGained += 12000;

                stealCount++;
            }
            else if (message.contains("battlestaff"))
            {
                moneyGained += 16000;

                stealCount++;
            }
            else if (message.contains("adamant scimitar"))
            {
                moneyGained += 20000;

                stealCount++;
            }
            else if (message.contains("anti-bot"))
            {
                randomCount++;
            }
        }
    }

    private Image getImage(String str)
    {
        try
        {
            return ImageIO.read(new URL(str));
        }
        catch(IOException e)
        {
            return null;
        }
    }

    // Forces the user to log out
    private void forceLogout()
    {
        try
        {
            Class<?> c = Loader.getClient().getClass();
            Method m = c.getDeclaredMethod("am");
            m.setAccessible(true);
            m.invoke(Loader.getClient());
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    private String formatNumber(double number)
    {
        DecimalFormat normal = new DecimalFormat("#,###.0");
        DecimalFormat goldFarmer = new DecimalFormat("#,###.00");

        if (number >= 1000 && number < 1000000)
        {
            return normal.format(number / 1000) + "K";
        }
        else if (number >= 1000000 && number < 1000000000)
        {
            return normal.format(number / 1000000) + "M";
        }
        else if (number >= 1000000000)
        {
            return goldFarmer.format(number / 1000000000) + "B";
        }

        return "" + number;
    }

    private String getPerHour(int number)
    {
        return formatNumber(number) + "(" + formatNumber(timer.getPerHour(number)) + ")";
    }
}