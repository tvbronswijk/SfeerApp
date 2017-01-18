package com.hueandme.sfeer.sfeerconfig;

/**
 * Created by Tobi on 18-Jan-17.
 */

public class WeatherSetting extends SfeerSetting {

    public WeatherSetting(){
        colors.put("cold", new int[]{ 0, 0, 20});
        colors.put("warm", new int[]{ 20, 10, 0});
        colors.put("dry", new int[]{ 20, 10, 0});
        colors.put("rainy", new int[]{ 0, 0, 20});
    }

    public int[] getCold(){
        return colors.get("cold");
    }

    public void setCold(int r, int g, int b){
        colors.put("cold", new int[]{r, g, b });
    }

    public int[] getWarm(){
        return colors.get("warm");
    }

    public void setWarm(int r, int g, int b){
        colors.put("warm", new int[]{r, g, b });
    }

    public int[] getDry(){
        return colors.get("dry");
    }

    public void setDry(int r, int g, int b){
        colors.put("dry", new int[]{r, g, b });
    }

    public int[] getRainy(){
        return colors.get("rainy");
    }

    public void setRainy(int r, int g, int b){
        colors.put("rainy", new int[]{r, g, b });
    }
}
