package com.hueandme;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.hueandme.sfeer.EmotionController;
import com.hueandme.sfeer.TimeController;
import com.hueandme.sfeer.WeatherController;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        TextView tvEmotie = (TextView) findViewById(R.id.tvEmotie);
        TextView tvWeer = (TextView) findViewById(R.id.tvWeer);
        final TextView tvTijd = (TextView) findViewById(R.id.tvTime);

        double longitude;
        double latitude;
        double weer = 0.0;


        final TimeController timeController = new TimeController();
        EmotionController.Emotion emotion = EmotionController.getCurrentEmotion(this);
        WeatherController weatherController = new WeatherController(this);

        try {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            weer = weatherController.getTemperature(latitude, longitude);
        } catch (SecurityException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }


        if (emotion == EmotionController.Emotion.Comfort) {
            tvEmotie.setText(getResources().getString(R.string.btn_angry));
        }
        if (emotion == EmotionController.Emotion.Inspired) {
            tvEmotie.setText(getResources().getString(R.string.btn_comfort));
        }
        if (emotion == EmotionController.Emotion.Optimistic) {
            tvEmotie.setText(getResources().getString(R.string.btn_phlegmatic));
        }
        if (emotion == EmotionController.Emotion.Peaceful) {
            tvEmotie.setText(getResources().getString(R.string.btn_sad));
        }
        if (emotion == EmotionController.Emotion.Happy) {
            tvEmotie.setText(getResources().getString(R.string.btn_happy));
        }

        updateTime(tvTijd, timeController);
        tvWeer.setText(weer + "°C");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location required")
                        .setMessage("Location required for beacons")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTime(tvTijd, timeController);
                    }
                });
            }
        }, 0, 1000);
    }

    private void updateTime(TextView tvTijd, TimeController timeController) {
        tvTijd.setText(timeController.getTime().getTime().getHours() + ":" + timeController.getTime().getTime().getMinutes());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rooms) {

        } else if (id == R.id.nav_moods) {
            startActivity(new Intent(this, SelectMoodActivity.class));
        } else if (id == R.id.nav_personal_atmosphere) {
            startActivity(new Intent(this, SfeerSettingsListActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
