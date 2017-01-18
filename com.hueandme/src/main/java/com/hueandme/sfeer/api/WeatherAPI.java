package com.hueandme.sfeer.api;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

/**
 * Created by Tobi on 16-Dec-16.
 */

public class WeatherAPI {

    private static WeatherAPI API = null;

    Context context;

    private WeatherAPI(Context context) {
        this.context = context;
    }

    public static WeatherAPI getInstance(Context context){
        if(API == null){
            API = new WeatherAPI(context);
        }
        return API;
    }

    /**
     * Retrieves the current weather from https://openweathermap.org/current.
     *
     * @param lat A double representing Latitude.
     * @param lon A double representing Longitude.
     * @return A JsonObject containing Weather.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public JsonObject get(double lat, double lon) throws ExecutionException, InterruptedException {
        //sfeer_settings.humidity = int
        //sfeer_settings.pressure = int
        //sfeer_settings.temp_min = double
        //sfeer_settings.temp_max = double
        //rain.3h = int
        //clouds.all = int
        //weather.sfeer_settings = string
        //weather.description = string

        Future<JsonObject> json = Ion.with(context)
                .load("http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&APPID=5f953fe8437b39c6e7a40ca898f0742f")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                    }
                });

        return json.get();
    }
}
