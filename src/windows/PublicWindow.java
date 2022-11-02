package windows;

import handball.SuspendedPlayer;
import other.Logging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;

class PublicWindow extends JFrame {
    private static final int MIN_WIDTH = 1120;
    private static final int MIN_HEIGHT = 630;

    private static final Font TEAM_NAME_FONT = new Font("Monospaced", Font.PLAIN, 40);  // TODO choose fallback fonts
    private static final Font TEAM_SCORE_FONT = new Font("Monospaced", Font.PLAIN, 90);
    private static final Font TIMER_FONT = new Font("Monospaced", Font.PLAIN, 130);

    private final JPanel pnlMain = new JPanel(new GridBagLayout());

    final JLabel lblLeftTeamName = new JLabel("Team 1");
    final JLabel lblRightTeamName = new JLabel("Team 2");
    final JLabel lblLeftTeamScore = new JLabel("0");
    final JLabel lblRightTeamScore = new JLabel("0");
    final JLabel lblTimer = new JLabel("00:00");

    private final JPanel pnlLeftTeamPlayers = new JPanel();
    private final JPanel pnlRightTeamPlayers = new JPanel();
    final JPanel pnlSuspendedPlayers = new JPanel();

    final JLabel[] lblLeftTeamPlayers = new JLabel[7];
    final JLabel[] lblRightTeamPlayers = new JLabel[7];
    final ArrayList<JLabel> lblSuspendedPlayers = new ArrayList<>();

    private final Application application;

    PublicWindow(Application application) {
        super("JHandball Table");

        this.application = application;

        setupLayout();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                application.publicWindow = null;
                Logging.info("Set publicWindow to null");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);

                application.publicWindow = null;
                Logging.info("Set publicWindow to null");
            }
        });

        Logging.info("Created public window");
    }

    void setupData() {
        if (application.match == null) {
            return;
        }

        lblTimer.setText(application.lblTimer.getText());
        lblLeftTeamName.setText(application.lblLeftTeamName.getText());
        lblRightTeamName.setText(application.lblRightTeamName.getText());
        lblLeftTeamScore.setText(application.lblLeftTeamScore.getText());
        lblRightTeamScore.setText(application.lblRightTeamScore.getText());

        for (int i = 0; i < 7; i++) {
            String text;
            JLabel label;

            text = application.leftTeamPlayers.get(i) + " - " + application.match.getLeftTeam().getPlayers()[i].getScore();
            label = new JLabel(text);
            label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            lblLeftTeamPlayers[i] = label;
            pnlLeftTeamPlayers.add(label);

            text = application.rightTeamPlayers.get(i) + " - " + application.match.getRightTeam().getPlayers()[i].getScore();
            label = new JLabel(text);
            label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            lblRightTeamPlayers[i] = label;
            pnlRightTeamPlayers.add(label);
        }

        for (SuspendedPlayer player : application.match.getSuspendedPlayers()) {
            pnlSuspendedPlayers.add(new JLabel(player.getPlayer().getName() + "[" + player.getPlayer().getNumber() + "]"));
        }

        Logging.info("Setup public window data");
    }

    private void setupLayout() {
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
        constraints.weighty = 0.2;
        pnlLeftTeam.add(lblLeftTeamScore, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weighty = 1.0;
        pnlLeftTeam.add(pnlLeftTeamPlayers, constraints);

        // Right team
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.2;
        pnlRightTeam.add(lblRightTeamName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 0.2;
        pnlRightTeam.add(lblRightTeamScore, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weighty = 1.0;
        pnlRightTeam.add(pnlRightTeamPlayers, constraints);

        // Middle
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.2;
        pnlMiddle.add(lblTimer, constraints);

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
        pnlMain.add(pnlLeftTeam, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        pnlMain.add(pnlMiddle, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        pnlMain.add(pnlRightTeam, constraints);

        lblTimer.setFont(TIMER_FONT);
        lblLeftTeamName.setFont(TEAM_NAME_FONT);
        lblRightTeamName.setFont(TEAM_NAME_FONT);
        lblLeftTeamScore.setFont(TEAM_SCORE_FONT);
        lblRightTeamScore.setFont(TEAM_SCORE_FONT);

        lblTimer.setHorizontalAlignment(JLabel.CENTER);
        lblLeftTeamName.setHorizontalAlignment(JLabel.CENTER);
        lblRightTeamName.setHorizontalAlignment(JLabel.CENTER);
        lblLeftTeamScore.setHorizontalAlignment(JLabel.CENTER);
        lblRightTeamScore.setHorizontalAlignment(JLabel.CENTER);

        pnlLeftTeamPlayers.setBorder(BorderFactory.createEtchedBorder());
        pnlRightTeamPlayers.setBorder(BorderFactory.createEtchedBorder());
        pnlSuspendedPlayers.setBorder(BorderFactory.createEtchedBorder());

        pnlLeftTeamPlayers.setLayout(new BoxLayout(pnlLeftTeamPlayers, BoxLayout.Y_AXIS));
        pnlRightTeamPlayers.setLayout(new BoxLayout(pnlRightTeamPlayers, BoxLayout.Y_AXIS));
        pnlSuspendedPlayers.setLayout(new BoxLayout(pnlSuspendedPlayers, BoxLayout.Y_AXIS));

        add(pnlMain);
        pack();
    }
}
