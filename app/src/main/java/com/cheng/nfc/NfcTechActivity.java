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
import com.cheng.nfc.utils.LogUtil;
import com.cheng.nfc.utils.NfcUtils;

import java.io.IOException;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class NfcTechActivity extends AppCompatActivity {
    private static boolean READ_LOCK = false;

    private static final String TAG = "NfcTechActivity";

    IsoDep nfcA;

    NfcUtils nfcUtils;

    TextView tv_cardId;

    TextView tv_type;


    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.test);
        this.nfcUtils = new NfcUtils(this);

        this.tv_type = (TextView) findViewById(R.id.tv_type);
        this.tv_cardId = (TextView) findViewById(R.id.tv_cardId);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View param1View) {
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
            try {

                IsoDep isoDep = IsoDep.get(tag);
                this.nfcA = isoDep;
                isoDep.connect();
                transceive(new String[]{"80F6000304", "00B081001E", "00B0820002", "00A4000002DF01", "00B0810027", "00B0820065"});
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e(TAG, "读卡失败: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "读卡失败: " + e.getMessage());
            } finally {
            }
        }
        READ_LOCK = false;
    }

    public void getNdefMsg(Tag tag) {
        String[] techList = tag.getTechList();
        StringBuilder sb = new StringBuilder();
        this.tv_type.setText(Arrays.toString(techList));

        String cardID = ByteHex.bytesToHex(tag.getId());
        Log.i(TAG, "cardID : " + cardID);
        this.tv_cardId.setText(cardID);
    }


    private String[] transceive(String[] cmds) throws IOException {
        String[] rspData = new String[cmds.length];
        for (int i = 0; i < cmds.length; i++) {
            String command = cmds[i];
            LogUtil.i(TAG, "指令:" + command);
            byte[] bytes = ByteHex.hexString2Bytes(command);
            String result = ByteHex.bytesToHex(this.nfcA.transceive(bytes));
            LogUtil.i(TAG, "响应:" + result);
            rspData[i] = result;
        }
        return rspData;
    }
}
