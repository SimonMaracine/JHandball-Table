package windows;

import javax.swing.*;
import java.awt.*;

import handball.Match;
import handball.Player;
import other.Logging;
import timer.Timer;
import timer.TimerException;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

public class Application extends JFrame {
    private static final int MIN_WIDTH = 1120;
    private static final int MIN_HEIGHT = 630;

    private static final Font TEAM_NAME_FONT = new Font("Monospaced", Font.PLAIN, 26);  // TODO choose fallback fonts
    private static final Font TEAM_SCORE_FONT = new Font("Monospaced", Font.PLAIN, 60);
    private static final Font TIMER_FONT = new Font("Monospaced", Font.PLAIN, 80);
    private static final Font PLAYER_DATA_FONT = new Font("Monospaced", Font.PLAIN, 24);

    private final JPanel pnlMain = new JPanel(new GridBagLayout());

    private final JLabel lblLeftTeamName = new JLabel("Team 1");
    private final JLabel lblRightTeamName = new JLabel("Team 2");
    private final JLabel lblLeftTeamScore = new JLabel("0");
    private final JLabel lblRightTeamScore = new JLabel("0");

    private final DefaultListModel<String> leftTeamPlayers = new DefaultListModel<>();
    private final DefaultListModel<String> rightTeamPlayers = new DefaultListModel<>();

    private final JList<String> lstLeftTeamPlayers = new JList<>(leftTeamPlayers);
    private final JList<String> lstRightTeamPlayers = new JList<>(rightTeamPlayers);

    private final JPanel pnlSuspendedPlayers = new JPanel();
    private final JLabel lblTimer = new JLabel("00:00");
    private final JLabel lblSelectedPlayerNameNumber = new JLabel("n/a");
    private final JLabel lblSelectedPlayerScore = new JLabel("n/a");
    private final JLabel lblSelectedPlayerHasYellowCard = new JLabel("n/a");
    private final JLabel lblSelectedPlayerHasRedCard = new JLabel("n/a");
    private final JLabel lblSelectedPlayerIsSuspended = new JLabel("n/a");

    private Timer matchTimer = null;
    Match match = null;
    private Player selectedPlayer = null;

    public Application() {
        super("JHandball Table");

        setupLayout();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setVisible(true);

        Logging.info("Created main window");
    }

    void initializeTeams() {
        assert match != null;

        lblLeftTeamName.setText(match.getLeftTeam().getName());
        lblRightTeamName.setText(match.getRightTeam().getName());

        leftTeamPlayers.clear();
        rightTeamPlayers.clear();

        for (int i = 0; i < match.getLeftTeam().getPlayers().length; i++) {
            final Player player = match.getLeftTeam().getPlayers()[i];
            final String entry = player.getName() + "[" + player.getNumber() + "]";

            leftTeamPlayers.add(i, entry);
        }

        for (int i = 0; i < match.getRightTeam().getPlayers().length; i++) {
            final Player player = match.getRightTeam().getPlayers()[i];
            final String entry = player.getName() + "[" + player.getNumber() + "]";

            rightTeamPlayers.add(i, entry);
        }
    }

    private void setupLayout() {
        setupMenuBar();
        setupTeams();
        setupTimer();
        setupPlayerOptions();

        add(pnlMain);
        pack();
    }

