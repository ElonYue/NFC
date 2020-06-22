package com.cheng.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cheng.nfc.utils.ByteHex;
import com.cheng.nfc.utils.Constant;
import com.cheng.nfc.utils.LogUtil;
import com.cheng.nfc.utils.NfcUtils;

import java.io.IOException;
import java.util.Arrays;
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
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View param1View) {
                String[] commands = new String[]{"80F6000304", "00B081001E", "00B0820002",
                        "00A4000002DF01", "00B0810027", "00B0820065"};
                int result = transceive(commands);
                if (result == 0) {
                    LogUtil.d("响应：" + Arrays.toString(commands));
                    tv_result.setText(Arrays.toString(commands));
                } else {
                    LogUtil.d(String.format("操作失败，错误码=【%d】", result));
                    tv_result.setText(String.format("操作失败，错误码=【%d】", result));
                }
            }
        });
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
        String cardID = ByteHex.bytesToHex(tag.getId());
        Log.i(TAG, "cardID : " + cardID);
        this.tv_cardId.setText(cardID);
    }


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
                byte[] bytes = ByteHex.hexString2Bytes(command);
                byte[] rsp = nfcA.transceive(bytes);
                String result = ByteHex.bytesToHex(rsp);
                rspData[i] = result.toUpperCase();
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
