package com.cheng.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_ndef).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NfcNdefActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.btn_tech).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NfcTechActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        findViewById(R.id.btn_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NfcTagActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}