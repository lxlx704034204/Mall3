/***
 * This is free and unencumbered software released into the public domain.
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * For more information, please refer to <http://unlicense.org/>
 */

package com.hxqc.util;

import android.util.Log;



/**
 * author Mustafa Ferhan Akman
 *         Create a simple and more understandable Android logs.
 * @date 21.06.2012
 */

public class DebugLog {

    static String className;
    static String methodName;
    static int lineNumber;
    //    BuildConfig.DEBUG
    public static boolean debug;

    public static void setDebug(boolean debug) {
        DebugLog.debug = debug;
    }

    private DebugLog() {
        /* Protect from instantiations */
    }

    public static void e(String tag, String message) {

        if (!debug) {
            return;
        }
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(tag, className + "  " + createLog(message));
    }


    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    private static String createLog(String log) {

        return "[" + methodName + ":" + lineNumber + "]  " + log;
    }

    public static void i(String tag, String message) {
        if (!debug) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.i(tag, className + "  " + createLog(message));
    }

    public static void d(String tag, String message) {
        if (!debug) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.d(tag, className + "  " + createLog(message));
    }

    public static void v(String tag, String message) {
        if (!debug) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.v(tag, className + "  " + createLog(message));
    }

    public static void w(String tag, String message) {
        if (!debug) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.w(tag, className + "  " + createLog(message));
    }

    public static void wtf(String tag, String message) {
        if (!debug) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(tag, className + "  " + createLog(message));
    }

}
