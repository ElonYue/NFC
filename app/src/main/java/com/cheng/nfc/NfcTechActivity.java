package com.cheng.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cheng.nfc.utils.ByteHex;
import com.cheng.nfc.utils.Constant;
import com.cheng.nfc.utils.LogUtil;
import com.cheng.nfc.utils.NfcUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class NfcTechActivity extends AppCompatActivity {
    private static boolean READ_LOCK = false;

    private static final String TAG = "NfcTechActivity";
    IsoDep nfcA;
    NfcUtils nfcUtils;
    TextView tv_type;
    TextView tv_cardId;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.test);
        this.nfcUtils = new NfcUtils(this);

        this.tv_type = (TextView) findViewById(R.id.tv_type);
        this.tv_cardId = (TextView) findViewById(R.id.tv_cardId);
        this.tv_result = (TextView) findViewById(R.id.tv_result);
        tv_result.setMovementMethod(ScrollingMovementMethod.getInstance());
        findViewById(R.id.btn_CPU).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int readICC0015Length = 43, readICC0019Length = 43, readICC0002Length = 4;
                String[] cosCmds = new String[5];
                int cosNums = 0;
                //指令：00B0960037 读取IC Card 0016文件55个字节 3F00目录下
                cosCmds[cosNums++] = "00B0960037";
                //指令：进入卡片1001（DF01目录） 联网收费应用目录
                cosCmds[cosNums++] = "00A40000021001";
                //指令：00B095002B 读取IC Card 0015文件43个字节
                cosCmds[cosNums++] = String.format("00B09500%02X", (byte) readICC0015Length);
                //指令：00B201CC2B 读取IC Card 0019文件43个字节
                cosCmds[cosNums++] = String.format("00B201CC%02X", (byte) readICC0019Length);
                //指令，805C000204 读0002钱包文件
                cosCmds[cosNums++] = String.format("805C0002%02X", (byte) readICC0002Length);
                int result = transceive(cosCmds);
                if (result == 0) {
                    tv_result.setText(parseCpuInfo(cosCmds));
                } else {
                    LogUtil.d(String.format("操作失败，错误码=【%d】", result));
                    tv_result.setText(String.format("操作失败，错误码=【%2X】", result));
                }
            }
        });
        findViewById(R.id.btn_CPC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] cosCmds = new String[7];
                int cosNums = 0;
                // CPC SN
                cosCmds[cosNums++] = "80F6000304";
                // 读取3F00目录下EF01系统信息文件
                cosCmds[cosNums++] ="00B081001E";
                // 读取3F00目录下EF02基本信息文件
                cosCmds[cosNums++] = "00B0820002";
                // 进入cpc卡DF01目录
                cosCmds[cosNums++] = "00A4000002DF01";
                // 读取DF01目录下EF01文件 出入口信息
                cosCmds[cosNums++] = "00B0810027";
                // 读取DF01目录下EF02文件 CPC过站文件
                cosCmds[cosNums++] = "00B0820065";
                // 读取DF01目录下EF04文件 CPC计费文件
                cosCmds[cosNums++] = "00B0840080";

                int result = transceive(cosCmds);
                if (result == 0) {
                    tv_result.setText(parseCpcInfo(cosCmds));
                } else {
                    LogUtil.d(String.format("操作失败，错误码=【%d】", result));
                    tv_result.setText(String.format("操作失败，错误码=【%d】", result));
                }
            }
        });
    }

    private String parseCpuInfo(String[] cosCmds) {
        StringBuilder sb = new StringBuilder();
        String file0016 = cosCmds[0];
        String file0015 = cosCmds[2];
        String file0019 = cosCmds[3];
        String file0002 = cosCmds[4];
        // B0B2BBD534010001 17 10 2201 1030230200056249 19000101 19000101 CDEE48433035313300000000 1B 00 00
        sb.append("======卡片信息======").append("\n");
        sb.append("原始编码: ").append(file0015.substring(0, file0015.length()-4)).append("\n");
        sb.append("发行方标识: ").append(file0015.substring(0,16)).append("\n");
        sb.append("卡片类型: ").append(Integer.parseInt(file0015.substring(16,18),16)).append("\n");
        sb.append("卡版本号: ").append(file0015.substring(18,20)).append("\n");
        sb.append("网络编号: ").append(file0015.substring(20,24)).append("\n");
        sb.append("卡片号码: ").append(file0015.substring(24,40)).append("\n");
        sb.append("启用日期: ").append(file0015.substring(40,48)).append("\n");
        sb.append("截止日期: ").append(file0015.substring(48,56)).append("\n");
        byte[] vehPlate = ByteHex.hexStringToBytes(file0015.substring(56, 80));
        try {
            sb.append("车牌号码: ").append(ByteHex.byteToString(vehPlate,0,12)).append("\n");
        }catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        sb.append("用户类型: ").append(Integer.parseInt(file0015.substring(80, 82), 16)).append("\n");
        sb.append("车牌颜色: ").append(Integer.parseInt(file0015.substring(82, 84), 16)).append("\n");
        sb.append("车辆类型: ").append(Integer.parseInt(file0015.substring(84, 86), 16)).append("\n");

        sb.append("======余额信息======").append("\n");
        int balance = ByteHex.bytesToBigInt(ByteHex.hexStringToBytes(file0002.substring(0,8)));
        sb.append("卡片余额：").append(ByteHex.getUnsignedInt(balance)).append("\n");

        sb.append("======持卡人信息======").append("\n");
        sb.append("原始编码: ").append(file0016.substring(0, file0016.length() - 4)).append("\n");
        sb.append("身份标识: ").append(file0016.substring(0, 2)).append("\n");
        sb.append("职工标识: ").append(file0016.substring(2, 4)).append("\n");
        byte[] name = ByteHex.hexStringToBytes(file0016.substring(4, 44));
        sb.append("姓名: ").append(ByteHex.byteToString(name, 0, 20)).append("\n");
        byte[] ID = ByteHex.hexStringToBytes(file0016.substring(44, 108));
        sb.append("证件号码: ").append(ByteHex.byteToString(ID, 0, 32)).append("\n");
        sb.append("证件类型: ").append(Integer.parseInt(file0016.substring(108, 110), 16)).append("\n");


        sb.append("======0019消费文件======").append("\n");
        sb.append("原始编码: ").append(file0019.substring(0, file0019.length() - 4)).append("\n");
        sb.append("消费标识: ").append(file0019.substring(0, 6)).append("\n");
        sb.append("网络编号: ").append(file0019.substring(6, 10)).append("\n");
        sb.append("收费站号: ").append(Integer.parseInt(file0019.substring(10, 14), 16)).append("\n");
        sb.append("车道编号: ").append(Integer.parseInt(file0019.substring(14, 16), 16)).append("\n");

        Long time = Long.parseLong(file0019.substring(16, 24),16);
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("通行时间: ").append(format.format(date)).append("\n");

        sb.append("车辆类型: ").append(Integer.parseInt(file0019.substring(24, 26), 16)).append("\n");
        sb.append("出入状态: ").append(Integer.parseInt(file0019.substring(26, 28), 16)).append("\n");
        sb.append("门架编号: ").append(file0019.substring(28, 34)).append("\n");

        Long passGateStructureTime = Long.parseLong(file0019.substring(34, 42),16);
        sb.append("门架通行时间: ").append(format.format(passGateStructureTime)).append("\n");

        byte[] vehPlate0019 = ByteHex.hexStringToBytes(file0019.substring(42, 66));
        try {
            sb.append("车牌号码: ").append(ByteHex.byteToString(vehPlate0019,0,12)).append("\n");
        }catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        sb.append("车牌颜色: ").append(Integer.parseInt(file0019.substring(66, 68), 16)).append("\n");
        sb.append("车 轴 数: ").append(Integer.parseInt(file0019.substring(68, 70), 16)).append("\n");
        sb.append("总    重: ").append(Integer.parseInt(file0019.substring(70, 76), 16)).append("\n");
        sb.append("车辆状态标识: ").append(Integer.parseInt(file0019.substring(76, 78), 16)).append("\n");
        sb.append("累计金额: ").append(Long.parseLong(file0019.substring(78, 86), 16)).append("\n");
        return sb.toString();
    }

    private String parseCpcInfo(String[] cosCmds) {
        StringBuilder sb = new StringBuilder();
        String sn = cosCmds[0];
        String sysInfoHex = cosCmds[1].substring(0, cosCmds[1].length() - 4);
        String baseInfoHex = cosCmds[2].substring(0, cosCmds[2].length() - 4);
        String inoutHex = cosCmds[4].substring(0, cosCmds[4].length() - 4);
        String passInfoHex = cosCmds[5].substring(0, cosCmds[5].length() - 4);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sb.append("======CPC SN======").append("\n");
        sb.append("SN MAC：").append(sn.substring(0, sn.length() - 4)).append("\n");

        sb.append("======CPC系统信息======").append("\n");
        sb.append("原始编码：").append(sysInfoHex).append("\n");
        sb.append("发行方：").append(sysInfoHex.substring(0, 16)).append("\n");
        sb.append("卡片编码：").append(sysInfoHex.substring(16, 32)).append("\n");
        sb.append("卡片版本：").append(sysInfoHex.substring(32, 34)).append("\n");
        sb.append("签署日期：").append(sysInfoHex.substring(34, 42)).append("\n");
        sb.append("过期日期：").append(sysInfoHex.substring(42, 50)).append("\n");

        sb.append("======CPC基本信息======").append("\n");
        sb.append("原始编码：").append(baseInfoHex).append("\n");
        sb.append("电量信息：").append(baseInfoHex.substring(0, 2)).append("\n");
        sb.append("工作状态：").append(baseInfoHex.substring(2, 4)).append("\n");

        sb.append("======CPC出入口文件======").append("\n");
        sb.append("原始编码：").append(inoutHex).append("\n");
        sb.append("车型：").append(inoutHex.substring(0, 2)).append("\n");

        byte[] vehPlate = ByteHex.hexStringToBytes(inoutHex.substring(2, 26));
        try {
            sb.append("车牌号码: ").append(ByteHex.byteToString(vehPlate,0,12)).append("\n");
        }catch (Exception e) {
            LogUtil.e(e.getMessage());
        }

        sb.append("车牌颜色：").append(Integer.parseInt(inoutHex.substring(26, 28), 16)).append("\n");
        sb.append("网络编码：").append(inoutHex.substring(28, 32)).append("\n");
        sb.append("收费站号：").append(Integer.parseInt(inoutHex.substring(32, 36), 16)).append("\n");
        sb.append("车道号码：").append(Integer.parseInt(inoutHex.substring(36, 38), 16)).append("\n");

        Long inTime = Long.parseLong(inoutHex.substring(38, 46),16);
        sb.append("入口时间: ").append(format.format(new Date(inTime))).append("\n");

        sb.append("工作状态：").append(inoutHex.substring(46, 48)).append("\n");
        sb.append("出入口状态：").append(Integer.parseInt(inoutHex.substring(48, 50), 16)).append("\n");
        sb.append("车种类型：").append(Integer.parseInt(inoutHex.substring(50, 52), 16)).append("\n");
        sb.append("员工号：").append(Integer.parseInt(inoutHex.substring(52, 58), 16)).append("\n");
        sb.append("入口班次：").append(inoutHex.substring(58, 60)).append("\n");
        sb.append("货车轴数：").append(inoutHex.substring(60, 62)).append("\n");
        sb.append("货车总重：").append(Long.parseLong(inoutHex.substring(62, 70),16)).append("\n");
        sb.append("核定载重：").append(Long.parseLong(inoutHex.substring(70, 76),16)).append("\n");
        sb.append("特殊货车：").append(Integer.parseInt(inoutHex.substring(76, 78),16)).append("\n");


        sb.append("======CPC过站信息======").append("\n");
        sb.append("原始编码：").append(passInfoHex).append("\n");
        sb.append("通行省份：").append(Integer.parseInt(passInfoHex.substring(0, 2), 16)).append("\n");
        sb.append("本省门架数：").append(Integer.parseInt(passInfoHex.substring(2, 4), 16)).append("\n");
        sb.append("本省累计金额：").append(Long.parseLong(passInfoHex.substring(4, 10), 16)).append("\n");
        sb.append("本省计费里程：").append(Long.parseLong(passInfoHex.substring(10, 16), 16)).append("\n");
        sb.append("本省入口门架编码：").append(passInfoHex.substring(16, 22)).append("\n");
        sb.append("本省入口通行时间：").append(format.format(new Date(Long.parseLong(inoutHex.substring(22, 30),16)))).append("\n");

        sb.append("最新门架编码：").append(passInfoHex.substring(30, 36)).append("\n");
        sb.append("最新通行时间：").append(format.format(new Date(Long.parseLong(inoutHex.substring(36, 44),16)))).append("\n");
        sb.append("最新计费金额：").append(Long.parseLong(passInfoHex.substring(44, 50), 16)).append("\n");
        sb.append("最新计费里程：").append(Long.parseLong(passInfoHex.substring(50, 56), 16)).append("\n");
        sb.append("门架数量：").append(Integer.parseInt(passInfoHex.substring(56, 58), 16)).append("\n");
        sb.append("过站信息：").append(passInfoHex.substring(58, 202)).append("\n");

        sb.append("：").append(passInfoHex.substring(2, 4)).append("\n");


        return sb.toString();
    }

    @Override
    protected void onNewIntent(Intent paramIntent) {
        super.onNewIntent(paramIntent);
        resolveIntentNfcA(paramIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent,
                NfcUtils.mIntentFilter, NfcUtils.mTechList);
    }

    /**
     * 获取NFC通信对象
     * @param intent
     */
    private void resolveIntentNfcA(Intent intent) {
        if (READ_LOCK) {
            LogUtil.i(TAG, "正在读取中....");
            return;
        }
        if (this.nfcA != null) {
            LogUtil.i(TAG, "nfcA.isConnected() = " + nfcA.isConnected());
        }
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        getNdefMsg(tag);
        READ_LOCK = true;
        LogUtil.i("action = " + intent.getAction());
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            connect(tag);
        }
        READ_LOCK = false;
    }

    /**
     * 连接NFC
     * @param tag
     * @return
     */
    private int connect(Tag tag) {
        if (this.nfcA != null && this.nfcA.isConnected()) {
            return 0;
        }
        this.nfcA = IsoDep.get(tag);
        try {
            this.nfcA.connect();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e("连接失败：" + e.getMessage());
            return Constant.DEVICE_CONNECT_FAIL;
        }
        return 0;
    }

    public void getNdefMsg(Tag tag) {
        String[] techList = tag.getTechList();
        StringBuilder sb = new StringBuilder();
        this.tv_type.setText(Arrays.toString(techList));
        String cardID = ByteHex.bytesToHexString(tag.getId(),tag.getId().length);
        Log.i(TAG, "cardID : " + cardID);
        this.tv_cardId.setText(cardID);
    }


    /**
     * 指令透传操作
     * @param cosCmds
     * @return
     */
    private int transceive(String[] cosCmds) {
        if (this.nfcA == null) {
            return Constant.DEVICE_NOT_INIT;
        } else if (!this.nfcA.isConnected()) {
            return Constant.DEVICE_NOT_CONNECT;
        }
        int cosNum = cosCmds.length;
        String[] rspData = new String[cosNum];
        try {
            for (int i = 0; i < cosNum; i++) {
                String command = cosCmds[i];
                byte[] bytes = ByteHex.hexStringToBytes(command);
                byte[] rsp = nfcA.transceive(bytes);
                String result = ByteHex.bytesToHexString(rsp,rsp.length);
                rspData[i] = result.toUpperCase();
                if (!result.substring(result.length() - 4).equals("9000")) {
                    return Integer.parseInt(result, 16);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
            return Constant.COMMAND_EXCEPTION;
        }

        for (int j = 0; j < cosNum; j++) {
            String cosCmd = String.format(Locale.CHINA, "指令[%d]: %s", j, cosCmds[j]);
            LogUtil.i(TAG, cosCmd.toUpperCase());
        }

        for (int j = 0; j < cosNum; j++) {
            String rsp = String.format(Locale.CHINA, "响应[%d]: %s", j, rspData[j]);
            LogUtil.i(TAG, rsp.toUpperCase());
        }

        System.arraycopy(rspData, 0, cosCmds, 0, cosNum);
        return 0;
    }
}
