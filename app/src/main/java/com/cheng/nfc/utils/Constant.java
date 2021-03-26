package com.cheng.nfc.utils;

/**
 * @author : chengyue
 * @date : 2020/6/23 09:20
 * @history : change on 2020/6/23 09:20 by chengyue
 * @since : v
 */
public class Constant {

    /**
     * NFC连接失败
     */
    public static final int DEVICE_CONNECT_FAIL = 0x1001;
    /**
     * NFC未初始化
     */
    public static final int DEVICE_NOT_INIT = 0x1002;
    /**
     * NFC未连接
     */
    public static final int DEVICE_NOT_CONNECT = 0x1003;

    /**
     * 指令执行异常
     */
    public static final int COMMAND_EXCEPTION = 0x2001;

    public final static byte[] DFN_PSE = {(byte) '1', (byte) 'P',
            (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
            (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F',
            (byte) '0', (byte) '1',};
}
