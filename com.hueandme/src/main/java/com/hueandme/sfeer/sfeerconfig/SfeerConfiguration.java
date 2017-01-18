package com.hueandme.sfeer.sfeerconfig;

import com.hueandme.sfeer.WeatherController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tobi on 22-Dec-16.
 */
public class SfeerConfiguration implements Serializable{

    private int Id;
    private String name;
    private Map<String, SfeerSetting> settings = new HashMap<>();

    public SfeerConfiguration(){
        settings.put("emotion", null);
        settings.put("time", null);
        settings.put("weather", null);
    }

    public void setWeatherEnabled(boolean enabled){
        if(enabled && settings.get("weather") != null){
            settings.put("weather", new WeatherSetting());
        }else if(!enabled){
            settings.put("weather", null);
        }
    }

    public void setTimeEnabled(boolean enabled){
        if(enabled && settings.get("time") != null){
            settings.put("time", new TimeSetting());
        }else if(!enabled){
            settings.put("time", null);
        }
    }

    public void setEmotionEnabled(boolean enabled){
        if(enabled && settings.get("emotion") != null){
            settings.put("emotion", new EmotionSetting());
        }else if(!enabled){
            settings.put("emotion", null);
        }
    }

    public WeatherSetting getWeather(){
        return (WeatherSetting) settings.get("weather");
    }

    public TimeSetting getTime(){
        return (TimeSetting) settings.get("time");
    }

    public EmotionSetting getEmotion(){
        return (EmotionSetting) settings.get("emotion");
    }
}
