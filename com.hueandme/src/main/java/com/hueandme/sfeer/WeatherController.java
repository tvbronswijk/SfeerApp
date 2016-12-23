package com.hueandme.sfeer;

import android.content.Context;

import java.util.concurrent.ExecutionException;

import com.hueandme.sfeer.api.WeatherAPI;

/**
 * Created by Tobi on 16-Dec-16.
 */

public class WeatherController {

    private WeatherAPI API;

    public WeatherController(Context context){
        API = WeatherAPI.getInstance(context);
    }


    public double getTemperature(double lat, double lon) {
        try {
            return API.get(lat, lon).getAsJsonObject("main").get("temp").getAsDouble();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public int getRain(double lat, double lon) {
        try {
            return API.get(lat, lon).getAsJsonObject("rain").get("3h").getAsInt();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
