package studentskills.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * A Simple Logging utility that lets users print logs, to a console and to an
 * output file.
 * 
 * This is a Singleton class, and will only be initialized once.
 * 
 * @author Harshit Vadodaria
 *
 */
public class Logger {

	protected static Logger instance = null;
	protected final Level level, logtoFileForLevel;
	protected final FileWriter fw;

	/**
	 * Initializes the logger with the provided level and log file. Call this
	 * function only once, and either pass the instance around, or use the static
	 * {@link #getInstance()} method to get the Logger's instance, and call the
	 * {@link #log(Level, String, Object...)} method. A better approach would be to
	 * call the various static logging methods available for each level:
	 * <ul>
	 * <li>{@link #error(String, Throwable, Object...)}</li>
	 * <li>{@link #warn(String, Object...)}</li>
	 * <li>{@link #info(String, Object...)}</li>
	 * <li>{@link #config(String, Object...)}</li>
	 * <li>{@link #debugLow(String, Object...)}</li>
	 * <li>{@link #debugMed(String, Object...)}</li>
	 * <li>{@link #debugHigh(String, Object...)}</li>
	 * </ul>
	 * 
	 * @param level             Level to log at (Refer {@link Level})
	 * @param logFile           Name of log file
	 * @param logtoFileForLevel If this is not null, only the logs at this level
	 *                          will be printed to the log file, else all the logs
	 *                          (with levels equal to or less than the constructor
	 *                          parameter {@code level}) will be printed to the log
	 *                          file. Regardless of this parameter, all the logs are
	 *                          printed to the console.
	 * @throws IOException
	 */
	public Logger(Level level, String logFile, Level logtoFileForLevel) throws IOException {
		if (instance != null)
			throw new RuntimeException("Logger already initialized, cannot initialize it again");
		this.level = level;
		this.fw = new FileWriter(logFile);
		this.logtoFileForLevel = logtoFileForLevel;
		Logger.instance = this;
	}

	public static Logger getInstance() {
		if (Logger.instance == null)
			throw new RuntimeException("Logger not initialized. Initialize it using the constructor new Logger(...)");
		return Logger.instance;
	}

	public void log(Level level, String msg, Object... args) {
		if (level.toInt() <= this.level.toInt()) {
			String logMsg = String.format("[%s][%s]%s %s", new Date(), level, msg,
					(args == null || args.length == 0) ? "" : "(" + (args.length == 1 ? args[0] : args) + ")");
			if (level == Level.ERROR)
				System.err.println(logMsg);
			else
				System.out.println(logMsg);
			if (this.logtoFileForLevel == null || this.logtoFileForLevel == level) {
				try {
					this.fw.write(logMsg + "\n");
					this.fw.flush();
				} catch (IOException e) {
					System.err.println("Failed to write to log file");
					e.printStackTrace();
				}
			}
		}
	}

	public static void debugHigh(String msg, Object... args) {
		Logger.getInstance().log(Level.DEBUG_HIGH, msg, args);
	}

	public static void debugMed(String msg, Object... args) {
		Logger.getInstance().log(Level.DEBUG_MED, msg, args);
	}

	public static void debugLow(String msg, Object... args) {
		Logger.getInstance().log(Level.DEBUG_LOW, msg, args);
	}

	public static void config(String msg, Object... args) {
		Logger.getInstance().log(Level.CONFIG, msg, args);
	}

	public static void info(String msg, Object... args) {
		Logger.getInstance().log(Level.INFO, msg, args);
	}

	public static void warn(String msg, Object... args) {
		Logger.getInstance().log(Level.WARN, msg, args);
	}

	public static void error(String msg, Throwable t, Object... args) {
		Logger.getInstance().log(Level.ERROR, msg, args);
		if (t != null)
			t.printStackTrace();
	}

	public static enum Level {
		ERROR(1), WARN(2), INFO(3), CONFIG(4), DEBUG_LOW(5), DEBUG_MED(6), DEBUG_HIGH(7),;

		private final int levelNum;

		Level(int levelNum) {
			this.levelNum = levelNum;
		}

		public int toInt() {
			return this.levelNum;
		}

		public static Level from(int levelInt) {
			for (Level level : Level.values())
				if (level.toInt() == levelInt)
					return level;
			throw new RuntimeException("Invalid log level number: [" + levelInt + "]");
		}
	}
}