    private void setupMenuBar() {
        var menMenuBar = new JMenuBar();

        var menMatch = new JMenu("Match");
        var menPlayer = new JMenu("Player");
        var menHelp = new JMenu("Help");

        JMenuItem menItem;

        menItem = new JMenuItem("Initialize");
        menItem.addActionListener(actionEvent -> initializeMatch());
        menMatch.add(menItem);

        menItem = new JMenuItem("Begin");
        menItem.addActionListener(actionEvent -> beginMatch());
        menMatch.add(menItem);

        menItem = new JMenuItem("End");
        menItem.addActionListener(actionEvent -> endMatch());
        menMatch.add(menItem);

        menItem = new JMenuItem("Reset");
        menItem.addActionListener(actionEvent -> resetMatch());
        menMatch.add(menItem);

        menItem = new JMenuItem("Show Public Window");
        menItem.addActionListener(actionEvent -> showPublicWindow());
        menMatch.add(menItem);

        menItem = new JMenuItem("Score Down");
        menItem.addActionListener(actionEvent -> scoreDownPlayer());
        menPlayer.add(menItem);

        menItem = new JMenuItem("Release");
        menItem.addActionListener(actionEvent -> releasePlayer());
        menPlayer.add(menItem);

        menItem = new JMenuItem("Take Yellow Card");
        menItem.addActionListener(actionEvent -> takeYellowCardPlayer());
        menPlayer.add(menItem);

        menItem = new JMenuItem("Take Red Card");
        menItem.addActionListener(actionEvent -> takeRedCardPlayer());
        menPlayer.add(menItem);

<<<<<<< HEAD
        menHelp.add(new JMenuItem("About"));
        menItem.addActionListener(actionEvent -> showAboutWindow());
=======
        menItem = new JMenuItem("About");
        menItem.addActionListener(actionEvent -> about());
        menHelp.add(menItem);
>>>>>>> c721523989fc0f60e9401ceb1a7621e753ea7702

        menMenuBar.add(menMatch);
        menMenuBar.add(menPlayer);
        menMenuBar.add(menHelp);

        setJMenuBar(menMenuBar);
    }

    private void setupTeams() {
        var pnlTeams = new JPanel(new GridBagLayout());
        var pnlLeftTeam = new JPanel(new GridBagLayout());
        var pnlRightTeam = new JPanel(new GridBagLayout());
        var pnlMiddle = new JPanel(new GridBagLayout());

        var constraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, new Insets(5, 5, 5, 5), 0, 0);

