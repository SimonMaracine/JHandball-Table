package handball;

import timer.Timer;

public class SuspendedPlayer {
    private final Timer timer;
    private final Player player;

    public SuspendedPlayer(Timer timer, Player player) {
        this.timer = timer;
        this.player = player;
    }

    public Timer getTimer() {
        return timer;
    }

    public Player getPlayer() {
        return player;
    }
}
