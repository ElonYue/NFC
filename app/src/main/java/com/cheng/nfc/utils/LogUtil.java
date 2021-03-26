package com.cheng.nfc.utils;

import android.text.TextUtils;
import android.util.Log;

public final class LogUtil {

//  static Logger logger = LoggerFactory.getLogger(LogUtil.class);
  private static String TAG = "[NFC]";
  public static final int LEVEL = Log.ERROR;

  private static String className;
  private static String methodName;
  private static int lineNumber;

  private static boolean isDebuggable() {
//        return BuildConfig.DEBUG || FlavorUtils.isTest();
    return true;
  }

  // 拼接 方法名+行号+log
  private static String createLog(String tag, String log) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(TextUtils.isEmpty(tag) ? "(NFC)" : "(" + tag + ")");
    buffer.append("[");
    buffer.append(methodName);
//        buffer.append(":");
//        buffer.append(lineNumber);
    buffer.append("]");
    buffer.append(log);
    return buffer.toString();
  }

  private static void getMethodNames() {
    //判断是否含有 lambda 表达式
    boolean next = false;
    StackTraceElement[] traceElements = new Throwable().getStackTrace();
    for (StackTraceElement value : traceElements) {
      if (value.getMethodName().startsWith("lambda")) {
        next = true;
      }
    }
    String className = traceElements[3].getClassName();
    if (className.isEmpty()) {
      return;
    } else if (className.contains("$")) { //用于内部类的名字解析
      className = className.substring(className.lastIndexOf(".") + 1, className.indexOf("$"));
    } else {
      className = className.substring(className.lastIndexOf(".") + 1, className.length());
    }

    if (!next) {
      methodName = traceElements[3].getMethodName();
    } else {
      methodName = traceElements[5].getMethodName();
    }
    lineNumber = traceElements[3].getLineNumber();
    //生成指向java的字符串 加入到TAG标签里面
    TAG = "(" + className + ".java:" + lineNumber + ")";
  }

  public static void v(String message) {
    logger(Log.VERBOSE, "", message);
  }

  public static void v(String tag, String message) {
    logger(Log.VERBOSE, tag, message);
  }

  public static void d(String message) {
    logger(Log.DEBUG, "", message);
  }

  public static void d(String tag, String message) {
    logger(Log.DEBUG, tag, message);
  }

  public static void i(String message) {
    logger(Log.INFO, "", message);
  }

  public static void i(String tag, String message) {
    logger(Log.INFO, tag, message);
  }

  public static void w(String message) {
    logger(Log.WARN, "", message);
  }

  public static void w(String tag, String message) {
    logger(Log.WARN, tag, message);
  }

  public static void e(String message) {
    logger(Log.ERROR, "", message);
  }

  public static void e(String tag, String message) {
    logger(Log.ERROR, tag, message);
  }

  public static void e(Exception e) {
    logger(Log.ERROR, "", Log.getStackTraceString(e));
  }

  public static void e(String tag, Exception e) {
    logger(Log.ERROR, tag, Log.getStackTraceString(e));
  }

  private static void logger(int level, String tag, String message) {
    if (!isDebuggable() || level > LEVEL) {
      return;
    }
    getMethodNames();

    if (level == Log.VERBOSE) {
      Log.v(TAG, createLog(tag, message));
    } else if (level == Log.DEBUG) {
      Log.d(TAG, createLog(tag, message));
    } else if (level == Log.INFO) {
      Log.i(TAG, createLog(tag, message));
    } else if (level == Log.WARN) {
      Log.w(TAG, createLog(tag, message));
    } else if (level == Log.ERROR) {
      Log.e(TAG, createLog(tag, message));
    }
//    logger.info(TAG + " : " + message);
  }
}