        // Left team
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.2;
        pnlLeftTeam.add(lblLeftTeamName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 1.0;
        pnlLeftTeam.add(lstLeftTeamPlayers, constraints);

        // Right team
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.2;
        pnlRightTeam.add(lblRightTeamName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 1.0;
        pnlRightTeam.add(lstRightTeamPlayers, constraints);

        // Middle
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.2;
        pnlMiddle.add(lblLeftTeamScore, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weighty = 0.2;
        pnlMiddle.add(lblRightTeamScore, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        pnlMiddle.add(pnlSuspendedPlayers, constraints);

        constraints.gridwidth = 1;
        constraints.weighty = 1.0;

        // Add intermediate panels
        constraints.gridx = 0;
        constraints.gridy = 0;
        pnlTeams.add(pnlLeftTeam, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        pnlTeams.add(pnlMiddle, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        pnlTeams.add(pnlRightTeam, constraints);

        // Add teams panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        pnlMain.add(pnlTeams, constraints);

        lblLeftTeamName.setFont(TEAM_NAME_FONT);
        lblRightTeamName.setFont(TEAM_NAME_FONT);
        lblLeftTeamScore.setFont(TEAM_SCORE_FONT);
        lblRightTeamScore.setFont(TEAM_SCORE_FONT);

        lstLeftTeamPlayers.setSelectionMode(SINGLE_SELECTION);
        lstRightTeamPlayers.setSelectionMode(SINGLE_SELECTION);
        lstLeftTeamPlayers.addListSelectionListener(listSelectionEvent -> leftListSelection());
        lstRightTeamPlayers.addListSelectionListener(listSelectionEvent -> rightListSelection());

        lblLeftTeamName.setHorizontalAlignment(JLabel.CENTER);
        lblRightTeamName.setHorizontalAlignment(JLabel.CENTER);
        lblLeftTeamScore.setHorizontalAlignment(JLabel.CENTER);
        lblRightTeamScore.setHorizontalAlignment(JLabel.CENTER);

        pnlTeams.setBorder(BorderFactory.createEtchedBorder());
    }

    private void setupTimer() {
        var pnlTimer = new JPanel(new GridBagLayout());
        var pnlButtons = new JPanel(new GridBagLayout());

        var btnLeftTeamTimeout = new JButton("L Team Timeout");
        var btnRightTeamTimeout = new JButton("R Team Timeout");
        var btnRefereeTimeout = new JButton("Referee Timeout");

        var constraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, new Insets(5, 5, 5, 5), 0, 0);

        constraints.gridx = 0;
        constraints.gridy = 0;
        pnlTimer.add(lblTimer, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        pnlTimer.add(pnlButtons, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 20;
        constraints.ipady = 20;
        pnlButtons.add(btnLeftTeamTimeout, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 20;
        constraints.ipady = 20;
        pnlButtons.add(btnRightTeamTimeout, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 20;
        constraints.ipady = 20;
        pnlButtons.add(btnRefereeTimeout, constraints);

        constraints.gridwidth = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.ipadx = 0;
        constraints.ipady = 0;

        // Add timer panel
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.2;
        constraints.weighty = 0.5;
        pnlMain.add(pnlTimer, constraints);

        btnLeftTeamTimeout.addActionListener(actionEvent -> leftTeamTimeout());
        btnRightTeamTimeout.addActionListener(actionEvent -> rightTeamTimeout());
        btnRefereeTimeout.addActionListener(actionEvent -> refereeTimeout());

        lblTimer.setFont(TIMER_FONT);
        lblTimer.setHorizontalAlignment(JLabel.CENTER);

        pnlTimer.setBorder(BorderFactory.createEtchedBorder());
    }

    private void setupPlayerOptions() {
        var pnlPlayerOptions = new JPanel(new GridBagLayout());
        var pnlPlayerData = new JPanel(new GridBagLayout());
        var pnlButtons = new JPanel(new GridBagLayout());

        var btnScore = new JButton("Score");
        var btnSuspend = new JButton("Suspend");
        var btnYellowCard = new JButton("Yellow Card");
        var btnRedCard = new JButton("Red Card");

        var constraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, new Insets(5, 5, 5, 5), 0, 0);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        pnlPlayerData.add(lblSelectedPlayerNameNumber, constraints);

        constraints.gridwidth = 1;

        constraints.gridx = 0;
        constraints.gridy = 1;
        pnlPlayerData.add(lblSelectedPlayerScore, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        pnlPlayerData.add(lblSelectedPlayerIsSuspended, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        pnlPlayerData.add(lblSelectedPlayerHasYellowCard, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        pnlPlayerData.add(lblSelectedPlayerHasRedCard, constraints);

        constraints.gridx = 0;
        constraints.gridy = 0;
        pnlPlayerOptions.add(pnlPlayerData, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        pnlPlayerOptions.add(pnlButtons, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 20;
        constraints.ipady = 20;
        pnlButtons.add(btnScore, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 20;
        constraints.ipady = 20;
        pnlButtons.add(btnSuspend, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 20;
        constraints.ipady = 20;
        pnlButtons.add(btnYellowCard, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 20;
        constraints.ipady = 20;
        pnlButtons.add(btnRedCard, constraints);

        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.ipadx = 0;
        constraints.ipady = 0;

        // Add player options panel
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 0.1;
        constraints.weighty = 1.0;
        pnlMain.add(pnlPlayerOptions, constraints);

        btnScore.addActionListener(actionEvent -> scoreUpPlayer());
        btnSuspend.addActionListener(actionEvent -> suspendPlayer());
        btnYellowCard.addActionListener(actionEvent -> giveYellowCardPlayer());
        btnRedCard.addActionListener(actionEvent -> giveRedCardPlayer());

        lblSelectedPlayerNameNumber.setHorizontalAlignment(JLabel.CENTER);
        lblSelectedPlayerScore.setHorizontalAlignment(JLabel.CENTER);
        lblSelectedPlayerHasYellowCard.setHorizontalAlignment(JLabel.CENTER);
        lblSelectedPlayerHasRedCard.setHorizontalAlignment(JLabel.CENTER);
        lblSelectedPlayerIsSuspended.setHorizontalAlignment(JLabel.CENTER);

        lblSelectedPlayerNameNumber.setFont(PLAYER_DATA_FONT);
        lblSelectedPlayerScore.setFont(PLAYER_DATA_FONT);
        lblSelectedPlayerHasYellowCard.setFont(PLAYER_DATA_FONT);
        lblSelectedPlayerHasRedCard.setFont(PLAYER_DATA_FONT);
        lblSelectedPlayerIsSuspended.setFont(PLAYER_DATA_FONT);

        pnlPlayerOptions.setBorder(BorderFactory.createEtchedBorder());
    }

    private void initializeMatch() {
        new InitializeWindow(this);
    }

    private void showAboutWindow() {
        JFrame frame = new JFrame("About");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel label;
        label = new JLabel("This is the first version of the app.", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        label.setPreferredSize(new Dimension(500, 20));
        frame.add(label, c);

        label = new JLabel("Developers: Simon Maracine and Adrian Demian", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        label.setPreferredSize(new Dimension(500, 50));
        frame.add(label, c);

        label = new JLabel("The application is a simple score table for the game of handball", SwingConstants.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        label.setPreferredSize(new Dimension(500, 50));
        frame.add(label, c);

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
        frame.add(label, c);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    private void beginMatch() {
        matchTimer = new Timer(lblTimer);

        try {
            matchTimer.start();
        } catch (TimerException ignored) {}

        Logging.info("Started match");
    }

    private void endMatch() {
        assert matchTimer != null;

        try {
            matchTimer.stop();
        } catch (TimerException ignored) {}

        Logging.info("Ended match");
    }

    private void resetMatch() {

    }

    private void showPublicWindow() {
        new PublicWindow(this);
    }

    private void scoreDownPlayer() {

    }

    private void releasePlayer() {

    }

    private void takeYellowCardPlayer() {

    }

    private void takeRedCardPlayer() {

    }

    private void about() {
        // FIXME see how to open AboutWindow
//        AboutWindow.addComponentsToPane();
    }

    private void leftTeamTimeout() {

    }

    private void rightTeamTimeout() {

    }

    private void refereeTimeout() {

    }

    private void scoreUpPlayer() {

    }

    private void suspendPlayer() {

    }

    private void giveYellowCardPlayer() {

    }

    private void giveRedCardPlayer() {

    }

    private void leftListSelection() {
        if (lstLeftTeamPlayers.getValueIsAdjusting()) {
            return;
        }

        final int index = lstLeftTeamPlayers.getSelectedIndex();

        if (index != -1) {
            selectedPlayer = match.getLeftTeam().getPlayers()[index];
            lstRightTeamPlayers.clearSelection();

            fillSelectedPlayerData();

            Logging.info("Selected left player: " + selectedPlayer);
        }
    }

    private void rightListSelection() {
        if (lstRightTeamPlayers.getValueIsAdjusting()) {
            return;
        }

        final int index = lstRightTeamPlayers.getSelectedIndex();

        if (index != -1) {
            selectedPlayer = match.getRightTeam().getPlayers()[index];
            lstLeftTeamPlayers.clearSelection();

            fillSelectedPlayerData();

            Logging.info("Selected right player: " + selectedPlayer);
        }
    }

    private void fillSelectedPlayerData() {
        assert selectedPlayer != null;

        lblSelectedPlayerNameNumber.setText(selectedPlayer.getName() + "[" + selectedPlayer.getNumber() + "]");
        lblSelectedPlayerScore.setText("Score: " + selectedPlayer.getScore());
        lblSelectedPlayerHasYellowCard.setText("Yellow Card: " + (selectedPlayer.hasYellowCard() ?  "true" : "false"));
        lblSelectedPlayerHasRedCard.setText("Red Card: " + (selectedPlayer.hasRedCard() ?  "true" : "false"));
        lblSelectedPlayerIsSuspended.setText("Suspended: " + (selectedPlayer.isSuspended() ? "true" : "false"));
    }
}
