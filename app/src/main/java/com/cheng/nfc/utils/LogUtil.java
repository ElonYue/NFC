package com.cheng.nfc.utils;

import android.text.TextUtils;
import android.util.Log;
import com.cheng.nfc.BuildConfig;

public final class LogUtil {
  public static final int ERROR = 5;
  
  private static String TAG = "[NFCTEST]";
  
  private static String className;
  
  public static int level = 5;
  
  private static int lineNumber;
  
  private static String methodName;
  
  private static String createLog(String paramString) { return createLog("handsetETC", paramString); }
  
  private static String createLog(String paramString1, String paramString2) {
    StringBuffer stringBuffer = new StringBuffer();
    if (TextUtils.isEmpty(paramString1)) {
      paramString1 = "(handsetETC)";
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("(");
      stringBuilder.append(paramString1);
      stringBuilder.append(")");
      paramString1 = stringBuilder.toString();
    } 
    stringBuffer.append(paramString1);
    stringBuffer.append("[");
    stringBuffer.append(methodName);
    stringBuffer.append("]");
    stringBuffer.append(paramString2);
    return stringBuffer.toString();
  }
  
  public static void d(String paramString) {
    logger(paramString);
    if (!isDebuggable())
      return; 
    Log.d(TAG, createLog("", paramString));
  }
  
  public static void d(String paramString1, String paramString2) {
    logger(paramString2);
    if (!isDebuggable())
      return; 
    Log.d(TAG, createLog(paramString1, paramString2));
  }
  
  public static void e(String paramString) {
    logger(paramString);
    if (!isDebuggable())
      return; 
    Log.e(TAG, createLog("", paramString));
  }
  
  public static void e(String paramString1, String paramString2) {
    logger(paramString2);
    if (!isDebuggable())
      return; 
    Log.e(TAG, createLog(paramString1, paramString2));
  }
  
  private static void getMethodNames() {
    boolean bool = false;
    StackTraceElement[] arrayOfStackTraceElement = (new Throwable()).getStackTrace();
    int i = arrayOfStackTraceElement.length;
    for (byte b = 0; b < i; b++) {
      if (arrayOfStackTraceElement[b].getMethodName().startsWith("lambda"))
        bool = true; 
    } 
    String str = arrayOfStackTraceElement[3].getClassName();
    if (str.isEmpty())
      return; 
    if (str.contains("$")) {
      str = str.substring(str.lastIndexOf(".") + 1, str.indexOf("$"));
    } else {
      str = str.substring(str.lastIndexOf(".") + 1, str.length());
    } 
    if (!bool) {
      methodName = arrayOfStackTraceElement[3].getMethodName();
    } else {
      methodName = arrayOfStackTraceElement[5].getMethodName();
    } 
    lineNumber = arrayOfStackTraceElement[3].getLineNumber();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(");
    stringBuilder.append(str);
    stringBuilder.append(".java:");
    stringBuilder.append(lineNumber);
    stringBuilder.append(")");
    TAG = stringBuilder.toString();
  }
  
  public static void i(String paramString) {
    logger(paramString);
    if (!isDebuggable())
      return; 
    Log.i(TAG, createLog("", paramString));
  }
  
  public static void i(String paramString1, String paramString2) {
    logger(paramString2);
    if (!isDebuggable())
      return; 
    Log.i(TAG, createLog(paramString1, paramString2));
  }
  
  private static boolean isDebuggable() { return BuildConfig.DEBUG; }
  
  private static void logger(String paramString) { getMethodNames(); }
  
  public static void v(String paramString) {
    logger(paramString);
    if (!isDebuggable())
      return; 
    Log.v(TAG, createLog("", paramString));
  }
  
  public static void v(String paramString1, String paramString2) {
    logger(paramString2);
    if (!isDebuggable())
      return; 
    Log.v(TAG, createLog(paramString1, paramString2));
  }
  
  public static void w(String paramString) {
    logger(paramString);
    if (!isDebuggable())
      return; 
    Log.w(TAG, createLog("", paramString));
  }
  
  public static void w(String paramString1, String paramString2) {
    logger(paramString2);
    if (!isDebuggable())
      return; 
    Log.w(TAG, createLog(paramString1, paramString2));
  }
}


/* Location:              /Users/chengyue/Documents/Development/DevelopmentTool/apktool/dex2jar-2.0/app-debug-dex2jar.jar!/com/cheng/nfc/utils/LogUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */