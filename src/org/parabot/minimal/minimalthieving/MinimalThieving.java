package org.parabot.minimal.minimalthieving;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.Player;

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
        version = 2.0)

public class MinimalThieving extends Script implements Paintable, MessageListener
{
    private final ArrayList<Strategy> strategies = new ArrayList<>();

    private final Image IMG = getImage("http://i.imgur.com/TNIuZ47.png");

    private Timer timer = new Timer();
    public static Timer secondaryTimer = new Timer(30000);

    public static Mode mode;

    private String muleUsername;

    public static int moneyGained;
    private int stealCount;
    private int randomCount;

    @Override
    public boolean onExecute()
    {
        MinimalThievingGUI gui = new MinimalThievingGUI();
        gui.setVisible(true);

        int interval;

        while (gui.isVisible())
        {
            sleep(500);
        }

        muleUsername = gui.getMuleUsername();
        interval = gui.getInterval();

        strategies.add(new Relog());
        strategies.add(new Teleport());

        if (mode == Mode.BOT)
        {
            secondaryTimer = new Timer(300000);

            strategies.add(new BotTransfer(muleUsername));
            strategies.add(new Sell());
            strategies.add(new Wait());
            strategies.add(new Steal());
        }
        else if (mode == Mode.MULE)
        {
            secondaryTimer = new Timer(0);

            strategies.add(new MuleWait(interval));
            strategies.add(new MuleTransfer());
        }

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
        g.drawString("Steals(hr): " + getPerHour(stealCount), 555, 389);
        g.drawString("Randoms: " + randomCount, 555, 448);

        if (mode == Mode.BOT)
        {
            if (secondaryTimer.getRemaining() <= 0)
            {
                g.drawString("Available to transfer", 15, 30);
            }
            else
            {
                g.drawString((secondaryTimer.getRemaining() / 1000) + "s until transfer is ready", 15, 30);
            }
            g.drawString("Mule: " + muleUsername, 15, 45);
        }
        else if (mode == Mode.MULE)
        {

            g.drawString("Secondary timer: " + (secondaryTimer.getRemaining() / 1000) + "s", 15, 30);
        }
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
                Logger.addMessage("Account is nulled");

                forceLogout();
            }
            else if (message.contains("silk"))
            {
                moneyGained += 5120;

                stealCount++;
            }
            else if (message.contains("golden ring"))
            {
                moneyGained += 6000;

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

        if (m.getType() == 4)
        {
            tradePlayer(m.getSender());
        }
    }

    private void tradePlayer(String username)
    {
        for (Player p : Players.getNearest())
        {
            if (p.getName().equalsIgnoreCase(username))
            {
                p.interact(Players.Option.TRADE);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return Game.getOpenInterfaceId() == 3323
                                || Game.getOpenInterfaceId() == 3443;
                    }
                }, 5000);

                Time.sleep(2000);
            }
        }
    }

    public static void forceLogout()
    {
        try
        {
            Class<?> c = Loader.getClient().getClass();

            Method m = c.getDeclaredMethod("am");
            m.setAccessible(true);

            m.invoke(Loader.getClient());
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
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
        catch(IOException e)
        {
            return null;
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