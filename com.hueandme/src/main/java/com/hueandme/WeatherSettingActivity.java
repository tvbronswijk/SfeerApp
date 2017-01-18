package com.hueandme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.hueandme.sfeer.sfeerconfig.SfeerConfiguration;

public class WeatherSettingActivity extends AppCompatActivity {

    private SfeerConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_setting);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveColors();
    }

    private void saveColors(){
        config.getWeather().setCold(((SeekBar)findViewById(R.id.seekBar11)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar12)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar13)).getProgress() - 50);
        config.getWeather().setWarm(((SeekBar)findViewById(R.id.seekBar21)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar22)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar23)).getProgress() - 50);
        config.getWeather().setDry(((SeekBar)findViewById(R.id.seekBar31)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar32)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar33)).getProgress() - 50);
        config.getWeather().setRainy(((SeekBar)findViewById(R.id.seekBar41)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar42)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar43)).getProgress() - 50);
    }
}
