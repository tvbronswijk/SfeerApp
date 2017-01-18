package com.hueandme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.hueandme.sfeer.SfeerConfigurationController;
import com.hueandme.sfeer.sfeerconfig.SfeerConfiguration;

public class WeatherSettingActivity extends AppCompatActivity {

    private SfeerConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_setting);
        config = new SfeerConfigurationController(this).get(getIntent().getStringExtra("name"));

        ((SeekBar)findViewById(R.id.seekBar11)).setProgress(config.getWeather().getCold()[0]);
        ((SeekBar)findViewById(R.id.seekBar12)).setProgress(config.getWeather().getCold()[1]);
        ((SeekBar)findViewById(R.id.seekBar13)).setProgress(config.getWeather().getCold()[2]);

        ((SeekBar)findViewById(R.id.seekBar21)).setProgress(config.getWeather().getWarm()[0]);
        ((SeekBar)findViewById(R.id.seekBar22)).setProgress(config.getWeather().getWarm()[1]);
        ((SeekBar)findViewById(R.id.seekBar23)).setProgress(config.getWeather().getWarm()[2]);

        ((SeekBar)findViewById(R.id.seekBar31)).setProgress(config.getWeather().getDry()[0]);
        ((SeekBar)findViewById(R.id.seekBar32)).setProgress(config.getWeather().getDry()[1]);
        ((SeekBar)findViewById(R.id.seekBar33)).setProgress(config.getWeather().getDry()[2]);

        ((SeekBar)findViewById(R.id.seekBar41)).setProgress(config.getWeather().getRainy()[0]);
        ((SeekBar)findViewById(R.id.seekBar42)).setProgress(config.getWeather().getRainy()[1]);
        ((SeekBar)findViewById(R.id.seekBar43)).setProgress(config.getWeather().getRainy()[2]);
    }

    @Override
    protected void onPause(){
        super.onPause();
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
        new SfeerConfigurationController(this).save(config);
    }
}
