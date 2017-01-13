package com.hueandme.sfeer;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tobi on 22-Dec-16.
 */
public class SfeerConfiguration implements Serializable{
    public enum Setting{
        Weather,
        Time,
        Emotion
    }

    private List<Setting> settings;


    public void setSettings(List<Setting> settings){
        this.settings = settings;
    }

    public List<Setting> getSettings(){
        return settings;
    }
}
