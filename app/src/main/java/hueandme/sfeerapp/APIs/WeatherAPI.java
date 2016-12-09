package hueandme.sfeerapp.APIs;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tobi on 09-Dec-16.
 */

public class WeatherAPI {

    public double get(double lat, double lon, Context context) throws IOException, JSONException, ExecutionException, InterruptedException {
        //https://openweathermap.org/current

        //api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}
        //by coordinate


        Future<JsonObject> json = Ion.with(context)
                .load("http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&APPID=5f953fe8437b39c6e7a40ca898f0742f")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                    }
                });

        return json.get().getAsJsonObject("main").get("temp").getAsDouble();
    }
}
