package windows;

import handball.Match;
import handball.Player;
import handball.Team;
import other.Logging;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;

class InitializeWindow extends JFrame {
    private static final int MIN_WIDTH = 1040;
    private static final int MIN_HEIGHT = 585;

    private final JPanel pnlMain = new JPanel(new GridBagLayout());

    private final JTextField txtLeftTeamName = new JTextField();
    private final JTextField txtRightTeamName = new JTextField();

    private final JTextField[] txtLeftTeamPlayerNames = new JTextField[7];
    private final JTextField[] txtRightTeamPlayerNames = new JTextField[7];

    private final JTextField[] txtLeftTeamPlayerNumbers = new JTextField[7];
    private final JTextField[] txtRightTeamPlayerNumbers = new JTextField[7];

    private final JButton btnApply = new JButton("Apply");
    private final JButton btnCancel = new JButton("Cancel");

    private final Application application;

    InitializeWindow(Application application) {
        super("Initialize Match");

        this.application = application;

        setupLayout();

        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setVisible(true);

        Logging.info("Created initialize window");
    }

    private void setupLayout() {
        var pnlTeams = new JPanel(new GridBagLayout());
        var pnlLeftTeam = new JPanel(new GridBagLayout());
        var pnlRightTeam = new JPanel(new GridBagLayout());
        var pnlConfigure = new JPanel(new GridBagLayout());

        var constraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, new Insets(5, 5, 5, 5), 0, 0);

        for (int i = 0; i < 7; i++) {
            txtLeftTeamPlayerNames[i] = new JTextField(12);
            txtRightTeamPlayerNames[i] = new JTextField(12);
            txtLeftTeamPlayerNumbers[i] = new JTextField(1);
            txtRightTeamPlayerNumbers[i] = new JTextField(1);
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
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        pnlConfigure.add(btnApply, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        pnlConfigure.add(btnCancel, constraints);

        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.ipadx = 0;
        constraints.ipady = 0;

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

        // TODO temporary; delete this later!
        var tempButton = new JButton("Click Me");
        tempButton.addActionListener(actionEvent -> __fillEntries());
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        pnlConfigure.add(tempButton, constraints);

        add(pnlMain);
        pack();
    }

    private void showEmptyPopup(String parameter) {
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, parameter + " cannot be empty!");
    }

    private void showNumberPopup(int team, int number) {
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, "Only digits are accepted for team " + team + ", number " + number + "!");
    }

    private void showNumberBoundPopup(int team, int number) {
        JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(
            jFrame, "Only numbers between 1 and 99 are accepted for team " + team + ", number " + number + "!"
        );
    }

    private void apply() {
        // Check input first
        if (txtLeftTeamName.getText().length() == 0) {
            showEmptyPopup("Team 1");
            return;
        }
        for (int i = 0; i < 7; i++) {
            if (txtLeftTeamPlayerNames[i].getText().length() == 0) {
                showEmptyPopup("Team 1, name " + (i + 1));
                return;
            }
            if (txtLeftTeamPlayerNumbers[i].getText().length() == 0) {
                showEmptyPopup("Team 1, number " + (i + 1));
                return;
            }
            if (!txtLeftTeamPlayerNumbers[i].getText().matches("[0-9]+")) {
                showNumberPopup(1, i + 1);
                return;
            }

            final int playerNumber = Integer.parseInt(txtLeftTeamPlayerNumbers[i].getText());
            if (playerNumber < 1 || playerNumber > 99) {
                showNumberBoundPopup(1, i + 1);
                return;
            }
        }

        if (txtRightTeamName.getText().length() == 0) {
            showEmptyPopup("Team 2");
            return;
        }
        for (int i = 0; i < 7; i++) {
            if (txtRightTeamPlayerNames[i].getText().length() == 0) {
                showEmptyPopup("Team 2, name " + (i + 1));
                return;
            }
            if (txtRightTeamPlayerNumbers[i].getText().length() == 0) {
                showEmptyPopup("Team 2, number " + (i + 1));
                return;
            }
            if (!txtRightTeamPlayerNumbers[i].getText().matches("[0-9]+")) {
                showNumberPopup(2, i + 1);
                return;
            }

            final int playerNumber = Integer.parseInt(txtRightTeamPlayerNumbers[i].getText());
            if (playerNumber < 1 || playerNumber > 99) {
                showNumberBoundPopup(2, i + 1);
                return;
            }
        }

        // Create objects
        Team leftTeam = new Team(txtLeftTeamName.getText());
        Team rightTeam = new Team(txtRightTeamName.getText());

        for (int i = 0; i < 7; i++) {
            leftTeam.getPlayers()[i] = new Player(
                txtLeftTeamPlayerNames[i].getText(),
                Integer.parseInt(txtLeftTeamPlayerNumbers[i].getText()),
                leftTeam
            );

            rightTeam.getPlayers()[i] = new Player(
                txtRightTeamPlayerNames[i].getText(),
                Integer.parseInt(txtRightTeamPlayerNumbers[i].getText()),
                rightTeam
            );
        }

        application.match = new Match(leftTeam, rightTeam, new Date());
        application.initializeTeams();

        dispose();

        Logging.info("Created a new match");
    }

    private void cancel() {
        dispose();

        Logging.info("Cancelled initialize window");
    }

    // TODO temporary; delete this later!
    private void __fillEntries() {
        txtLeftTeamName.setText("Losers");
        txtRightTeamName.setText("Players");

        txtLeftTeamPlayerNames[0].setText("Simon");
        txtLeftTeamPlayerNames[1].setText("Theodore");
        txtLeftTeamPlayerNames[2].setText("Alex");
        txtLeftTeamPlayerNames[3].setText("Jane");
        txtLeftTeamPlayerNames[4].setText("Pal");
        txtLeftTeamPlayerNames[5].setText("Hmm");
        txtLeftTeamPlayerNames[6].setText("Dory");

        txtLeftTeamPlayerNumbers[0].setText("20");
        txtLeftTeamPlayerNumbers[1].setText("24");
        txtLeftTeamPlayerNumbers[2].setText("81");
        txtLeftTeamPlayerNumbers[3].setText("1");
        txtLeftTeamPlayerNumbers[4].setText("89");
        txtLeftTeamPlayerNumbers[5].setText("13");
        txtLeftTeamPlayerNumbers[6].setText("43");

        txtRightTeamPlayerNames[0].setText("Adrian");
        txtRightTeamPlayerNames[1].setText("Paul");
        txtRightTeamPlayerNames[2].setText("Sandy");
        txtRightTeamPlayerNames[3].setText("Jordan");
        txtRightTeamPlayerNames[4].setText("Qwerty");
        txtRightTeamPlayerNames[5].setText("Hello");
        txtRightTeamPlayerNames[6].setText("World");

        txtRightTeamPlayerNumbers[0].setText("93");
        txtRightTeamPlayerNumbers[1].setText("76");
        txtRightTeamPlayerNumbers[2].setText("10");
        txtRightTeamPlayerNumbers[3].setText("50");
        txtRightTeamPlayerNumbers[4].setText("11");
        txtRightTeamPlayerNumbers[5].setText("66");
        txtRightTeamPlayerNumbers[6].setText("48");
    }
}
