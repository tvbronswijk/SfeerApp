package com.hueandme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SfeerSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfeer_settings);

        Button opslaanButton = (Button) findViewById(R.id.opslaanButton);

        opslaanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //TODO: sla de sfeer ergens op
                    finish();
            }
        });
    }


    private void updateSettings() {

    }


}
