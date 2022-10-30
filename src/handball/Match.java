package handball;

import java.util.ArrayList;

public class Match {
    private String date;
    private boolean hasEnded;
    private ArrayList<Player> suspendedPlayers = new ArrayList<Player>();

    public Match(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isHasEnded() {
        return hasEnded;
    }

    public void setHasEnded(boolean hasEnded) {
        this.hasEnded = hasEnded;
    }

    public ArrayList<Player> getSuspendedPlayers() {
        return suspendedPlayers;
    }

    public void addSuspendedPlayers(Player p) {
        suspendedPlayers.add(p);
    }
}
