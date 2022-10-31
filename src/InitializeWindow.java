import handball.Match;
import handball.Player;
import handball.Team;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;

class InitializeWindow extends JFrame {
    private static final int MIN_WIDTH = 1024;
    private static final int MIN_HEIGHT = 576;

    private final JPanel pnlMain = new JPanel(new GridBagLayout());

    private final JTextField txtLeftTeamName = new JTextField();
    private final JTextField txtRightTeamName = new JTextField();

    private final JTextField[] txtLeftTeamPlayerNames = new JTextField[7];
    private final JTextField[] txtRightTeamPlayerNames = new JTextField[7];

    private final JTextField[] txtLeftTeamPlayerNumbers = new JTextField[7];
    private final JTextField[] txtRightTeamPlayerNumbers = new JTextField[7];

    private final JButton btnApply = new JButton("Apply");
    private final JButton btnCancel = new JButton("Cancel");

    InitializeWindow() {
        super("JHandball Table");

        setupLayout();

        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setVisible(true);

        Logging.info("Initialized initialize window");
    }

    private void setupLayout() {
        var pnlTeams = new JPanel(new GridBagLayout());
        var pnlLeftTeam = new JPanel(new GridBagLayout());
        var pnlRightTeam = new JPanel(new GridBagLayout());
        var pnlConfigure = new JPanel(new GridBagLayout());

        var constraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, new Insets(5, 5, 5, 5), 0, 0);

        for (int i = 0; i < 7; i++) {
            txtLeftTeamPlayerNames[i] = new JTextField();
            txtRightTeamPlayerNames[i] = new JTextField();
            txtLeftTeamPlayerNumbers[i] = new JTextField();
            txtRightTeamPlayerNumbers[i] = new JTextField();
        }

        // Left team
        constraints.gridx = 0;
        constraints.gridy = 0;
        pnlLeftTeam.add(new JLabel("Team 1"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        pnlLeftTeam.add(txtLeftTeamName, constraints);

        constraints.gridwidth = 1;

        for (int i = 0; i < 7; i++) {
            constraints.gridx = 0;
            constraints.gridy = i + 1;
            pnlLeftTeam.add(new JLabel(String.valueOf(i + 1)), constraints);

            constraints.gridx = 1;
            constraints.gridy = i + 1;
            pnlLeftTeam.add(txtLeftTeamPlayerNames[i], constraints);

            constraints.gridx = 2;
            constraints.gridy = i + 1;
            pnlLeftTeam.add(txtLeftTeamPlayerNumbers[i], constraints);
        }

        // Right team
        constraints.gridx = 0;
        constraints.gridy = 0;
        pnlRightTeam.add(new JLabel("Team 2"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        pnlRightTeam.add(txtRightTeamName, constraints);

        constraints.gridwidth = 1;

        for (int i = 0; i < 7; i++) {
            constraints.gridx = 0;
            constraints.gridy = i + 1;
            pnlRightTeam.add(new JLabel(String.valueOf(i + 1)), constraints);

            constraints.gridx = 1;
            constraints.gridy = i + 1;
            pnlRightTeam.add(txtRightTeamPlayerNames[i], constraints);

            constraints.gridx = 2;
            constraints.gridy = i + 1;
            pnlRightTeam.add(txtRightTeamPlayerNumbers[i], constraints);
        }

        // Buttons
        constraints.gridx = 0;
        constraints.gridy = 0;
        pnlConfigure.add(btnApply, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        pnlConfigure.add(btnCancel, constraints);

        // Put everything together
        constraints.gridx = 0;
        constraints.gridy = 0;
        pnlTeams.add(pnlLeftTeam, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        pnlTeams.add(pnlRightTeam, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        pnlMain.add(pnlTeams, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.2;
        pnlMain.add(pnlConfigure, constraints);

        btnApply.addActionListener(actionEvent -> apply());
        btnCancel.addActionListener(actionEvent -> cancel());

        add(pnlMain);
        pack();
    }

    private void apply() {
        // TODO process input, create two Team objects and a Match object

        Team leftTeam = new Team(txtLeftTeamName.getText());

        for (int i = 0; i < 7; i++) {
            Player player = new Player(
                txtLeftTeamPlayerNames[i].getText(),
                Integer.parseInt(txtLeftTeamPlayerNumbers[i].getText()),
                leftTeam
            );

            leftTeam.getPlayers()[i] = player;
        }




        Team rightTeam = new Team("Losers");

//        Team leftTeam = new Team("Losers");
//        leftTeam.getPlayers()[0] = new Player("Simon", 8, leftTeam);
//
//        Team rightTeam = new Team("Happies");
//
//        Match match = new Match(leftTeam, rightTeam, new Date());
    }

    private void cancel() {
        dispose();

        Logging.info("Cancelled initialize window");
    }
}
