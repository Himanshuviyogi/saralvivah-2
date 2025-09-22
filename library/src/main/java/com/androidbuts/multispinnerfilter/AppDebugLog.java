package com.androidbuts.multispinnerfilter;

import android.util.Log;

/**
 * App log related methods
 * Created by Nasirali on 02-02-2019.
 */

public class AppDebugLog {
	private static boolean isProductionMode;
	private static boolean isFileLogMode;

	public static void setProductionMode(boolean isProductionMode) {
		AppDebugLog.isProductionMode = isProductionMode;
	}

	public static void setFileLogMode(boolean isFileLogMode) {
		AppDebugLog.isFileLogMode = isFileLogMode;
	}

	//use for normal log
	public static void print(String message) {
		// if not in production, then print log on console
		if (!isProductionMode) Log.d("AppDebug : ", " Debug :" + message);


		// log in file for testing mode debug
		if (isFileLogMode) { }
	}

	//use for warning log
	public static void printWarning(String message) {
		// if not in production, then print log on console
		if (!isProductionMode) Log.w("AppDebug : ", " Warning Debug:" + message);

	}

	//use for error log
	public static void printError(String message) {
		// if not in production, then print log on console
		if (!isProductionMode) Log.e("AppDebug : ", " Error Debug:" + message);
	}

	public static void printArray(Object[] message) {

		// if not in production, then print log on console
		if (!isProductionMode) {
			for (Object object : message) {
				System.out.println(object);
			}

		}

		// log in file for testing mode debug
		if (isFileLogMode) {

		}
	}

}
