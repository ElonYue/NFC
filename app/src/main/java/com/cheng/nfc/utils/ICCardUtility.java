package com.cheng.nfc.utils;

/*
 * #%L
 * 车道收费 设备
 * %%
 * Copyright (C) 2013 - 2016 安徽皖通科技股份有限公司
 * %%
 * All rights reserved
 * #L%
 */

/**
 * IC工具类
 * 
 * @author ta0217 yy
 */
public class ICCardUtility {
    
    public static CpuCardCommand selectByFileName(String name) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("A4");
        command.setP1("04");
        command.setP2("00");
        command.setLc(String.format("%1$02x", (name.length() / 2) & 0xFF));
        command.setData(name);
        command.setLe("00");
        return command;
    }
    
    public static CpuCardCommand selectByFileID(String id) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("A4");
        command.setP1("00");
        command.setP2("00");
        command.setLc("02");
        command.setData(id);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand getChallenge(int length) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("84");
        command.setP1("00");
        command.setP2("00");
        command.setLc("");
        command.setData("");
        command.setLe(String.format("%1$02x", length & 0xFF));
        return command;
    }
    
    public static CpuCardCommand verify(int keyID, String data) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("20");
        command.setP1("00");
        command.setP2(String.format("%1$02x", keyID));
        command.setLc(String.format("%1$02x", (data.length() / 2) & 0xFF));
        command.setData(data);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand readBinary(int id, int offset, int length) {
        id = ((id & 0x1F) | 0x80) & 0xFF;
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("B0");
        command.setP1(String.format("%1$02x", id));
        command.setP2(String.format("%1$02x", offset & 0xFF));
        command.setLc("");
        command.setData("");
        command.setLe(String.format("%1$02x", length & 0xFF));
        return command;
    }
    
    public static CpuCardCommand readBinary(int offset, int length) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("B0");
        command.setP1("");
        command.setP2(String.format("%1$04x", offset & 0xFF));
        command.setLc("");
        command.setData("");
        command.setLe(String.format("%1$02x", length & 0xFF));
        return command;
    }
    
    public static CpuCardCommand updateBinary(int id, int offset, String data) {
        id = (id & 0x1F) | 0x80;
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("D6");
        command.setP1(String.format("%1$02x", id & 0xFF));
        command.setP2(String.format("%1$02x", offset & 0xFF));
        command.setLc(String.format("%1$02x", (data.length() / 2) & 0xFF));
        command.setData(data);
        command.setLe("");
        return command;
    }

    /**
     * 04
     * D6
     * 81
     * 00
     */
    public static CpuCardCommand updateBinaryWithEmpytMac(int id, int offset, String data) {
        id = (id & 0x1F) | 0x80;
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("04");
        command.setIns("D6");
        command.setP1(String.format("%1$02x", id & 0xFF));
        command.setP2(String.format("%1$02x", offset & 0xFF));
        command.setLc(String.format("%1$02x", ((data.length() / 2) + 4) & 0xFF));
        command.setData(data);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand updateBinary(int offset, String data) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("D6");
        command.setP1("");
        command.setP2(String.format("%1$04x", offset & 0xFF));
        command.setLc(String.format("%1$02x", (data.length() / 2) & 0xFF));
        command.setData(data);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand readRecord(int recordNumber, int length) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("B2");
        command.setP1(String.format("%1$02x", (recordNumber) & 0xFF));
        command.setP2("04");
        command.setLc("");
        command.setData("");
        command.setLe(String.format("%1$02x", length & 0xFF));
        return command;
    }
    
    public static CpuCardCommand readRecord(int sfi, int recordNumber, int length) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("B2");
        command.setP1(String.format("%1$02x", (recordNumber) & 0xFF));
        command.setP2(String.format("%1$02x", (sfi << 3 | 4) & 0xFF));
        command.setLc("");
        command.setData("");
        command.setLe(String.format("%1$02x", length & 0xFF));
        return command;
    }
    
    public static CpuCardCommand updateRecord(int recordNumber, String data) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("DC");
        command.setP1(String.format("%1$02x", (recordNumber) & 0xFF));
        command.setP2("04");
        command.setLc(String.format("%1$02x", (data.length() / 2) & 0xFF));
        command.setData(data);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand updateRecord(int sfi, int recordNumber, String data) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("DC");
        command.setP1(String.format("%1$02x", (recordNumber) & 0xFF));
        command.setP2(String.format("%1$02x", (sfi << 3 | 4) & 0xFF));
        command.setLc(String.format("%1$02x", (data.length() / 2) & 0xFF));
        command.setData(data);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand getBalance() {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("5C");
        command.setP1("00");
        command.setP2("02");
        command.setLc("");
        command.setData("");
        command.setLe("04");
        return command;
    }
    
    public static CpuCardCommand getTAC(int tranNumber) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("5A");
        command.setP1("00");
        command.setP2("09");
        command.setLc("02");
        command.setData(String.format("%1$04x", (tranNumber) & 0xFFFF));
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand externalAuthentication(byte keyID, String data) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("82");
        command.setP1("00");
        command.setP2(String.format("%1$02x", (keyID & 0xFF)));
        command.setLc("08");
        command.setData(data);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand updateRecord(byte recordNumber, String data) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("00");
        command.setIns("DC");
        command.setP1(String.format("%1$02x", (recordNumber & 0xFF)));
        command.setP2("04");
        command.setLc(String.format("%1$02x", ((data.length() / 2) & 0xFF)));
        command.setData(data);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand initializeForDecrypt(byte keyType, byte keyID, String factor, byte level) {
        byte keyProperty = (byte) ((level << 5) | (keyType & 0x1F));
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("1A");
        command.setP1(String.format("%1$02x", (keyProperty & 0xFF)));
        command.setP2(String.format("%1$02x", (keyID & 0xFF)));
        command.setLc(String.format("%1$02x", ((factor.length() / 2) & 0xFF)));
        command.setData(factor);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand maccrypt(String data, String random) {
        
        String dataAfter = "8000000000000000";
        int aftLen = 8 - ((data.length() / 2) % 8);
        data += dataAfter.substring(0, aftLen * 2);
        
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("FA");
        command.setP1("05");
        command.setP2("00");
        command.setLc(String.format("%1$02x", ((data.length() / 2 + 8) & 0xFF)));
        command.setData(random + data);
        command.setLe("");
        return command;
    }
    
    /**
     * PSAM卡DES解密（data长度为8字节）
     */
    public static CpuCardCommand desDecrypt(byte[] data, String p1) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("FA");
        command.setP1(p1); // 00 - MTC写卡 80 - ETC写卡
        command.setP2("00");
        command.setLc(String.format("%1$02x", (data.length & 0xFF)));
        command.setData(ByteHex.bytesToHexString(data, 0, data.length));
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand initializeForPurchase(int keyID, int money, String terminalNumber) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("50");
        command.setP1("01");
        command.setP2("02");
        command.setLc("0B");
        command.setData(String.format("%1$02x", (keyID & 0xFF)) + String.format("%1$08x", money) + terminalNumber);
        // command.setLe("0F"); // 可能不需要
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand initializeForCAPPPurchase(int keyID, long money, String terminalNumber) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("50");
        command.setP1("03");
        command.setP2("02");
        command.setLc("0B");
        command.setData(String.format("%1$02x", (keyID & 0xFF)) + String.format("%1$08x", money) + terminalNumber);
        command.setLe(""); // 有的天线设置Le会跪...
        // command.setLe("0F");
        return command;
    }
    
