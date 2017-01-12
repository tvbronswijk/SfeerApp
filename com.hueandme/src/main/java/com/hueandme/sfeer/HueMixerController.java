package com.hueandme.sfeer;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.graphics.Color;

import com.google.common.primitives.Doubles;

import java.util.List;

/**
 * Created by Tobi on 22-Dec-16.
 */

public class HueMixerController {

    private WeatherController weather;
    private TimeController time;
    private EmotionController emotion;
    private Boolean weatherUsed;
    private Boolean timeUsed;
    private Boolean emotionUsed;
    private Context context;
    private Color color;
    private float RGBred = 100;
    private float RGBgreen = 100;
    private float RGBblue = 100;
    private SfeerConfiguration sfeerconfig;

    public HueMixerController(Context context, SfeerConfiguration sfeerconfig){
        this.context = context;
        this.sfeerconfig = sfeerconfig;
    }

    public Sfeer getSfeer(){
        checkSettings();

        if(weatherUsed == true && timeUsed == true && emotionUsed == true)
        {
            handleWeather();
        }

        if(timeUsed == true)
        {
            handleTime();
        }

        if(emotionUsed == true)
        {
            handleEmotion();
        }
        removeExtremeValues();
        getRGBtoXY();
        return new Sfeer();
    }

    private void checkSettings()
    {
        if(sfeerconfig.getSettings().contains(SfeerConfiguration.Setting.Weather))
        {
            weatherUsed = true;
        }

        if(sfeerconfig.getSettings().contains(SfeerConfiguration.Setting.Time))
        {
            timeUsed = true;
        }

        if(sfeerconfig.getSettings().contains(SfeerConfiguration.Setting.Emotion))
        {
            emotionUsed = true;
        }
    }

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
                RGBred += 50;
            }
            else if(weather.getTemperature(latitude, longitude) > 15)
            {
                RGBred -= 50;
            }
        }
        catch(SecurityException ex)
        {

        }
    }

    private void handleTime()
    {
        if(time.getTimeOfDay() == TimeController.TimeOfDay.Morning)
        {
            RGBred += 50;
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Afternoon)
        {
            RGBblue += 50;
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Evening)
        {
            RGBblue -= 50;
        }

        if(time.getTimeOfDay() == TimeController.TimeOfDay.Night)
        {
            RGBgreen += 50;
        }
    }

    private void handleEmotion()
    {
        if(emotion.getEmotion() == EmotionController.Emotion.Angry)
        {
            RGBgreen += 50;
            RGBblue += 50;
        }

        if(emotion.getEmotion() == EmotionController.Emotion.Comfortable)
        {
            RGBblue -= 50;
            RGBgreen -= 50;
        }

        if(emotion.getEmotion() == EmotionController.Emotion.Happy)
        {
            RGBred += 50;
        }

        if(emotion.getEmotion() == EmotionController.Emotion.Phlegmatic)
        {
            RGBgreen -= 50;
            RGBblue -= 50;
        }

        if(emotion.getEmotion() == EmotionController.Emotion.Sad)
        {
            RGBred += 50;
            RGBgreen += 50;
        }

        if(emotion.getEmotion() == EmotionController.Emotion.Tranquil)
        {
            RGBgreen += 50;
        }
    }

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

    public List<Double> getRGBtoXY() {
        // For the hue bulb the corners of the triangle are:
        // -Red: 0.675, 0.322
        // -Green: 0.4091, 0.518
        // -Blue: 0.167, 0.04
        double[] normalizedToOne = new double[3];
        float cred, cgreen, cblue;
        cred = RGBred;
        cgreen = RGBgreen;
        cblue = RGBblue;
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

        double[] xy = new double[2];
        xy[0] = x;
        xy[1] = y;
        List<Double> xyAsList = Doubles.asList(xy);
        return xyAsList;
    }
}
