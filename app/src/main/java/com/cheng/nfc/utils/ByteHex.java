package com.cheng.nfc.utils;

import android.text.TextUtils;
import android.util.Log;
import java.io.UnsupportedEncodingException;

public class ByteHex {
  private String hexStr;
  private byte[] bs;
  private static String hexString = "0123456789ABCDEF";

  public ByteHex(byte[] bs) {
    this.bs = bs;
  }

  public ByteHex(byte[] frame, int frameLen) {
    if (frame.length == frameLen) {
      this.bs = frame;
    } else {
      this.bs = new byte[frameLen];
      System.arraycopy(frame, 0, this.bs, 0, frameLen);
    }

  }

  public ByteHex(String hexStr) {
    this.hexStr = hexStr;
  }

  public byte[] toBytes() {
    if (this.bs == null) {
      this.bs = new byte[this.hexStr.length() / 2];

      for(int i = 0; i < this.hexStr.length(); i += 2) {
        String b = this.hexStr.substring(i, i + 2);
        this.bs[i / 2] = (byte)Integer.parseInt(b, 16);
      }
    }

    return this.bs;
  }

  public static int hex2decimal(String hex) {
    return Integer.parseInt(hex, 16);
  }

  public static int hexToInt(String hex) {
    int javaReadInt = Integer.parseInt(hex, 16);
    byte byte4 = (byte)(javaReadInt & 255);
    byte byte3 = (byte)((javaReadInt & '\uff00') >> 8);
    int realint = (byte3 & 255) << 0 | (byte4 & 255) << 8;
    return realint;
  }

  public static byte[] strArray_to_byteArray(String[] strArray) {
    byte[] bytes = new byte[strArray.length];

    for(int i = 0; i < strArray.length; ++i) {
      bytes[i] = Byte.parseByte(strArray[i]);
    }

    return bytes;
  }

  public static String lowHigh(int var0) {
    int var1 = 1;
    int var2 = var0 >> 8;
    int var3 = var0 & 255;
    String var4 = Integer.toHexString(var2);
    String var5 = Integer.toHexString(var3);
    if (var4.length() > 2) {
      do {
        if (var1 > 1) {
          var2 >>= 8;
        }

        var4 = Integer.toHexString(var2 >> 8);
        var5 = var5 + Integer.toHexString(var2 & 255);
        ++var1;
      } while(var4.length() > 2);
    }

    if (var4.length() < 2) {
      var4 = "0" + var4;
    }

    if (var5.length() < 2) {
      var5 = "0" + var5;
    }

    return var5 + var4;
  }

  public static int bigBytesToInt(byte[] bytes, int len) {
    int number = 0;
    if (len > 0 && len <= 4) {
      for(int i = 0; i < len; ++i) {
        number <<= 8;
        number |= bytes[i] & 255;
      }

      return number;
    } else {
      return 0;
    }
  }

  public static byte[] hexToBin(String shex) {
    int j = 0;
    byte val = 0;
    boolean flag = true;
    if (shex == null) {
      return null;
    } else {
      int len = shex.length() / 2;
      if (len == 0) {
        return null;
      } else {
        byte[] sbin = new byte[len];

        for(int i = 0; i < shex.length(); ++i) {
          flag = !flag;
          char ch = shex.charAt(i);
          byte temp;
          if (ch >= '0' && ch <= '9') {
            temp = (byte)(ch - 48);
          } else if (ch >= 'a' && ch <= 'f') {
            temp = (byte)(ch - 97 + 10);
          } else {
            if (ch < 'A' || ch > 'F') {
              return null;
            }

            temp = (byte)(ch - 65 + 10);
          }

          if (flag) {
            sbin[j++] = (byte)(val << 4 | temp);
          } else {
            val = temp;
          }
        }

        return sbin;
      }
    }
  }

  public static byte[] subBytes(byte[] src, int begin, int count) {
    byte[] bs = new byte[count];
    System.arraycopy(src, begin, bs, 0, count);
    return bs;
  }

  public static String bcd2Str(byte[] bytes) {
    StringBuffer temp = new StringBuffer(bytes.length * 2);

    for(int i = 0; i < bytes.length; ++i) {
      temp.append((byte)((bytes[i] & 240) >>> 4));
      temp.append((byte)(bytes[i] & 15));
    }

    return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
  }

  public static byte intToByte(int x) {
    return (byte)x;
  }

  public static int byteToInt(byte b) {
    return b & 255;
  }

  public static int byteArrayToInt(byte[] b) {
    return b[3] & 255 | (b[2] & 255) << 8 | (b[1] & 255) << 16 | (b[0] & 255) << 24;
  }

  public static int byteArrayToInt(byte[] b, int index) {
    return b[index + 3] & 255 | (b[index + 2] & 255) << 8 | (b[index + 1] & 255) << 16 | (b[index + 0] & 255) << 24;
  }

  public static int highLowExchangeToInt(byte[] b) {
    return b[0] & 255 | (b[1] & 255) << 8;
  }

  public static int byteArrToInt(byte[] b) {
    int s = 0;
    if (b.length == 1) {
      s = b[0] & 255;
    } else if (b.length == 2) {
      s = b[1] & 255 | (b[0] & 255) << 8;
    } else if (b.length == 3) {
      s = b[2] & 255 | (b[1] & 255) << 8 | (b[0] & 255) << 16;
    } else {
      s = b[3] & 255 | (b[2] & 255) << 8 | (b[1] & 255) << 16 | (b[0] & 255) << 24;
    }

    return s;
  }

