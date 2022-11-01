package windows;

import other.Logging;

import javax.swing.*;
import java.awt.*;

class AboutWindow extends JFrame {
    AboutWindow() {
        super("About");

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        JLabel label;
        label = new JLabel("This is the first version of the app.", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        label.setPreferredSize(new Dimension(500, 20));
        add(label, c);

        label = new JLabel("Developers: Simon Maracine and Adrian Demian", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        label.setPreferredSize(new Dimension(500, 50));
        add(label, c);

        label = new JLabel("The application is a simple score table for the game of handball", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        label.setPreferredSize(new Dimension(500, 50));
        add(label, c);

        label = new JLabel("<html><center>Features of the app:<br>" +
                "<br>Add teams and players" +
                "<br>View the game timer" +
                "<br>Modify all game stats" +
                "<br>Display a public window for the viewers" +
                "</center></html>", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 3;
        label.setPreferredSize(new Dimension(500, 100));

        add(label, c);
        setLocationRelativeTo(null);
        pack();

        setVisible(true);

        Logging.info("Created about window");
    }
}
