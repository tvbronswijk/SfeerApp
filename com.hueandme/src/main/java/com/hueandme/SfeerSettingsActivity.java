package com.hueandme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.hueandme.service.hue.HueService;
import com.hueandme.sfeer.SfeerConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SfeerSettingsActivity extends AppCompatActivity {

    private SfeerConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfeer_settings);
        config = new SfeerConfiguration();
        Button opslaanButton = (Button) findViewById(R.id.opslaanButton);

        opslaanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            updateSettings();
            Intent intent = new Intent(SfeerSettingsActivity.this, HueService.class);
                intent.putExtra("configuration", config);
                startActivity(intent);
                    finish();
            }
        });
    }


    private void updateSettings() {
        Switch weatherSwitch = (Switch) findViewById(R.id.switch7);
        Switch timeSwitch = (Switch) findViewById(R.id.switch5);
        Switch emotionSwitch = (Switch) findViewById(R.id.switch6);


        List<SfeerConfiguration.Setting> settings = new ArrayList<>();

        if(weatherSwitch.isActivated())
        {
            settings.add(SfeerConfiguration.Setting.Weather);
        }

        if(timeSwitch.isActivated())
        {
            settings.add(SfeerConfiguration.Setting.Time);
        }

        if(emotionSwitch.isActivated())
        {
            settings.add(SfeerConfiguration.Setting.Emotion);
        }

        config.setSettings(settings);
    }


}
