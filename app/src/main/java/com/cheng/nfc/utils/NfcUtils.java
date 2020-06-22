package com.cheng.nfc.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import androidx.annotation.RequiresApi;

public class NfcUtils {
    private static final String TAG = "NfcUtils";

    public static IntentFilter[] mIntentFilter = null;

    public static NfcAdapter mNfcAdapter;

    public static PendingIntent mPendingIntent = null;

    public static String[][] mTechList = (String[][]) null;

    public NfcUtils(Activity paramActivity) {
        mNfcAdapter = nfcCheck(paramActivity);
        init(paramActivity);
    }

    /**
     * 初始化nfc设置
     */
    public static void init(Activity activity) {
        mPendingIntent = PendingIntent.getActivity(activity, 0, (new Intent(activity,
                activity.getClass())).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter filter3 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException malformedMimeTypeException) {
            malformedMimeTypeException.printStackTrace();
        }
        mIntentFilter = new IntentFilter[]{filter, filter2, filter3};
        mTechList = null;
    }

    /**
     * 检查NFC是否打开
     */
    public static NfcAdapter nfcCheck(Activity activity) {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (mNfcAdapter == null) {
            return null;
        } else {
            if (!mNfcAdapter.isEnabled()) {
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                activity.startActivity(setNfc);
            }
        }
        return mNfcAdapter;
    }


    /**
     * 读取NFC的数据
     */
    public static String readNFCFromTag(Intent intent) throws UnsupportedEncodingException {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                return readResult;
            }
        }
        return "";
    }

    /**
     * 往nfc写入数据
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void writeNFCToTag(String data, Intent intent) throws IOException,
            FormatException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        NdefRecord ndefRecord = NdefRecord.createTextRecord(null, data);
        NdefRecord[] records = {ndefRecord};
        NdefMessage ndefMessage = new NdefMessage(records);
        ndef.writeNdefMessage(ndefMessage);
    }
}