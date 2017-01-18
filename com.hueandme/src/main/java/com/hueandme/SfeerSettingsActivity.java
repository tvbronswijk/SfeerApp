package com.hueandme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.google.gson.Gson;
import com.hueandme.sfeer.SfeerConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SfeerSettingsActivity extends AppCompatActivity {

    private SfeerConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfeer_settings);

        Switch weatherSwitch = (Switch) findViewById(R.id.switch7);
        Switch timeSwitch = (Switch) findViewById(R.id.switch5);
        Switch emotionSwitch = (Switch) findViewById(R.id.switch6);

        config = new Gson().fromJson(getSharedPreferences("config", 0).getString("setting", null), SfeerConfiguration.class);
        weatherSwitch.setChecked(config.getSettings().contains(SfeerConfiguration.Setting.Weather));
        timeSwitch.setChecked(config.getSettings().contains(SfeerConfiguration.Setting.Time));
        emotionSwitch.setChecked(config.getSettings().contains(SfeerConfiguration.Setting.Emotion));

        weatherSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettings();
            }
        });
        timeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettings();
            }
        });
        emotionSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettings();
            }
        });
    }


    private void updateSettings() {
        Switch weatherSwitch = (Switch) findViewById(R.id.switch5);
        Switch emotionSwitch = (Switch) findViewById(R.id.switch6);
        Switch timeSwitch = (Switch) findViewById(R.id.switch7);

        List<SfeerConfiguration.Setting> settings = new ArrayList<>();

        if(weatherSwitch.isChecked())
        {
            settings.add(SfeerConfiguration.Setting.Weather);
        }

        if(timeSwitch.isChecked())
        {
            settings.add(SfeerConfiguration.Setting.Time);
        }

        if(emotionSwitch.isChecked())
        {
            settings.add(SfeerConfiguration.Setting.Emotion);
        }

        config.setSettings(settings);
        getSharedPreferences("config", 0).edit().putString("setting", new Gson().toJson(config)).apply();
    }


}
