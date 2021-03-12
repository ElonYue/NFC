package com.cheng.nfc.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ByteHex {
    private static final byte[] hex = "0123456789ABCDEF".getBytes();

    private static int parse(char c) {
        if (c >= 'a') {
            return c - 97 + 10 & 15;
        } else {
            return c >= 'A' ? c - 65 + 10 & 15 : c - 48 & 15;
        }
    }

    public static String bytesToHexString(byte[] b, int bytesLen) {
        byte[] buff = new byte[2 * bytesLen];

        for (int i = 0; i < bytesLen; ++i) {
            buff[2 * i] = hex[b[i] >> 4 & 15];
            buff[2 * i + 1] = hex[b[i] & 15];
        }

        return new String(buff);
    }

    public static String bytesToHexString(byte[] bin, int index, int len) {
        byte[] hex_str = new byte[len * 2];
        int index_str = 0;
        byte[] bin2hex = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
        String str = null;
        if (len <= 0) {
            return null;
        } else {
            for (int i = 0; i < len; ++i) {
                int b;
                if (bin[index] >= 0) {
                    b = bin[index];
                } else {
                    b = 256 + bin[index];
                }

                ++index;
                hex_str[index_str++] = bin2hex[b >> 4];
                hex_str[index_str++] = bin2hex[b & 15];
            }

            try {
                str = new String(hex_str, "GBK");
            } catch (UnsupportedEncodingException var9) {
                var9.printStackTrace();
            }

            System.out.println("binToHex:" + str);
            return str;
        }
    }

    public static byte[] hexStringToBytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;

        for (int i = 0; i < b.length; ++i) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) (parse(c0) << 4 | parse(c1));
        }

        return b;
    }

    public static String byteToString(byte[] buf, int index, int num) {
        return byteToString(buf, index, num, "GBK");
    }

    public static String byteToString(byte[] buf, int index, int num, String charsetName) {
        if (buf[index] == 0) {
            return null;
        } else {
            int i = 0;
            for (i = 0; i < num && buf[index + i] != 0; ++i) {
            }

            byte[] dest = new byte[i];
            String s = null;

            try {
                System.arraycopy(buf, index, dest, 0, i);
                s = new String(dest, charsetName);
            } catch (UnsupportedEncodingException var8) {
                var8.printStackTrace();
            }

            return s;
        }
    }

    public static byte[] stringToByte(String str) {
        return stringToByte(str, "GBK");
    }

    public static byte[] stringToByte(String str, String charsetName) {
        byte[] b = null;

        try {
            b = str.getBytes(charsetName);
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        return b;
    }

    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < strPart.length(); ++i) {
            int ch = strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }

        return hexString.toString();
    }

    public static int bytesToBigInt(byte[] bytes) {
        int tmp = bytes[0] & 255;
        tmp = tmp << 8 | bytes[1] & 255;
        tmp = tmp << 8 | bytes[2] & 255;
        tmp = tmp << 8 | bytes[3] & 255;
        return tmp;
    }

    public static byte[] bigIntToBytes(int value) {
        byte[] tmpBytes = new byte[]{(byte) (value >> 24 & 255), (byte) (value >> 16 & 255),
                (byte) (value >> 8 & 255), (byte) (value & 255)};
        return tmpBytes;
    }

    public static byte[] littleIntToBytes(int value) {
        byte[] tmpBytes = new byte[]{(byte) (255 & value), (byte) (('\uff00' & value) >> 8),
                (byte) ((16711680 & value) >> 16), (byte) ((-16777216 & value) >> 24)};
        return tmpBytes;
    }

    public static byte[] convertListByte(List<Byte> inData) {
        int inDataLen = inData.size();
        byte[] outData = new byte[inDataLen];

        for (int i = 0; i < inDataLen; ++i) {
            outData[i] = (Byte) inData.get(i);
        }

        return outData;
    }

    public static List<Byte> convertByteArray(byte[] inData) {
        int inDataLen = inData.length;
        List<Byte> outData = new ArrayList(inDataLen);

        for (int i = 0; i < inDataLen; ++i) {
            outData.add(inData[i]);
        }

        return outData;
    }

    public static short convertLen(byte lowByte, byte highByte) {
        return (short) (lowByte & 255 | highByte << 8 & '\uff00');
    }

    public static int getUnsignedByte(byte data) {
        return data & 255;
    }

    public static int getUnsignedShort(short data) {
        return data & '\uffff';
    }

    public static long getUnsignedInt(int data) {
        return (long) data & 4294967295L;
    }
}