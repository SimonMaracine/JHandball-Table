package handball;

public enum MatchStatus {
    Half1,
    Intermission,
    Half2,
    Overtime1,
    Overtime2,
    Penalty,
    RefereeTimeout,
    LeftTeamTimeout,
    RightTeamTimeout;

    @Override
    public String toString() {
        switch (this) {
            case Half1 -> {
                return "Half One";
            }
            case Intermission -> {
                return "Intermission";
            }
            case Half2 -> {
                return "Half Two";
            }
            case Overtime1 -> {
                return "Overtime One";
            }
            case Overtime2 -> {
                return "Overtime Two";
            }
            case Penalty -> {
                return "Penalty";
            }
            case RefereeTimeout -> {
                return "Referee Timeout";
            }
            case LeftTeamTimeout -> {
                return "Left Team Timeout";
            }
            case RightTeamTimeout -> {
                return "Right Team Timeout";
            }
            default -> {
                return null;
            }
        }
    }
}
