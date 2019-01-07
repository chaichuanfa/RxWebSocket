package com.felix.rxwebsocket;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

final class Logger {

    private static final String TAG = "RxWebSocket";

    private static boolean mIsDebug;

    private Logger() {
    }

    static void setDebug(boolean debug) {
        mIsDebug = debug;
    }

    public static void e(String message) {
        if (mIsDebug) {
            Log.e(TAG, message);
        }
    }

    public static void e(Throwable t, String message) {
        if (mIsDebug) {
            Log.e(TAG, message + "\n" + getStackTraceString(t));
        }
    }

    public static void d(String message) {
        if (mIsDebug) {
            Log.d(TAG, message);
        }
    }

    public static void i(String message) {
        if (mIsDebug) {
            Log.i(TAG, message);
        }
    }

    public static void v(String message) {
        if (mIsDebug) {
            Log.v(TAG, message);
        }
    }

    public static void w(String message) {
        if (mIsDebug) {
            Log.w(TAG, message);
        }
    }

    private static String getStackTraceString(Throwable t) {
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
