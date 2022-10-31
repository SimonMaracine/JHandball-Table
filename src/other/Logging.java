package other;

import java.util.Date;
import java.util.logging.*;

public class Logging {
    private static final Logger logger = Logger.getLogger("main");

    public static void initialize() {
        LogManager.getLogManager().reset();

        var handler = new ConsoleHandler();

        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                return String.format("[%s] [%s] %s\n", logRecord.getLevel(), new Date(), logRecord.getMessage());
            }
        });

        logger.addHandler(handler);
        logger.setLevel(Level.INFO);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void severe(String message) {
        logger.severe(message);
    }
}
