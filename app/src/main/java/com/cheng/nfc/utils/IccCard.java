package com.cheng.nfc.utils;

public class IccCard {
  public static byte[] CMD_GET_RANDOM;
  
  public static byte[] CMD_GET_RANDOM4;
  
  public static byte[] CMD_GET_RANDOM8;
  
  public static final byte[] CMD_START = { 0, -92, 0, 0, 2, 63, 0 };
  
  public static final byte[] PIN;
  
  public static final byte[] READ_11;
  
  public static final byte[] READ_15;
  
  public static final byte[] READ_16;
  
  public static final byte[] READ_19;
  
  public static final byte[] SWITCH_PBOC = { 0, -92, 0, 0, 2, 16, 1 };
  
  public static String _PRE_CONSUME;
  
  public static String _PRE_CONSUME_INIT;
  
  public static String _READ_ELECTRONIC_WALLET;
  
  public static String _RESET_0019;
  
  static  {
    READ_15 = new byte[] { 0, -80, -107, 0, 43 };
    READ_19 = new byte[] { Byte.MIN_VALUE, 92, 0, 2, 4 };
    READ_16 = new byte[] { 0, -80, -106, 0, 55 };
    READ_11 = new byte[] { 0, -80, -111, 0, 39 };
    CMD_GET_RANDOM = new byte[] { 0, -124, 0, 0, 8 };
    PIN = new byte[] { 
        0, 32, 0, 0, 6, 49, 50, 51, 52, 53, 
        54 };
    CMD_GET_RANDOM8 = new byte[] { 0, -124, 0, 0, 8 };
    CMD_GET_RANDOM4 = new byte[] { 0, -124, 0, 0, 4 };
    _READ_ELECTRONIC_WALLET = "805C000204";
    _PRE_CONSUME_INIT = "805003020B01";
    _RESET_0019 = "80DCAAC82BAA290000000000000000000000020000000000000000000000000000000000000000000000000000000000";
    _PRE_CONSUME = "805401000F00000000";
  }
}


/* Location:              /Users/chengyue/Documents/Development/DevelopmentTool/apktool/dex2jar-2.0/app-debug-dex2jar.jar!/com/cheng/nfc/utils/IccCard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */