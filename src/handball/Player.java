package handball;

public class Player {
    private String name;
    private int number;
    private int score;
    private boolean hasYellowCard;
    private boolean isSuspended;
    private int numberOfSuspensions;
    private boolean isDisqualified;


    public Player(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isHasYellowCard() {
        return hasYellowCard;
    }

    public void setHasYellowCard(boolean hasYellowCard) {
        this.hasYellowCard = hasYellowCard;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public int getNumberOfSuspensions() {
        return numberOfSuspensions;
    }

    public void setNumberOfSuspensions(int numberOfSuspensions) {
        this.numberOfSuspensions = numberOfSuspensions;
    }

    public boolean isDisqualified() {
        return isDisqualified;
    }

    public void setDisqualified(boolean disqualified) {
        isDisqualified = disqualified;
    }
}
