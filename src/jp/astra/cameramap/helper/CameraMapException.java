package jp.astra.cameramap.helper;


import android.content.Context;

/**
 * @author Lau
 */
public class CameraMapException extends Exception {

    private final static long serialVersionUID = 1L;

    public CameraMapException(Context context, String message) {

        super(message);
        Logger.warn(context, message);
    }

    public CameraMapException(Context context, String message, Throwable t) {

        super(message, t);

        message = message + "\n  Exception: " + t.getClass().getName();
        message = message + "\n  Message: " + t.getMessage();

        StackTraceElement stack[] = t.getStackTrace();

        for (int i = 0; i < stack.length; i++) {
            message = message + "\n    Stack " + i + ": " + stack[i].getClassName() + ":" + stack[i].getMethodName() + "(" + stack[i].getFileName() + ":" + stack[i].getLineNumber() + ")";
        }

        Logger.error(context, message);
    }
}
