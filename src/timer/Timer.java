package timer;

import javax.swing.*;

public class Timer implements Runnable {
    private static final long ONE_SECOND = 1000;

    private int time = 0;  // In seconds
    private boolean isRunning = false;
    private Thread thread;

    private long lastSystemTime = 0;  // In milliseconds, from System

    private final JLabel lblTime;

    public Timer(JLabel lblTime) {
        this.lblTime = lblTime;
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

    public void start() throws TimerException {  // TODO maybe don't use exceptions (depends on the project's requirements)
        if (isRunning) {
            throw new TimerException("Timer already running");
        }

        isRunning = true;

        thread = new Thread(this);
        thread.start();
    }

    public void stop() throws TimerException {
        if (!isRunning) {
            throw new TimerException("Timer already stopped");
        }

        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            final long currentSystemTime = System.currentTimeMillis();

            if (currentSystemTime > lastSystemTime + ONE_SECOND) {
                lastSystemTime = currentSystemTime;

                time++;

                SwingUtilities.invokeLater(() -> lblTime.setText(getTimeFormatted()));
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {}
        }
    }
}
