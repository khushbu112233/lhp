package com.aip.targascan.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * This class includes Log methods to be used in application.
 * 
 * @author aipxperts
 */
public class Logger {

	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");

	public static void info(String tag, String msg) {
		Log.i(tag, " [ " + dateToString(new Date()) + " ] " + msg);
	}

	public static void debug(String tag, String msg) {
		Log.d(tag, "[ " + dateToString(new Date()) + " ] " + msg);
	}

	public static void error(String tag, String msg) {
		Log.e(tag, " [ " + dateToString(new Date()) + " ]" + msg);
	}

	public static void verbose(String tag, String msg) {
		Log.v(tag, " [ " + dateToString(new Date()) + " ] " + msg);
	}

	public static void warning(String tag, String msg) {
		Log.w(tag, " [ " + dateToString(new Date()) + " ] " + msg);
	}

	protected static String dateToString(Date date) {
		return sdf.format(date);
	}
}