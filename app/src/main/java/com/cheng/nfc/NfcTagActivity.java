package com.cheng.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cheng.nfc.utils.LogUtil;
import com.cheng.nfc.utils.NfcUtils;

import java.io.UnsupportedEncodingException;

import androidx.appcompat.app.AppCompatActivity;

public class NfcTagActivity extends AppCompatActivity {
    private final String TAG = NfcTagActivity.class.getSimpleName();

    NfcUtils nfcUtils;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.test);
        this.nfcUtils = new NfcUtils(this);
        findViewById(R.id.btn_CPU).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View param1View) {
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            LogUtil.w(TAG, NfcUtils.readNFCFromTag(intent));
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
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
}