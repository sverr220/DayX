package dayx.home13.ru.dayx.utils;

import android.util.Log;

public class Logger {
	private static final String tag = "ru.taximaster.www";
	/** Вести ли лог информации */
	private static final boolean INFO_LOG_ENABLED = true;
	/** Вести ли лог еррора */
	private static final boolean ERROR_LOG_ENABLED = true;

	private static final String LOG_PATH = "/TMDriver.log";

	public static void debug(final String text) {
		if (INFO_LOG_ENABLED) {
			String msg = "[debug]" + getFullTrace() + ": ";
			Log.d(tag, msg + text);
		}
	}

	public static void info(final String text) {
		if (INFO_LOG_ENABLED) {
			String msg = "[info]: ";
			Log.i(tag, msg + text);
		}
	}

	public static void error(final String text) {
		if (ERROR_LOG_ENABLED) {
			String msg = "[error]" + getFullTrace() + ": ";
			Log.e(tag, msg + text);
		}
	}

	public static void error(Exception e) {
		if (ERROR_LOG_ENABLED) {
			String msg = "[error]" + getTrace(e) + ": ";
			Log.e(tag, msg + e.toString());
		}
	}

	private static String getFullTrace() {
		String result;
		try {
			throw new Exception();
		} catch (Exception e) {
			result = e.getStackTrace()[2].getClassName() + ": ";
			for (int i = 2; i < e.getStackTrace().length - 8; i++)
				result = result + e.getStackTrace()[i].getMethodName() + ": "
						+ e.getStackTrace()[i].getLineNumber() + " ";
		}
		return result;
	}

	private static String getTrace(Exception e) {
		String result = e.getStackTrace()[0].getClassName() + ": ";
		for (int i = 0; i < e.getStackTrace().length - 1; i++)
			result = result + e.getStackTrace()[i].getMethodName() + ": "
					+ e.getStackTrace()[i].getLineNumber() + " ";
		return result;
	}
}