  public static byte[] intToByteArray(int a) {
    return new byte[]{(byte)(a >> 24 & 255), (byte)(a >> 16 & 255), (byte)(a >> 8 & 255), (byte)(a & 255)};
  }

  public static String intToHex16(int b) {
    return String.format("%02x", b);
  }

  public static String intToHexByLength(int b, int byteLength) {
    int length = byteLength * 2;
    String hex = intToHex16(b);
    hex = hex.length() < length ? hex + "0" : hex;
    return hex;
  }

  public static String byteToString(byte[] buf, int index, int num) {
    if (buf[index] == 0) {
      return null;
    } else {
      int i;
      for(i = 0; i < num && buf[index + i] != 0; ++i) {
      }

      byte[] dest = new byte[i];
      System.arraycopy(buf, index, dest, 0, i);
      String s = null;

      try {
        s = new String(dest, "gbk");
      } catch (UnsupportedEncodingException var7) {
        var7.printStackTrace();
      }

      return s;
    }
  }

  public static boolean checkRs(byte[] resp) {
    String r = printByte(resp);
    Log.i("---------", "response " + r);
    int status = (255 & resp[resp.length - 2]) << 8 | 255 & resp[resp.length - 1];
    return status == 36864;
  }

  private static String printByte(byte[] data) {
    StringBuffer bf = new StringBuffer();
    byte[] var2 = data;
    int var3 = data.length;

    for(int var4 = 0; var4 < var3; ++var4) {
      byte b = var2[var4];
      bf.append(Integer.toHexString(b & 255));
      bf.append(",");
    }

    Log.i("TAG", bf.toString());
    return bf.toString();
  }

  public static String bytesToHexString(byte[] src) {
    StringBuilder stringBuilder = new StringBuilder("0x");
    if (src != null && src.length > 0) {
      char[] buffer = new char[2];

      for(int i = 0; i < src.length; ++i) {
        buffer[0] = Character.forDigit(src[i] >>> 4 & 15, 16);
        buffer[1] = Character.forDigit(src[i] & 15, 16);
        stringBuilder.append(buffer);
      }

      return stringBuilder.toString();
    } else {
      return null;
    }
  }

  public static String bytesToHex(byte[] bytes) {
    StringBuffer sb = new StringBuffer();

    for(int i = 0; i < bytes.length; ++i) {
      String hex = Integer.toHexString(bytes[i] & 255);
      if (hex.length() < 2) {
        sb.append(0);
      }

      sb.append(hex);
    }

    return sb.toString();
  }

  public static int HexStringToInt(String HexString) {
    int inJTFingerLockAddress = Integer.valueOf(HexString, 16);
    return inJTFingerLockAddress;
  }

  public static String intToHex(int num) {
    String s = Integer.toHexString(num);
    return s;
  }

  public static String intToHex2(int num) {
    String s = Integer.toHexString(num);
    return s;
  }

  public static String validate3MAC(String fullMac) {
    String _3Mac = "";
    if (fullMac.length() > 16) {
      _3Mac = fullMac.substring(18, 26).toUpperCase();
    }

    return _3Mac;
  }

  public static byte[] addBytes(byte[] data1, byte[] data2) {
    byte[] data3 = new byte[data1.length + data2.length];
    System.arraycopy(data1, 0, data3, 0, data1.length);
    System.arraycopy(data2, 0, data3, data1.length, data2.length);
    return data3;
  }

  public static byte[] hexString2Bytes(String hex) {
    if (hex != null && !hex.equals("")) {
      if (hex.length() % 2 != 0) {
        return null;
      } else {
        hex = hex.toUpperCase();
        int len = hex.length() / 2;
        byte[] b = new byte[len];
        char[] hc = hex.toCharArray();

        for(int i = 0; i < len; ++i) {
          int p = 2 * i;
          b[i] = (byte)(charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
        }

        return b;
      }
    } else {
      return null;
    }
  }

  private static byte charToByte(char c) {
    return (byte)"0123456789ABCDEF".indexOf(c);
  }

  public static String encodeStrToHex(String str, String charset) {
    String tmpStr = "";
    byte[] bytes = new byte[0];

    try {
      bytes = str.getBytes(charset);
      StringBuilder sb = new StringBuilder(bytes.length * 2);

      for(int i = 0; i < bytes.length; ++i) {
        sb.append(hexString.charAt((bytes[i] & 240) >> 4));
        sb.append(hexString.charAt((bytes[i] & 15) >> 0));
      }

      tmpStr = sb.toString();
    } catch (UnsupportedEncodingException var6) {
      var6.printStackTrace();
      tmpStr = str;
    }

    return tmpStr;
  }

  public static byte[] simpleAPDU(int length, String cmd) {
    byte[] data = new byte[cmd.length() / 2];

    for(int j = 0; j < length; ++j) {
      byte[] temp = hexToBin(cmd);
      System.arraycopy(temp, 0, data, 0, temp.length);
    }

    return data;
  }

  public static byte[] bigIntToBytes(int num) {
    byte[] abyte0 = new byte[]{(byte)(255 & num), (byte)(('\uff00' & num) >> 8), (byte)((16711680 & num) >> 16), (byte)((-16777216 & num) >> 24)};
    return abyte0;
  }

  public static boolean isRightSJLReturn(String str) {
    boolean isRightSJLStr = false;
    if (!TextUtils.isEmpty(str) && str.trim().startsWith("41")) {
      isRightSJLStr = true;
    }

    return isRightSJLStr;
  }

  public String toString() {
    if (this.hexStr == null) {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < this.bs.length; ++i) {
        sb.append(String.format("%02X", this.bs[i]));
      }

      this.hexStr = sb.toString();
    }

    return this.hexStr;
  }
}