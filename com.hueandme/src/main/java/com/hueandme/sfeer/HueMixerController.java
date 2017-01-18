package com.hueandme.sfeer;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.hueandme.sfeer.sfeerconfig.SfeerConfiguration;

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
    private SfeerConfigurationController mConfigurationController;

    public HueMixerController(Context context, SfeerConfiguration sfeerconfig){
        this.context = context;
        this.sfeerconfig = sfeerconfig;
        weather = new WeatherController(this.context);
        time = new TimeController();
        mConfigurationController = new SfeerConfigurationController(context);
    }

    public float[] getSfeer(){
        sfeerconfig = mConfigurationController.get("Default");

        RGBred = 75;
        RGBgreen = 75;
        RGBblue = 75;

        if (sfeerconfig != null) {

            if (sfeerconfig.getWeather() != null) {
                handleWeather();
            }

            if (sfeerconfig.getTime() != null) {
                handleTime();
            }

            if (sfeerconfig.getEmotion() != null) {
                handleEmotion();
            }
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
                changeColor(sfeerconfig.getWeather().getCold());
            }
            else if(weather.getTemperature(latitude, longitude) > 15)
            {
                changeColor(sfeerconfig.getWeather().getWarm());
            }
            if(weather.getRain(latitude, longitude) > 0)
            {
                changeColor(sfeerconfig.getWeather().getDry());
            }
            else if(weather.getRain(latitude, longitude) < 0)
            {
                changeColor(sfeerconfig.getWeather().getRainy());
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
            changeColor(sfeerconfig.getTime().getMorning());
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Afternoon)
        {
            changeColor(sfeerconfig.getTime().getAfternoon());
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Evening)
        {
            changeColor(sfeerconfig.getTime().getEvening());
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Night)
        {
            changeColor(sfeerconfig.getTime().getNight());
        }
    }

    /**
     * methode die kleur aanpast op basis van emotie
     */
    private void handleEmotion()
    {
        if(emotion.getEmotion(context) == EmotionController.Emotion.Happy)
        {
            changeColor(sfeerconfig.getEmotion().getHappy());
        }

        if(emotion.getEmotion(context) == EmotionController.Emotion.Comfort)
        {
            changeColor(sfeerconfig.getEmotion().getComfort());
        }

        if(emotion.getEmotion(context) == EmotionController.Emotion.Inspired)
        {
            changeColor(sfeerconfig.getEmotion().getInspired());
        }

        if(emotion.getEmotion(context) == EmotionController.Emotion.Optimistic)
        {
            changeColor(sfeerconfig.getEmotion().getOptimistic());
        }

        if(emotion.getEmotion(context) == EmotionController.Emotion.Peaceful)
        {
            changeColor(sfeerconfig.getEmotion().getPeaceful());
        }
    }

    private void changeColor(int[] rgb){
        RGBred += rgb[0];
        RGBgreen += rgb[1];
        RGBblue += rgb[2];
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
