package de.metux.nanoweb.core;

/**
 * Logging helpers
 *
 * Currently using stderr - future versions will use syslog
 */
public class Log {
	/**
	 * log error message
	 *
	 * @param unit	logging unit
	 * @param msg	message
	 */
	public static final void error(String unit, String msg) {
		System.err.println("ERR ["+unit+"] "+msg);
	}

	/**
	 * log error message with exception
	 *
	 * @param unit	logging unit
	 * @param msg	message
	 * @param e	exception
	 */
	public static final void error(String unit, String msg, Exception e) {
		System.err.println("ERR ["+unit+"] "+msg+" "+e);
		e.printStackTrace();
	}

	/**
	 * log warning message
	 *
	 * @param unit	logging unit
	 * @param msg	message
	 */
	public static final void warning(String unit, String msg) {
		System.err.println("WARN ["+unit+"] "+msg);
	}

	/**
	 * log warning message with exception
	 *
	 * @param unit	logging unit
	 * @param msg	message
	 * @param e	exception
	 */
	public static final void warning(String unit, String msg, Exception e) {
		System.err.println("WARN ["+unit+"] "+msg+" "+e);
		e.printStackTrace();
	}

	/**
	 * log info message
	 *
	 * @param unit	logging unit
	 * @param msg	message
	 */
	public static final void info(String unit, String msg) {
		System.err.println("INFO ["+unit+"] "+msg);
	}

	/**
	 * log info message with exception
	 *
	 * @param unit	logging unit
	 * @param msg	message
	 * @param e	exception
	 */
	public static final void info(String unit, String msg, Exception e) {
		System.err.println("INFO ["+unit+"] "+msg+" "+e);
		e.printStackTrace();
	}

	/**
	 * log debug message
	 *
	 * @param unit	logging unit
	 * @param msg	message
	 */
	public static final void debug(String unit, String msg) {
		System.err.println("DEBUG ["+unit+"] "+msg);
	}

	/**
	 * log debug message with exception
	 *
	 * @param unit	logging unit
	 * @param msg	message
	 * @param e	exception
	 */
	public static final void debug(String unit, String msg, Exception e) {
		System.err.println("DEBUG ["+unit+"] "+msg+" "+e);
		e.printStackTrace();
	}
}
