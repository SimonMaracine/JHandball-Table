package handball;

import java.util.ArrayList;
import java.util.Date;

public class Match {
    public static final int HALF_MATCH_TIME = 30 * 60;
    public static final int INTERMISSION_TIME = 10 * 60;
    public static final int OVERTIME_TIME = 5 * 60;

    private final Team leftTeam;
    private final Team rightTeam;
    private final Date date;
    private boolean hasEnded = false;
    private final ArrayList<SuspendedPlayer> suspendedPlayers = new ArrayList<>();

    public Match(Team leftTeam, Team rightTeam, Date date) {
        this.leftTeam = leftTeam;
        this.rightTeam = rightTeam;
        this.date = date;
    }

    public Team getLeftTeam() {
        return leftTeam;
    }

    public Team getRightTeam() {
        return rightTeam;
    }

    public String getDate() {
        return date.toString();
    }

    public boolean hasEnded() {
        return hasEnded;
    }

    public void end() {
        this.hasEnded = true;
    }

    public ArrayList<SuspendedPlayer> getSuspendedPlayers() {
        return suspendedPlayers;
    }
}
