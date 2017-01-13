package com.hueandme.sfeer;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Tobi on 22-Dec-16.
 */

public class HueMixerController {

    private WeatherController weather;
    private TimeController time;
    private EmotionController emotion;
    private Context context;
    private float RGBred = 100;
    private float RGBgreen = 100;
    private float RGBblue = 100;
    private SfeerConfiguration sfeerconfig;

    public HueMixerController(Context context, SfeerConfiguration sfeerconfig){
        this.context = context;
        this.sfeerconfig = sfeerconfig;
        weather = new WeatherController(this.context);
        time = new TimeController();
    }

    public float[] getSfeer(){
        sfeerconfig = (SfeerConfiguration)new Gson().fromJson(context.getSharedPreferences("config", 0).getString("setting", null), SfeerConfiguration.class);

        Log.d("tag you're it", context.getSharedPreferences("config", 0).getString("setting", null));

        RGBred = 100;
        RGBgreen = 100;
        RGBblue = 100;



        if(sfeerconfig.getSettings().contains(SfeerConfiguration.Setting.Weather))
        {
            handleWeather();
        }

        if(sfeerconfig.getSettings().contains(SfeerConfiguration.Setting.Time))
        {
            handleTime();
        }

        if(sfeerconfig.getSettings().contains(SfeerConfiguration.Setting.Emotion))
        {
            handleEmotion();
        }

        removeExtremeValues();
        return getRGBtoXY();
    }

    /**
     * methode die kleur aanpast op basis van weer
     */
    private void handleWeather()
    {
        double longitude;
        double latitude;
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            if(weather.getTemperature(latitude, longitude) < 15)
            {
                RGBred += 30;
            }
            else if(weather.getTemperature(latitude, longitude) > 15)
            {
                RGBred -= 30;
            }
            if(weather.getRain(latitude, longitude) > 0)
            {
                RGBblue -= 30;
            }
            else if(weather.getRain(latitude, longitude) < 0)
            {
                RGBblue += 30;
            }
        }
        catch(SecurityException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * methode die kleur aanpast op basis van tijd
     */
    private void handleTime()
    {
        if(time.getTimeOfDay() == TimeController.TimeOfDay.Morning)
        {
            RGBred += 25;
            RGBgreen += 25;
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Afternoon)
        {
            RGBred += 50;
            RGBgreen += 50;
            RGBblue += 25;
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Evening)
        {

            RGBred -= 50;
            RGBgreen -=50;
            RGBblue -= 25;
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Night)
        {
            RGBred -= 100;
            RGBgreen -= 100;
            RGBblue -= 50;
        }
    }

    /**
     * methode die kleur aanpast op basis van emotie
     */
    private void handleEmotion()
    {
        if(emotion.getEmotion(context) == EmotionController.Emotion.Happy)
        {
            RGBgreen += 50;
            RGBred += 50;
        }

        if(emotion.getEmotion(context) == EmotionController.Emotion.Comfort)
        {
            RGBgreen += 50;
        }

        if(emotion.getEmotion(context) == EmotionController.Emotion.Inspired)
        {
            RGBred += 50;
            RGBblue += 50;
        }

        if(emotion.getEmotion(context) == EmotionController.Emotion.Optimistic)
        {
            RGBgreen += 25;
            RGBred += 75;
        }

        if(emotion.getEmotion(context) == EmotionController.Emotion.Peaceful)
        {
            RGBblue += 50;
        }
    }

    /**
     * Methode die er voor zorgt dat RGB waardes nooit groter dan 255 zijn en nooit kleiner dan 0
     */
    private void removeExtremeValues()
    {
        if(RGBred > 255)
        {
            RGBred = 255;
        }
        if(RGBred < 0)
        {
            RGBred = 0;
        }

        if(RGBgreen > 255)
        {
            RGBgreen = 255;
        }
        if(RGBgreen < 0)
        {
            RGBgreen = 0;
        }

        if(RGBblue > 255)
        {
            RGBblue = 255;
        }
        if(RGBblue < 0)
        {
            RGBblue = 0;
        }
    }

    /**
     * methode om een rgb waarde om te zetten naar x en y waardes
     * met x en y waardes kan de Philihs hue worden aangestuurd
     * @return list with floats x and y
     */
    public float[] getRGBtoXY() {

        double[] normalizedToOne = new double[3];
        float cred, cgreen, cblue;
        cred = RGBred;
        cgreen = RGBgreen;
        cblue = RGBblue;

        //zorg ervoor dat de kleuren tussen de 0 en 1 in plaats van tussen 0 en 255
        normalizedToOne[0] = (cred / 255);
        normalizedToOne[1] = (cgreen / 255);
        normalizedToOne[2] = (cblue / 255);
        float red, green, blue;

        // Make red more vivid
        if (normalizedToOne[0] > 0.04045) {
            red = (float) Math.pow(
                    (normalizedToOne[0] + 0.055) / (1.0 + 0.055), 2.4);
        } else {
            red = (float) (normalizedToOne[0] / 12.92);
        }

        // Make green more vivid
        if (normalizedToOne[1] > 0.04045) {
            green = (float) Math.pow((normalizedToOne[1] + 0.055)
                    / (1.0 + 0.055), 2.4);
        } else {
            green = (float) (normalizedToOne[1] / 12.92);
        }

        // Make blue more vivid
        if (normalizedToOne[2] > 0.04045) {
            blue = (float) Math.pow((normalizedToOne[2] + 0.055)
                    / (1.0 + 0.055), 2.4);
        } else {
            blue = (float) (normalizedToOne[2] / 12.92);
        }

        float X = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
        float Y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
        float Z = (float) (red * 0.0000000 + green * 0.053077 + blue * 1.035763);

        float x = X / (X + Y + Z);
        float y = Y / (X + Y + Z);

        float[] xy = new float[2];
        xy[0] = x;
        xy[1] = y;
        return xy;
    }

}
