package handball;

import timer.Timer;

public class SuspendedPlayer {
    Timer timer;
    Player player;

    public Timer getTimer() {
        return timer;
    }

    public Player getPlayer() {
        return player;
    }

    public SuspendedPlayer(Timer timer, Player player) {
        this.timer = timer;
        this.player = player;
    }
}
