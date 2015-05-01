package org.parabot.minimal.minimalthieving;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinimalThievingGUI extends JFrame
{
    private JPanel botPanel;
    private JPanel mulePanel;

    private JSpinner intervalSpinner;

    private JTextField usernameField;

    private static final Font FONT = new Font("Helvetica", Font.PLAIN, 14);

    public MinimalThievingGUI()
    {
        setSize(200, 200);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        JPanel generalPanel = new GeneralPanel();
        c.gridx = 0;
        c.gridy = 0;
        add(generalPanel, c);

        botPanel = new BotPanel();
        botPanel.setVisible(false);
        c.gridx = 0;
        c.gridy = 1;
        add(botPanel, c);

        mulePanel = new MulePanel();
        mulePanel.setVisible(false);
        c.gridx = 0;
        c.gridy = 1;
        add(mulePanel, c);

        JPanel buttonPanel = new ButtonPanel();
        c.gridx = 0;
        c.gridy = 2;
        add(buttonPanel, c);

//        add(botPanel);
//        add(buttonPanel);
    }

    public class GeneralPanel extends JPanel
    {
        public GeneralPanel()
        {
            setBorder(BorderFactory.createTitledBorder("Account Type"));

            ButtonGroup bg = new ButtonGroup();

            final JRadioButton botButton = new JRadioButton("Bot");
            botButton.setFont(FONT);
            botButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (botButton.isSelected())
                    {
                        botPanel.setVisible(true);
                        mulePanel.setVisible(false);

                        MinimalThieving.mode = Mode.BOT;
                    }
                }
            });

            final JRadioButton muleButton = new JRadioButton("Mule");
            muleButton.setFont(FONT);
            muleButton.setEnabled(false);
            muleButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (muleButton.isSelected())
                    {
                        botPanel.setVisible(false);
                        mulePanel.setVisible(true);

                        MinimalThieving.mode = Mode.MULE;
                    }
                }
            });

            bg.add(botButton);
            bg.add(muleButton);

            add(botButton);
            add(muleButton);
        }
    }

    public class BotPanel extends JPanel
    {
        public BotPanel()
        {
            setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();

            JLabel usernameLabel = new JLabel("Mule Username: ");
            usernameLabel.setFont(FONT);
            c.gridx = 0;
            c.gridy = 0;
            add(usernameLabel, c);

            usernameField = new JTextField(10);
            usernameField.setFont(FONT);
            usernameField.setEnabled(false);
            c.gridx = 0;
            c.gridy = 1;
            add(usernameField, c);
        }
    }

    public class MulePanel extends JPanel
    {
        public MulePanel()
        {
            setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();

            JLabel spinnerLabel = new JLabel("Interval (hrs): ");
            spinnerLabel.setFont(FONT);
            c.gridx = 0;
            c.gridy = 0;
            add(spinnerLabel, c);

            // Come on, Java -- why is it this tedious to set columns?
            intervalSpinner = new JSpinner();
            ((JSpinner.DefaultEditor) intervalSpinner.getEditor()).getTextField().setColumns(2);
            intervalSpinner.setValue(6);
            c.gridx = 0;
            c.gridy = 1;

            add(intervalSpinner, c);
        }
    }

    public class ButtonPanel extends JPanel
    {
        public ButtonPanel()
        {
            JButton startButton = new JButton("Start");
            startButton.setFont(FONT);
            startButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    dispose();
                }
            });

            add(startButton);
        }
    }

    public String getMuleUsername()
    {
        return usernameField.getText();
    }

    public int getInterval()
    {
        return (int) intervalSpinner.getValue();
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                MinimalThievingGUI gui = new MinimalThievingGUI();

                gui.setVisible(true);
            }
        });
    }
}