package handball;

import timer.Timer;

public class SuspendedPlayer {
    public static final int SUSPENSION_TIME = 2;// * 60;  // FIXME make this right!!!

    private Timer timer;
    private final Player player;
    private int index;

    public SuspendedPlayer(Timer timer, Player player) {
        this.timer = timer;
        this.player = player;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Player getPlayer() {
        return player;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
