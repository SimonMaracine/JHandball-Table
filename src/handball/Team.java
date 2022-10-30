package handball;

import java.util.ArrayList;

public class Team {
    private String name;
    private int totalScore;
    private int numberOfTimeoutCalls;
    private int numberOfYellowCards;
    private ArrayList<Player> players = new ArrayList<Player>();

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getNumberOfTimeoutCalls() {
        return numberOfTimeoutCalls;
    }

    public void setNumberOfTimeoutCalls(int numberOfTimeoutCalls) {
        this.numberOfTimeoutCalls = numberOfTimeoutCalls;
    }

    public int getNumberOfYellowCards() {
        return numberOfYellowCards;
    }

    public void setNumberOfYellowCards(int numberOfYellowCards) {
        this.numberOfYellowCards = numberOfYellowCards;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayers(Player p) {
        players.add(p);
    }
}
