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
                return "Half 1";
            }
            case Intermission -> {
                return "Intermission";
            }
            case Half2 -> {
                return "Half 2";
            }
            case Overtime1 -> {
                return "Overtime 1";
            }
            case Overtime2 -> {
                return "Overtime 2";
            }
            case Penalty -> {
                return "Penalty";
            }
            case RefereeTimeout -> {
                return "RefereeTimeout";
            }
            case LeftTeamTimeout -> {
                return "LeftTeamTimeout";
            }
            case RightTeamTimeout -> {
                return "RightTeamTimeout";
            }
            default -> {
                return null;
            }
        }
    }
}
