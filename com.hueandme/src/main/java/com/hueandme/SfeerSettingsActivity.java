package com.hueandme;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.hueandme.sfeer.SfeerConfigurationController;
import com.hueandme.sfeer.sfeerconfig.SfeerConfiguration;

public class SfeerSettingsActivity extends AppCompatActivity {

    private SfeerConfiguration config;
    private SfeerConfigurationController mConfigurationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfeer_settings);

        Switch weatherSwitch = (Switch) findViewById(R.id.chk_weather);
        Switch timeSwitch = (Switch) findViewById(R.id.chk_time);
        Switch emotionSwitch = (Switch) findViewById(R.id.chk_emotion);

        mConfigurationController = new SfeerConfigurationController(this);

        if (getIntent().hasExtra("name")) {
            config = mConfigurationController.get(getIntent().getStringExtra("name"));
            weatherSwitch.setChecked(config.getWeather() != null);
            timeSwitch.setChecked(config.getTime() != null);
            emotionSwitch.setChecked(config.getEmotion() != null);
        } else {
            config = new SfeerConfiguration();
        }

        setTitle(config.getName() != null && !config.getName().isEmpty() ? config.getName() : "<no name>");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sfeer_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rename) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Name");

            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    config.setName(input.getText().toString());
                    setTitle(config.getName() != null && !config.getName().isEmpty() ? config.getName() : "<no name>");
                    updateSettings();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateSettings() {
        if (config.getName() != null && !config.getName().isEmpty()) {
            Switch weatherSwitch = (Switch) findViewById(R.id.chk_weather);
            Switch emotionSwitch = (Switch) findViewById(R.id.chk_emotion);
            Switch timeSwitch = (Switch) findViewById(R.id.chk_time);

            config.setWeatherEnabled(weatherSwitch.isChecked());
            config.setTimeEnabled(timeSwitch.isChecked());
            config.setEmotionEnabled(emotionSwitch.isChecked());

            mConfigurationController.save(config);
        } else {
            Toast.makeText(this, R.string.error_name_required, Toast.LENGTH_SHORT).show();
        }
    }


}
