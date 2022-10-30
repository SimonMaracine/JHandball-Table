package handball;

import java.util.ArrayList;

public class Team {
    private final String name;
    private int totalScore = 0;
    private int numberOfTimeoutCalls = 0;
    private int numberOfYellowCards = 0;
    private final ArrayList<Player> players = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addToTotalScore() {
        totalScore++;
    }

    public void removeFromTotalScore() {
        totalScore--;
    }

    public int getNumberOfTimeoutCalls() {
        return numberOfTimeoutCalls;
    }

    public void addToNumberOfTimeoutCalls() {
        numberOfTimeoutCalls++;
    }

    public void removeFromNumberOfTimeoutCalls() {
        numberOfTimeoutCalls--;
    }

    public int getNumberOfYellowCards() {
        return numberOfYellowCards;
    }

    public void addToNumberOfYellowCards() {
        numberOfYellowCards++;
    }

    public void removeFromNumberOfYellowCards() {
        numberOfYellowCards--;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
