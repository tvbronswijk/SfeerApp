package com.hueandme.controller;

import android.content.Context;

import com.hueandme.controller.api.WeatherAPI;

/**
 * Created by Tobi on 16-Dec-16.
 */

public class WeatherController {

    private WeatherAPI API;

    public WeatherController(Context context) {
        API = WeatherAPI.getInstance(context);
    }
}
