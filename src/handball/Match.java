package handball;

import java.util.ArrayList;
import java.util.Date;

public class Match {
    private final Team leftTeam;
    private final Team rightTeam;
    private final Date date;
    private boolean hasEnded = false;
    private final ArrayList<Player> suspendedPlayers = new ArrayList<>();

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

    public ArrayList<Player> getSuspendedPlayers() {
        return suspendedPlayers;
    }
}
