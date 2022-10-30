package handball;

import java.util.ArrayList;

public class Match {
    private final String date;
    private boolean hasEnded;
    private final ArrayList<Player> suspendedPlayers = new ArrayList<>();

    public Match(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
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
