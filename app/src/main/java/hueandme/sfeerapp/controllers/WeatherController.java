package hueandme.sfeerapp.controllers;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import hueandme.sfeerapp.APIs.WeatherAPI;

/**
 * Created by Tobi on 09-Dec-16.
 */

public class WeatherController {

    WeatherAPI api = new WeatherAPI();

    public double getWeather(Context context){

        //test

        try {
            return  api.get(55.610826, 5.143930, context) - 273.15; //convert kelvin to celcius
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
