package windows;

import other.Logging;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.CENTER;

class PublicWindow extends JFrame {
    private static final int MIN_WIDTH = 1120;
    private static final int MIN_HEIGHT = 630;

    private static final Font TEAM_NAME_FONT = new Font("Monospaced", Font.PLAIN, 40);  // TODO choose fallback fonts
    private static final Font TEAM_SCORE_FONT = new Font("Monospaced", Font.PLAIN, 80);
    private static final Font TIMER_FONT = new Font("Monospaced", Font.PLAIN, 120);

    private final JPanel pnlMain = new JPanel(new GridBagLayout());

    private final JLabel lblLeftTeamName = new JLabel("Team 1");  // FIXME make a way to easily mirror main window's labels
    private final JLabel lblRightTeamName = new JLabel("Team 2");
    private final JLabel lblLeftTeamScore = new JLabel("0");
    private final JLabel lblRightTeamScore = new JLabel("0");
    private final JLabel lblTimer = new JLabel("00:00");

    private final JPanel pnlLeftTeamPlayers = new JPanel();
    private final JPanel pnlRightTeamPlayers = new JPanel();
    private final JPanel pnlSuspendedPlayers = new JPanel();

    private final Application application;

    PublicWindow(Application application) {
        super("JHandball Table");

        this.application = application;

        setupLayout();

        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setVisible(true);

        Logging.info("Created public window");
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

        add(pnlMain);
        pack();
    }
}
