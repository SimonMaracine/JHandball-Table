package handball;

public class Player {
    private final String name;
    private final int number;
    private int score;
    private boolean hasYellowCard;
    private boolean isSuspended;
    private int numberOfSuspensions;
    private boolean hasRedCard;

    private final Team team;

    public Player(String name, int number, Team team) {
        this.name = name;
        this.number = number;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getScore() {
        return score;
    }

    public void scoreUp() {
        score++;
        team.addToTotalScore();
    }

    public void scoreDown() {
        score--;
        team.removeFromTotalScore();
    }

    public boolean hasYellowCard() {
        return hasYellowCard;
    }

    public void giveYellowCard() {
        hasYellowCard = true;
        team.addToNumberOfYellowCards();
    }

    public void takeYellowCard() {
        hasYellowCard = false;
        team.removeFromNumberOfYellowCards();
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void suspend() {
        isSuspended = true;
        numberOfSuspensions++;
    }

    public void release() {
        isSuspended = false;
    }

    public void release(boolean undo) {
        isSuspended = false;

        if (undo) {
            numberOfSuspensions++;
        }
    }

    public int getNumberOfSuspensions() {
        return numberOfSuspensions;
    }

    public boolean hasRedCard() {
        return hasRedCard;
    }

    public void giveRedCard() {
        hasRedCard = true;
    }

    public void takeRedCard() {
        this.hasRedCard = false;
    }
}
