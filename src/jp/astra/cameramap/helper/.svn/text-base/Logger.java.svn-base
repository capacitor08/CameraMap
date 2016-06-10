package jp.astra.cameramap.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * @category Helper
 * @author Kelvzs
 * @updated Kat
 */
public final class Logger {

    private static String[] LogHeader = {"debug: ", "warn: ", "ERROR: "};
    private static int DEBUG = 0;
    private static int WARNING = 1;
    private static int ERROR = 2;

    private static void write(Context context, int type, String message) {

        Log.d(Logger.LogHeader[type], message);

        if (Util.isMediaAvailable(context)) {

        	try {
        		Date currentDate = new Date();
        		String fileName = DateFormat.format("yyyyMMdd.log", currentDate).toString();
        		String timeStamp = DateFormat.format("yyyy-MM-dd hh:mm:ss", currentDate).toString();

        		File file = new File(Util.getLogPath(context) + fileName);
        		FileOutputStream stream = new FileOutputStream(file, true);

        		stream.write((timeStamp + " " + Logger.LogHeader[type] + "\n" + message + "\n").getBytes());
        		stream.close();

        	} catch (Throwable t) {}
        }
    }

    public static void debug(Context context, String message) {
        Logger.write(context, Logger.DEBUG, message);
    }

    public static void warn(Context context, String message) {
        Logger.write(context, Logger.WARNING, message);
    }

    public static void error(Context context, String message) {
        Logger.write(context, Logger.ERROR, message);
    }

    public static void debug(Context context, String message, Throwable t) {
        Logger.write(context, Logger.DEBUG, message + ": " + t.getMessage());
    }

    public static void warn(Context context, String message, Throwable t) {
        Logger.write(context, Logger.WARNING, message + ": " + t.getMessage());
    }

    public static void error(Context context, String message, Throwable t) {
        Logger.write(context, Logger.ERROR, message + ": " + t.getMessage());
    }
}
