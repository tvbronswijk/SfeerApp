package com.hueandme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.hueandme.sfeer.SfeerConfigurationController;
import com.hueandme.sfeer.sfeerconfig.SfeerConfiguration;

public class TimeSettingActivity extends AppCompatActivity {

    SfeerConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);
        config = new SfeerConfigurationController(this).get(getIntent().getStringExtra("name"));

        ((SeekBar)findViewById(R.id.seekBar11)).setProgress(config.getTime().getMorning()[0] + 50);
        ((SeekBar)findViewById(R.id.seekBar12)).setProgress(config.getTime().getMorning()[1] + 50);
        ((SeekBar)findViewById(R.id.seekBar13)).setProgress(config.getTime().getMorning()[2] + 50);

        ((SeekBar)findViewById(R.id.seekBar21)).setProgress(config.getTime().getAfternoon()[0] + 50);
        ((SeekBar)findViewById(R.id.seekBar22)).setProgress(config.getTime().getAfternoon()[1] + 50);
        ((SeekBar)findViewById(R.id.seekBar23)).setProgress(config.getTime().getAfternoon()[2] + 50);

        ((SeekBar)findViewById(R.id.seekBar31)).setProgress(config.getTime().getEvening()[0] + 50);
        ((SeekBar)findViewById(R.id.seekBar32)).setProgress(config.getTime().getEvening()[1] + 50);
        ((SeekBar)findViewById(R.id.seekBar33)).setProgress(config.getTime().getEvening()[2] + 50);

        ((SeekBar)findViewById(R.id.seekBar41)).setProgress(config.getTime().getNight()[0] + 50);
        ((SeekBar)findViewById(R.id.seekBar42)).setProgress(config.getTime().getNight()[1] + 50);
        ((SeekBar)findViewById(R.id.seekBar43)).setProgress(config.getTime().getNight()[2] + 50);
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveColors();
    }

    private void saveColors(){
        config.getTime().setMorning(
                ((SeekBar)findViewById(R.id.seekBar11)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar12)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar13)).getProgress() - 50);
        config.getTime().setAfternoon(
                ((SeekBar)findViewById(R.id.seekBar21)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar22)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar23)).getProgress() - 50);
        config.getTime().setEvening(
                ((SeekBar)findViewById(R.id.seekBar31)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar32)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar33)).getProgress() - 50);
        config.getTime().setNight(
                ((SeekBar)findViewById(R.id.seekBar41)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar42)).getProgress() - 50,
                ((SeekBar)findViewById(R.id.seekBar43)).getProgress() - 50);
        new SfeerConfigurationController(this).save(config);
    }
}