//    public static CpuCardCommand initializePSAMForPurchase(String random4, String dealNumber, long money,
//            LocalDateTime dateTime, byte keyVersion, byte algorithm, String factor, String tradeType) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(random4);
//        stringBuilder.append(dealNumber);
//        stringBuilder.append(String.format("%1$08x", money));
//        stringBuilder.append(tradeType);
//        stringBuilder.append(dateTime.toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")));
//        stringBuilder.append(String.format("%1$02x", keyVersion));
//        stringBuilder.append(String.format("%1$02x", algorithm));
//        stringBuilder.append(factor);
//        CpuCardCommand command = new CpuCardCommand();
//        command.setCla("80");
//        command.setIns("70");
//        command.setP1("00");
//        command.setP2("00");
//        command.setLc(String.format("%1$02x", ((stringBuilder.toString().length() / 2) & 0xFF)));
//        command.setData(stringBuilder.toString());
//        command.setLe("");
//        return command;
//    }
    
    public static CpuCardCommand updateCAPPDataCache(String data) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("DC");
        command.setP1("AA");
        command.setP2("C8");
        command.setLc(String.format("%1$02x", ((data.length() / 2) & 0xFF)));
        command.setData(data);
        command.setLe("");
        return command;
    }
    
//    public static CpuCardCommand debitForPurchaseCashDraw(String terminalNumber, LocalDateTime dateTime, String mac1) {
//        CpuCardCommand command = new CpuCardCommand();
//        command.setCla("80");
//        command.setIns("54");
//        command.setP1("01");
//        command.setP2("00");
//        command.setLc("0F");
//        command.setData(terminalNumber + dateTime.toString(DateTimeFormat.forPattern("yyyyMMddHHmmss")) + mac1);
//        // command.setLe("08");
//        command.setLe(""); // ETC不能设置此字段，会跪。。。
//        return command;
//    }
    
    public static CpuCardCommand creditPSAMForPurchase(String mac2) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("72");
        command.setP1("00");
        command.setP2("00");
        command.setLc("04");
        command.setData(mac2);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand getTransactionProof(String netNumber) {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("5A");
        command.setP1("00");
        command.setP2("09");
        command.setLc("02");
        command.setData(netNumber);
        command.setLe("");
        return command;
    }
    
    public static CpuCardCommand closeFile() {
        CpuCardCommand command = new CpuCardCommand();
        command.setCla("80");
        command.setIns("AC");
        command.setP1("00");
        command.setP2("01");
        command.setLc("00");
        command.setData("");
        command.setLe("");
        return command;
    }
    
    public static String getSWMessage(String sw) {
        if (sw.substring(0, 2).equals("6C")) {
            return "Le 长度错误，实际长度是 " + Integer.parseInt(sw.substring(2, 4));
        }
        if (sw.substring(0, 2).equals("61")) {
            return "需发 GET RESPONSE 命令";
        }
        switch (sw) {
            case "9000":
                return "成功执行";
            case "6200":
                return "信息未提供";
            case "6281":
                return "回送数据可能出错";
            case "6282":
                return "文件长度小于 Le";
            case "6283":
                return "选中的文件无效";
            case "6284":
                return "FCI 格式与 P2 指定的不符";
            case "6300":
                return "鉴别失败";
            case "63C2":
                return "校验失败，允许重试2次";
            case "63C1":
                return "校验失败，允许重试1次";
            case "6400":
                return "状态标志位没有变";
            case "6581":
                return "内存失败";
            case "6700":
                return "长度错误";
            case "6882":
                return "不支持安全报文";
            case "6982":
                return "操作条件（AC）不满足，没有校验 PIN";
            case "6983":
                return "认证方法锁定，PIN 被锁定";
            case "6984":
                return "随机数无效，引用的数据无效";
            case "6985":
                return "使用条件不满足";
            case "6986":
                return "不满足命令执行条件（不允许的命令，INS 有错）";
            case "6987":
                return "MAC 丢失";
            case "6988":
                return "MAC 不正确";
            case "6A80":
                return "数据域参数不正确";
            case "6A81":
                return "功能不支持；创建不允许；目录无效；应用锁定";
            case "6A82":
                return "该文件未找到";
            case "6A83":
                return "该记录未找到";
            case "6A84":
                return "文件预留空间不足";
            case "6A86":
                return "P1 或 P2 不正确";
            case "6A88":
                return "引用数据未找到";
            case "6B00":
                return "参数错误";
            case "6E00":
                return "不支持的类：CLA 有错";
            case "6F00":
                return "数据无效";
            case "6D00":
                return "不支持的指令代码";
            case "9301":
                return "资金不足";
            case "9302":
                return "MAC 无效";
            case "9303":
                return "应用被永久锁定";
            case "9401":
                return "交易金额不足";
            case "9402":
                return "交易计数器达到最大值";
            case "9403":
                return "密钥索引不支持";
            case "9406":
                return "所需 MAC 不可用";
            case "6900":
                return "不能处理";
            case "6901":
                return "命令不接受（无效状态）";
            case "6600":
                return "接收通讯超时";
            case "6601":
                return "接收字符奇偶错";
            case "6602":
                return "校验和不对";
            case "6603":
                return "当前DF文件无FCI";
            case "6604":
                return "当前DF下无SF或KF";
            default:
                return "未知错误";
        }
    }
}
