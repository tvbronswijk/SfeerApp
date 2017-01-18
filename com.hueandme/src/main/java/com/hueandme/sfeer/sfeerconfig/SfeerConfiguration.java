package com.hueandme.sfeer.sfeerconfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobi on 22-Dec-16.
 */
public class SfeerConfiguration implements Serializable{

    private String name;
    private Map<String, SfeerSetting> settings = new HashMap<>();

    public SfeerConfiguration(){
        settings.put("emotion", null);
        settings.put("time", null);
        settings.put("weather", null);
    }

    public void setWeatherEnabled(boolean enabled){
        if(enabled && settings.get("weather") == null){
            settings.put("weather", new SfeerSetting(SfeerSetting.Type.WEATHER));
        }else if(!enabled){
            settings.put("weather", null);
        }
    }

    public void setTimeEnabled(boolean enabled){
        if(enabled && settings.get("time") == null){
            settings.put("time", new SfeerSetting(SfeerSetting.Type.TIME));
        }else if(!enabled){
            settings.put("time", null);
        }
    }

    public void setEmotionEnabled(boolean enabled){
        if(enabled && settings.get("emotion") == null){
            settings.put("emotion", new SfeerSetting(SfeerSetting.Type.EMOTION));
        }else if(!enabled){
            settings.put("emotion", null);
        }
    }

    public WeatherSetting getWeather(){
        return settings.get("weather");
    }

    public TimeSetting getTime(){
        return settings.get("time");
    }

    public EmotionSetting getEmotion(){
        return settings.get("emotion");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
