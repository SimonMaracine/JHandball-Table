package windows;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

import handball.*;
import other.EntangledLabel;
import other.Logging;
import timer.Timer;

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

    final EntangledLabel lblLeftTeamName = new EntangledLabel("Team 1", null);
    final EntangledLabel lblRightTeamName = new EntangledLabel("Team 2", null);
    final EntangledLabel lblLeftTeamScore = new EntangledLabel("0", null);
    final EntangledLabel lblRightTeamScore = new EntangledLabel("0", null);

    final DefaultListModel<String> leftTeamPlayers = new DefaultListModel<>();
    final DefaultListModel<String> rightTeamPlayers = new DefaultListModel<>();

    private final JList<String> lstLeftTeamPlayers = new JList<>(leftTeamPlayers);
    private final JList<String> lstRightTeamPlayers = new JList<>(rightTeamPlayers);

    final EntangledLabel lblTimer = new EntangledLabel("00:00", null);
    private final JPanel pnlSuspendedPlayers = new JPanel();
    private final JLabel lblSelectedPlayerNameNumber = new JLabel("n/a");
    private final JLabel lblSelectedPlayerScore = new JLabel("n/a");
    private final JLabel lblSelectedPlayerHasYellowCard = new JLabel("n/a");
    private final JLabel lblSelectedPlayerHasRedCard = new JLabel("n/a");
    private final JLabel lblSelectedPlayerIsSuspended = new JLabel("n/a");

    final ArrayList<JLabel> lblSuspendedPlayers = new ArrayList<>();

    private MatchStatus matchStatus = MatchStatus.Half1;
    private final JLabel lblMatchStatus = new JLabel(matchStatus.toString());
    private MatchStatus previousMatchStatus = matchStatus;  // Used after timeouts

    Match match = null;
    private Timer matchTimer = null;
    private Timer teamTimeoutTimer = null;
    private Player selectedPlayer = null;
    private boolean isTeamTimeout = false;

    PublicWindow publicWindow = null;

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

        if (publicWindow != null) {
            publicWindow.setupData();
        }

        pack();
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

        menItem = new JMenuItem("About");
        menItem.addActionListener(actionEvent -> about());
        menHelp.add(menItem);

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

        pnlSuspendedPlayers.setLayout(new BoxLayout(pnlSuspendedPlayers, BoxLayout.Y_AXIS));

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

        constraints.gridx = 1;
        constraints.gridy = 0;
        pnlTimer.add(lblMatchStatus, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        pnlTimer.add(pnlButtons, constraints);

        constraints.gridwidth = 1;

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

    private void beginMatch() {
        if (match == null) {
            showMatchNotInitializedPopup();
            return;
        }

        if (isTeamTimeout) {
            switch (matchStatus) {
                case Half1, Half2, Overtime1, Overtime2 -> {
                    matchTimer.start();
                    teamTimeoutTimer = null;
                    isTeamTimeout = false;
                }
                default -> Logging.warning("There is nothing to begin. It's team timeout");
            }

            return;
        }

        switch (matchStatus) {
            case Half1 -> {
                if (matchTimer == null || !matchTimer.isRunning()) {
                    matchTimer = createHalf1Timer();
                } else {
                    showNothingToBeginPopup();
                    return;
                }
            }
            case Intermission -> {
                if (!matchTimer.isRunning()) {
                    matchTimer = createIntermissionTimer();
                } else {
                    showNothingToBeginPopup();
                    return;
                }
            }
            case Half2 -> {
                if (!matchTimer.isRunning()) {
                    matchTimer = createHalf2Timer();
                } else {
                    showNothingToBeginPopup();
                    return;
                }
            }
            case Overtime1 -> {
                if (!matchTimer.isRunning()) {
                    matchTimer = createOvertime1Timer();
                } else {
                    showNothingToBeginPopup();
                    return;
                }
            }
            case Overtime2 -> {
                if (!matchTimer.isRunning()) {
                    matchTimer = createOvertime2Timer();
                } else {
                    showNothingToBeginPopup();
                    return;
                }
            }
            case Penalty -> {
                showMatchIsPenaltyPopup();
                return;
            }
            case RefereeTimeout -> {
                matchStatus = previousMatchStatus;
                lblMatchStatus.setText(matchStatus.toString());
            }
            default -> {
                showNothingToBeginPopup();
                return;
            }
        }

        matchTimer.start();

        Logging.info("(Re)Started match");
    }

    private void endMatch() {  // TODO implement the rest
        assert match != null;
        assert matchTimer != null;

        if (!showConfirmEndPopup()) {
            return;
        }

        matchTimer.stop();  // TODO check

        Logging.info("Ended match");
    }

    private void resetMatch() {
        if (match == null) {
            showMatchNotInitializedPopup();
            return;
        }

        if (!showConfirmResetPopup()) {
            return;
        }

        String leftTeamName;
        String rightTeamName;
        String[] leftTeamPlayerNames = new String[7];
        String[] rightTeamPlayerNames = new String[7];
        int[] leftTeamPlayerNumbers = new int[7];
        int[] rightTeamPlayerNumbers = new int[7];

        leftTeamName = match.getLeftTeam().getName();
        rightTeamName = match.getRightTeam().getName();
        for (int i = 0; i < 7; i++) {
            leftTeamPlayerNames[i] = match.getLeftTeam().getPlayers()[i].getName();
            rightTeamPlayerNames[i] = match.getRightTeam().getPlayers()[i].getName();
            leftTeamPlayerNumbers[i] = match.getLeftTeam().getPlayers()[i].getNumber();
            rightTeamPlayerNumbers[i] = match.getRightTeam().getPlayers()[i].getNumber();
        }

        Team leftTeam = new Team(leftTeamName);
        Team rightTeam = new Team(rightTeamName);

        for (int i = 0; i < 7; i++) {
            leftTeam.getPlayers()[i] = new Player(leftTeamPlayerNames[i], leftTeamPlayerNumbers[i], leftTeam);
            rightTeam.getPlayers()[i] = new Player(rightTeamPlayerNames[i], rightTeamPlayerNumbers[i], rightTeam);
        }

        match = new Match(leftTeam, rightTeam, new Date());

        if (matchTimer != null && matchTimer.isRunning()) {
            matchTimer.stop();
        }

        if (teamTimeoutTimer != null && teamTimeoutTimer.isRunning()) {
            teamTimeoutTimer.stop();
        }

        matchTimer = null;
        teamTimeoutTimer = null;
        isTeamTimeout = false;
        selectedPlayer = null;

        matchStatus = MatchStatus.Half1;
        previousMatchStatus = matchStatus;
        lblMatchStatus.setText(matchStatus.toString());

        lblSuspendedPlayers.clear();
        pnlSuspendedPlayers.removeAll();

        if (publicWindow != null) {
            publicWindow.lblSuspendedPlayers.clear();
            publicWindow.pnlSuspendedPlayers.removeAll();
        }

        lblTimer.setText("00:00");
        lblLeftTeamScore.setText("0");
        lblRightTeamScore.setText("0");
        lblSelectedPlayerNameNumber.setText("n/a");
        lblSelectedPlayerScore.setText("n/a");
        lblSelectedPlayerHasYellowCard.setText("n/a");
        lblSelectedPlayerHasRedCard.setText("n/a");
        lblSelectedPlayerIsSuspended.setText("n/a");

        Logging.info("Reset match");
    }

    private void showPublicWindow() {
        publicWindow = new PublicWindow(this);

        lblTimer.setAnother(publicWindow.lblTimer);
        lblLeftTeamName.setAnother(publicWindow.lblLeftTeamName);
        lblRightTeamName.setAnother(publicWindow.lblRightTeamName);
        lblLeftTeamScore.setAnother(publicWindow.lblLeftTeamScore);
        lblRightTeamScore.setAnother(publicWindow.lblRightTeamScore);

        publicWindow.setupData();
    }

    private void scoreDownPlayer() {
        if (selectedPlayer == null) {
            Logging.warning("Selected player is null");
            return;
        }

        if (selectedPlayer.getScore() == 0) {
            Logging.warning("Cannot score down player with score 0");
            return;
        }

        selectedPlayer.scoreDown();

        lblLeftTeamScore.setText(String.valueOf(match.getLeftTeam().getTotalScore()));
        lblRightTeamScore.setText(String.valueOf(match.getRightTeam().getTotalScore()));
        fillSelectedPlayerData();

        if (publicWindow != null) {
            updateSelectedPlayerTextInPublicWindow();
        }
    }

    private void releasePlayer() {

    }

    private void takeYellowCardPlayer() {
        if (selectedPlayer == null) {
            Logging.warning("Selected player is null");
            return;
        }

        selectedPlayer.takeYellowCard();

        fillSelectedPlayerData();
    }

    private void takeRedCardPlayer() {
        if (selectedPlayer == null) {
            Logging.warning("Selected player is null");
            return;
        }

        selectedPlayer.takeRedCard();

        fillSelectedPlayerData();
    }

    private void about() {
        new AboutWindow();
    }

    private void leftTeamTimeout() {
        if (match == null) {
            Logging.warning("Match is not initialized");
            return;
        }

        if (matchTimer == null || !matchTimer.isRunning()) {
            Logging.warning("Match is not running");
            return;
        }

        if (matchStatus != MatchStatus.Half1 && matchStatus != MatchStatus.Half2
                && matchStatus != MatchStatus.Overtime1 && matchStatus != MatchStatus.Overtime2) {
            Logging.warning("Match is not running");
            return;
        }

        if (match.getLeftTeam().getNumberOfTimeoutCalls() == 3) {
            showTeamTimeoutCallsPopup();
            return;
        }

        assert !isTeamTimeout;

        matchTimer.stop();

        teamTimeoutTimer = createTeamTimeoutTimer();
        teamTimeoutTimer.start();

        matchStatus = MatchStatus.LeftTeamTimeout;
        lblMatchStatus.setText(matchStatus.toString());

        match.getLeftTeam().addToNumberOfTimeoutCalls();

        isTeamTimeout = true;
    }

    private void rightTeamTimeout() {
        if (match == null) {
            Logging.warning("Match is not initialized");
            return;
        }

        if (matchTimer == null || !matchTimer.isRunning()) {
            Logging.warning("Match is not running");
            return;
        }

        if (matchStatus != MatchStatus.Half1 && matchStatus != MatchStatus.Half2
                && matchStatus != MatchStatus.Overtime1 && matchStatus != MatchStatus.Overtime2) {
            Logging.warning("Match is not running");
            return;
        }

        if (match.getRightTeam().getNumberOfTimeoutCalls() == 3) {
            showTeamTimeoutCallsPopup();
            return;
        }

        assert !isTeamTimeout;

        matchTimer.stop();

        teamTimeoutTimer = createTeamTimeoutTimer();
        teamTimeoutTimer.start();

        matchStatus = MatchStatus.RightTeamTimeout;
        lblMatchStatus.setText(matchStatus.toString());

        match.getRightTeam().addToNumberOfTimeoutCalls();

        isTeamTimeout = true;
    }

    private void refereeTimeout() {
        if (match == null) {
            Logging.warning("Match is not initialized");
            return;
        }

        if (matchTimer == null || !matchTimer.isRunning()) {
            Logging.warning("Match is not running");
            return;
        }

        assert !isTeamTimeout;

        matchTimer.stop();

        matchStatus = MatchStatus.RefereeTimeout;
        lblMatchStatus.setText(matchStatus.toString());
    }

    private void scoreUpPlayer() {
        if (selectedPlayer == null) {
            Logging.warning("Selected player is null");
            return;
        }

        selectedPlayer.scoreUp();

        lblLeftTeamScore.setText(String.valueOf(match.getLeftTeam().getTotalScore()));
        lblRightTeamScore.setText(String.valueOf(match.getRightTeam().getTotalScore()));
        fillSelectedPlayerData();

        if (publicWindow != null) {
            updateSelectedPlayerTextInPublicWindow();
        }
    }

    private void suspendPlayer() {
        if (selectedPlayer == null) {
            Logging.warning("Selected player is null");
            return;
        }

        if (selectedPlayer.getNumberOfSuspensions() == 2) {
            showPlayerSuspensionsPopup();
            return;
        }

        selectedPlayer.suspend();

        final String text = selectedPlayer.getName() + " - 00:00";
        final EntangledLabel label = new EntangledLabel(text, null);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        SuspendedPlayer player = new SuspendedPlayer(null, selectedPlayer);
        Timer timer = new Timer(label, SuspendedPlayer.SUSPENSION_TIME, () -> suspendedPlayerFinish(player), selectedPlayer.getName() + " - ");

        pnlSuspendedPlayers.add(label);
        lblSuspendedPlayers.add(label);
        if (publicWindow != null) {
            JLabel publicWindowLabel = new JLabel(text);
            publicWindowLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            publicWindow.pnlSuspendedPlayers.add(publicWindowLabel);
            publicWindow.lblSuspendedPlayers.add(publicWindowLabel);

            label.setAnother(publicWindow.lblSuspendedPlayers.get(publicWindow.lblSuspendedPlayers.size() - 1));
        }
        match.getSuspendedPlayers().add(player);

        player.setTimer(timer);
        player.setIndex(match.getSuspendedPlayers().size() - 1);

        timer.start();

        pack();

        Logging.info("Suspended player " + selectedPlayer);
    }

    private void giveYellowCardPlayer() {
        if (selectedPlayer == null) {
            Logging.warning("Selected player is null");
            return;
        }

        selectedPlayer.giveYellowCard();

        fillSelectedPlayerData();
    }

    private void giveRedCardPlayer() {
        if (selectedPlayer == null) {
            Logging.warning("Selected player is null");
            return;
        }

        selectedPlayer.giveRedCard();

        fillSelectedPlayerData();
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

    private void updateSelectedPlayerTextInPublicWindow() {
        final int leftIndex = lstLeftTeamPlayers.getSelectedIndex();
        final int rightIndex = lstRightTeamPlayers.getSelectedIndex();

        if (leftIndex != -1) {
            final String text = leftTeamPlayers.get(leftIndex) + " - " + match.getLeftTeam().getPlayers()[leftIndex].getScore();
            publicWindow.lblLeftTeamPlayers[leftIndex].setText(text);
        } else if (rightIndex != -1) {
            final String text = rightTeamPlayers.get(rightIndex) + " - " + match.getRightTeam().getPlayers()[rightIndex].getScore();
            publicWindow.lblRightTeamPlayers[rightIndex].setText(text);
        } else {
            assert false;
        }
    }

    private void suspendedPlayerFinish(SuspendedPlayer suspendedPlayer) {
        pnlSuspendedPlayers.remove(suspendedPlayer.getIndex());
        lblSuspendedPlayers.remove(suspendedPlayer.getIndex());
        pack();

        if (publicWindow != null) {
            publicWindow.pnlSuspendedPlayers.remove(suspendedPlayer.getIndex());
            publicWindow.lblSuspendedPlayers.remove(suspendedPlayer.getIndex());
            publicWindow.pack();
        }

        match.getSuspendedPlayers().remove(suspendedPlayer.getIndex());

        Logging.info("Called suspendedPlayerFinish on player " + suspendedPlayer.getPlayer());
    }

    private Timer createHalf1Timer() {
        Logging.info("Creating Half1 timer...");

        return new Timer(lblTimer, Match.HALF_MATCH_TIME, () -> {
            matchStatus = MatchStatus.Intermission;
            previousMatchStatus = matchStatus;
            lblMatchStatus.setText(matchStatus.toString());
        });
    }

    private Timer createIntermissionTimer() {
        Logging.info("Creating Intermission timer...");

        return new Timer(lblTimer, Match.INTERMISSION_TIME, () -> {
            matchStatus = MatchStatus.Half2;
            previousMatchStatus = matchStatus;
            lblMatchStatus.setText(matchStatus.toString());
        });
    }

    private Timer createHalf2Timer() {
        Logging.info("Creating Half2 timer...");

        return new Timer(lblTimer, Match.HALF_MATCH_TIME, () -> {
            matchStatus = MatchStatus.Overtime1;
            previousMatchStatus = matchStatus;
            lblMatchStatus.setText(matchStatus.toString());
        });
    }

    private Timer createOvertime1Timer() {
        Logging.info("Creating Overtime1 timer...");

        return new Timer(lblTimer, Match.OVERTIME_TIME, () -> {
            matchStatus = MatchStatus.Overtime2;
            previousMatchStatus = matchStatus;
            lblMatchStatus.setText(matchStatus.toString());
        });
    }

    private Timer createOvertime2Timer() {
        Logging.info("Creating Overtime2 timer...");

        return new Timer(lblTimer, Match.OVERTIME_TIME, () -> {
            matchStatus = MatchStatus.Penalty;
            previousMatchStatus = matchStatus;
            lblMatchStatus.setText(matchStatus.toString());
        });
    }

    private Timer createTeamTimeoutTimer() {
        Logging.info("Creating TeamTimeout timer...");

        return new Timer(lblTimer, Team.TIMEOUT_TIME, () -> {
            matchStatus = previousMatchStatus;
            lblMatchStatus.setText(matchStatus.toString());
        });
    }

    private void showMatchNotInitializedPopup() {
        JOptionPane.showMessageDialog(this, "Match is not initialized.", "Match", JOptionPane.ERROR_MESSAGE);

        Logging.warning("Match is not initialized");
    }

    private void showNothingToBeginPopup() {
        JOptionPane.showMessageDialog(this, "There is nothing to begin.", "Match", JOptionPane.ERROR_MESSAGE);

        Logging.warning("There is nothing to begin");
    }

    private void showMatchIsPenaltyPopup() {
        JOptionPane.showMessageDialog(this, "Match is in penalty now. There is nothing to begin.", "Match", JOptionPane.ERROR_MESSAGE);

        Logging.warning("Match is in penalty now");
    }

    private void showTeamTimeoutCallsPopup() {
        JOptionPane.showMessageDialog(this, "Team has already called timeout three times.", "Team Timeout", JOptionPane.ERROR_MESSAGE);

        Logging.warning("Team has already called timeout three times");
    }

    private void showPlayerSuspensionsPopup() {
        JOptionPane.showMessageDialog(
            this, "Player has already been suspended twice. You must give them a red card", "Player Suspension", JOptionPane.ERROR_MESSAGE
        );

        Logging.warning("Player has already been suspended twice");
    }

    private boolean showConfirmEndPopup() {
        int result = JOptionPane.showConfirmDialog(
            this, "Are you sure you want to end the match?", "End", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );

        if (result == 0) {
            Logging.info("Chose yes");
            return true;
        } else if (result == 1) {
            Logging.info("Chose no");
            return false;
        }

        Logging.severe("Result not handled");
        assert false;
        return false;
    }

    private boolean showConfirmResetPopup() {
        int result = JOptionPane.showConfirmDialog(
            this, "Are you sure you want to reset the match?", "Reset", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );

        if (result == 0) {
            Logging.info("Chose yes");
            return true;
        } else if (result == 1) {
            Logging.info("Chose no");
            return false;
        }

        Logging.severe("Result not handled");
        assert false;
        return false;
    }
}
