package com.cheng.nfc.utils;

import android.annotation.SuppressLint;

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
 * CPU卡指令
 * 
 * @author ta0217 yy
 */
public class CpuCardCommand {
    
    /**
     * 命令类别(1字节)
     */
    private String cla;
    
    /**
     * 指令代码(1字节)
     */
    private String ins;
    
    /**
     * 命令参数1(1字节)
     */
    private String p1;
    
    /**
     * 命令参数2(1字节)
     */
    private String p2;
    
    /**
     * 命令数据域中存在的字节数(0或1字节)
     */
    private String lc;
    
    /**
     * 命令发送的数据位串(长度为Lc的数值)
     */
    private String data;
    
    /**
     * MAC(X字节)
     */
    private String mac;
    
    /**
     * 响应数据域中期望的最大数据字节数(1字节)
     */
    private String le;
    
    /**
     * 命令字符串
     */
    private String commandString;
    
    public byte[] getCommandBytes() {
        return ByteHex.hexStringToBytes(getCommandString());
    }
    
    /**
     * 获取 命令类别(1字节)
     */
    public String getCla() {
        return cla;
    }
    
    /**
     * 设置 命令类别(1字节)
     */
    public void setCla(String cla) {
        this.cla = cla;
    }
    
    /**
     * 获取 指令代码(1字节)
     */
    public String getIns() {
        return ins;
    }
    
    /**
     * 设置 指令代码(1字节)
     */
    public void setIns(String ins) {
        this.ins = ins;
    }
    
    /**
     * 获取 命令参数1(1字节)
     */
    public String getP1() {
        return p1;
    }
    
    /**
     * 设置 命令参数1(1字节)
     */
    public void setP1(String p1) {
        this.p1 = p1;
    }
    
    /**
     * 获取 命令参数2(1字节)
     */
    public String getP2() {
        return p2;
    }
    
    /**
     * 设置 命令参数2(1字节)
     */
    public void setP2(String p2) {
        this.p2 = p2;
    }
    
    /**
     * 获取 命令数据域中存在的字节数(0或1字节)
     */
    public String getLc() {
        return lc;
    }
    
    /**
     * 设置 命令数据域中存在的字节数(0或1字节)
     */
    public void setLc(String lc) {
        this.lc = lc;
    }
    
    /**
     * 获取 命令发送的数据位串(长度为Lc的数值)
     */
    public String getData() {
        return data;
    }
    
    /**
     * 设置 命令发送的数据位串(长度为Lc的数值)
     */
    public void setData(String data) {
        this.data = data;
    }
    
    /**
     * 获取 MAC(X字节)
     */
    public String getMac() {
        return mac;
    }
    
    /**
     * 设置 MAC(X字节)
     */
    public void setMac(String mac) {
        this.mac = mac;
    }
    
    /**
     * 获取 响应数据域中期望的最大数据字节数(1字节)
     */
    public String getLe() {
        return le;
    }
    
    /**
     * 设置 响应数据域中期望的最大数据字节数(1字节)
     */
    public void setLe(String le) {
        this.le = le;
    }
    
    /**
     * 获取 命令字符串
     */
    @SuppressLint("DefaultLocale")
	public String getCommandString() {
        if (commandString == null || commandString.isEmpty()) {
            commandString = cla + ins + p1 + p2 + lc + data + le;
        }
        return commandString.toUpperCase();
    }
    
    /**
     * 设置 命令字符串
     */
    public void setCommandString(String commandString) {
        this.commandString = commandString;
    }
}
