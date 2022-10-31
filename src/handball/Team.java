package handball;

import java.util.ArrayList;

public class Team {
    private final String name;
    private int totalScore = 0;
    private int numberOfTimeoutCalls = 0;
    private int numberOfYellowCards = 0;
    private final Player[] players = new Player[7];

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getTotalScore() {
        return totalScore;
    }

    void addToTotalScore() {
        totalScore++;
    }

    void removeFromTotalScore() {
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

    void addToNumberOfYellowCards() {
        numberOfYellowCards++;
    }

    void removeFromNumberOfYellowCards() {
        numberOfYellowCards--;
    }

    public Player[] getPlayers() {
        return players;
    }
}
