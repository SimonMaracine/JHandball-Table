package windows;

import java.awt.*;
import javax.swing.*;

public class AboutWindow {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        JLabel label;
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.HORIZONTAL;
        }

        label = new JLabel("This is the first version of the app.", SwingConstants.CENTER);
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        label.setPreferredSize(new Dimension(500, 20));
        pane.add(label, c);

        label = new JLabel("Developers: Simon Maracine and Adrian Demian", SwingConstants.CENTER);
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        label.setPreferredSize(new Dimension(500, 50));
        pane.add(label, c);

        label = new JLabel("The application is a simple score table for the game of handball", SwingConstants.CENTER);
        if (shouldWeightX) {
            c.weightx = 0.5;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        label.setPreferredSize(new Dimension(500, 50));
        pane.add(label, c);

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
        pane.add(label, c);


    }


    private static void createAndShowGUI() {
        JFrame frame = new JFrame("About");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
