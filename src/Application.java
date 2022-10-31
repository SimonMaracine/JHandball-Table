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

class Application extends JFrame {
    private static final int MIN_WIDTH = 1024;
    private static final int MIN_HEIGHT = 576;

    private static final Font TEAM_NAME_FONT = new Font("Monospaced", Font.PLAIN, 25);  // TODO choose fallback fonts
    private static final Font TEAM_SCORE_FONT = new Font("Monospaced", Font.PLAIN, 50);
    private static final Font TIMER_FONT = new Font("Monospaced", Font.PLAIN, 100);

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
    private final JLabel lblSelectedPlayer = new JLabel("Selected player: None");

    private Timer matchTimer = null;
    Match match = null;
    private Player selectedPlayer = null;

    Application() {
        super("JHandball Table");

        setupLayout();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setVisible(true);

        Logging.info("Initialized main window");
    }

    void initializeTeams() {
        // TODO add all match data to GUI
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

        menHelp.add(new JMenuItem("About"));

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

        lblLeftTeamName.setHorizontalAlignment(JLabel.CENTER);
        lblRightTeamName.setHorizontalAlignment(JLabel.CENTER);
        lblLeftTeamScore.setHorizontalAlignment(JLabel.CENTER);
        lblRightTeamScore.setHorizontalAlignment(JLabel.CENTER);
    }

    private void setupTimer() {
        var pnlTimer = new JPanel(new GridBagLayout());

        var btnLeftTeamTimeout = new JButton("Left Team Timeout");
        var btnRightTeamTimeout = new JButton("Right Team Timeout");
        var btnRefereeTimeout = new JButton("Referee Timeout");

        var constraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, new Insets(5, 5, 5, 5), 0, 0);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        pnlTimer.add(lblTimer, constraints);

        constraints.gridwidth = 1;

        constraints.gridx = 0;
        constraints.gridy = 1;
        pnlTimer.add(btnLeftTeamTimeout, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        pnlTimer.add(btnRightTeamTimeout, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        pnlTimer.add(btnRefereeTimeout, constraints);

        constraints.gridwidth = 1;

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
    }

    private void setupPlayerOptions() {
        var pnlPlayerOptions = new JPanel(new GridBagLayout());

        var btnScore = new JButton("Score");
        var btnSuspend = new JButton("Suspend");
        var btnYellowCard = new JButton("Yellow Card");
        var btnRedCard = new JButton("Red Card");

        var constraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, CENTER, BOTH, new Insets(5, 5, 5, 5), 0, 0);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        pnlPlayerOptions.add(lblSelectedPlayer, constraints);

        constraints.gridwidth = 1;

        constraints.gridx = 0;
        constraints.gridy = 1;
        pnlPlayerOptions.add(btnScore, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        pnlPlayerOptions.add(btnSuspend, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        pnlPlayerOptions.add(btnYellowCard, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        pnlPlayerOptions.add(btnRedCard, constraints);

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

        lblSelectedPlayer.setHorizontalAlignment(JLabel.CENTER);
    }

    private void initializeMatch() {
        new InitializeWindow(this);
    }

    private void beginMatch() {
        matchTimer = new Timer(lblTimer);

        try {
            matchTimer.start();
        } catch (TimerException ignored) {}

        Logging.info("Started match");
    }

    private void endMatch() {
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
}
