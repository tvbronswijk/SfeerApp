package com.hueandme.sfeer.sfeerconfig;

/**
 * Created by Tobi on 18-Jan-17.
 */

public class TimeSetting extends SfeerSetting {

    public TimeSetting(){
        colors.put("morning", new int[]{ 30, 20, 10});
        colors.put("afternoon", new int[]{ 30, 20, -10});
        colors.put("evening", new int[]{ 20, 10, 30});
        colors.put("night", new int[]{ -20, -30, -10});
    }

    public int[] getMorning(){
        return colors.get("morning");
    }

    public void setMorning(int r, int g, int b){
        colors.put("morning", new int[]{r, g, b });
    }

    public int[] getAfternoon(){
        return colors.get("afternoon");
    }

    public void setAfternoon(int r, int g, int b){
        colors.put("afternoon", new int[]{r, g, b });
    }

    public int[] getEvening(){
        return colors.get("evening");
    }

    public void setEvening(int r, int g, int b){
        colors.put("evening", new int[]{r, g, b });
    }

    public int[] getNight(){
        return colors.get("night");
    }

    public void setNight(int r, int g, int b){
        colors.put("night", new int[]{r, g, b });
    }
}
