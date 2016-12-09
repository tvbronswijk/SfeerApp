package hueandme.sfeerapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import hueandme.sfeerapp.R;
import hueandme.sfeerapp.controllers.WeatherController;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);


        TextView tv = (TextView)findViewById(R.id.txtTest);
        WeatherController ctrl = new WeatherController();
        tv.setText("" + ctrl.getWeather(this));
    }
}
