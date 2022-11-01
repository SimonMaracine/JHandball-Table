package timer;

import other.Logging;

import javax.swing.*;

public class Timer implements Runnable {
    private static final long ONE_SECOND = 1000;
    private static final int MAX_TIME = 60 * 99 + 59;

    private int time = 0;  // In seconds
    private boolean isRunning = false;
    private Thread thread;
    private final int timeUntilStop;
    private final TimerStopCallback stopCallback;

    private final JLabel lblTime;

    public Timer(JLabel lblTime, int timeUntilStop, TimerStopCallback stopCallback) {
        this.lblTime = lblTime;
        this.timeUntilStop = Math.min(timeUntilStop, MAX_TIME);
        this.stopCallback = stopCallback;
    }

    public int getTime() {
        return time;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getTimeFormatted() {
        final int minutes = time / 60;
        final int seconds = time % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void start() {
        assert !isRunning;

        isRunning = true;

        thread = new Thread(this);
        thread.start();

        Logging.info("Started timer");
    }

    public void stop() {
        assert isRunning;

        isRunning = false;

        Logging.info("Stopped timer");
    }

    @Override
    public void run() {
        long lastSystemTime = System.currentTimeMillis();

        while (isRunning) {
            final long currentSystemTime = System.currentTimeMillis();

            if (currentSystemTime > lastSystemTime + ONE_SECOND) {  // TODO this can be improved
                lastSystemTime = currentSystemTime;

                time++;

                if (time >= timeUntilStop) {
                    stop();
                    SwingUtilities.invokeLater(stopCallback::execute);
                }

                SwingUtilities.invokeLater(() -> lblTime.setText(getTimeFormatted()));
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {}
        }
    }
}
