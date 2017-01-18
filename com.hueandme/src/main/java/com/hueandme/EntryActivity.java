package com.hueandme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hueandme.hue.HueSharedPreferences;

/**
 * Created by Marc on 18-1-2017.
 */

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!HueSharedPreferences.getInstance(getApplicationContext()).getLastConnectedIPAddress().equals("")) {
            startActivity(new Intent(EntryActivity.this, SearchActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_entry);

        findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntryActivity.this, SearchActivity.class));
                finish();
            }
        });
    }
}
