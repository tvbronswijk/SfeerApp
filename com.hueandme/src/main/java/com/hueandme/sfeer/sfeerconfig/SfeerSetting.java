package com.hueandme.sfeer.sfeerconfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobi on 18-Jan-17.
 */

public class SfeerSetting implements WeatherSetting, TimeSetting, EmotionSetting {
    private Map<String, int[]> colors = new HashMap<>();

    private Type mType;

    public SfeerSetting(Type type) {
        mType = type;

        switch (type) {
            case EMOTION:
                colors.put("happy", new int[]{50, 50, 0});
                colors.put("comfort", new int[]{0, 50, 0});
                colors.put("peaceful", new int[]{0, 0, 50});
                colors.put("optimistic", new int[]{75, 25, 0});
                colors.put("inspired", new int[]{50, 0, 50});
                break;
            case TIME:
                colors.put("morning", new int[]{30, 20, 10});
                colors.put("afternoon", new int[]{30, 20, -10});
                colors.put("evening", new int[]{20, 10, 30});
                colors.put("night", new int[]{-20, -30, -10});
                break;
            case WEATHER:
                colors.put("cold", new int[]{0, 0, 20});
                colors.put("warm", new int[]{20, 10, 0});
                colors.put("dry", new int[]{20, 10, 0});
                colors.put("rainy", new int[]{0, 0, 20});
                break;
        }
    }

    @Override
    public int[] getHappy() {
        return colors.get("happy");
    }

    @Override
    public int[] getComfort() {
        return colors.get("comfort");
    }

    @Override
    public int[] getPeaceful() {
        return colors.get("peaceful");
    }

    @Override
    public int[] getOptimistic() {
        return colors.get("optimistic");
    }

    @Override
    public int[] getInspired() {
        return colors.get("inspired");
    }

    @Override
    public int[] getMorning() {
        return colors.get("morning");
    }

    @Override
    public void setMorning(int r, int g, int b) {
        colors.put("morning", new int[]{r, g, b});
    }

    @Override
    public int[] getAfternoon() {
        return colors.get("afternoon");
    }

    @Override
    public void setAfternoon(int r, int g, int b) {
        colors.put("afternoon", new int[]{r, g, b});
    }

    @Override
    public int[] getEvening() {
        return colors.get("evening");
    }

    @Override
    public void setEvening(int r, int g, int b) {
        colors.put("evening", new int[]{r, g, b});
    }

    @Override
    public int[] getNight() {
        return colors.get("night");
    }

    @Override
    public void setNight(int r, int g, int b) {
        colors.put("night", new int[]{r, g, b});
    }

    @Override
    public int[] getCold() {
        return colors.get("cold");
    }

    @Override
    public void setCold(int r, int g, int b) {
        colors.put("cold", new int[]{r, g, b});
    }

    @Override
    public int[] getWarm() {
        return colors.get("warm");
    }

    @Override
    public void setWarm(int r, int g, int b) {
        colors.put("warm", new int[]{r, g, b});
    }

    @Override
    public int[] getDry() {
        return colors.get("dry");
    }

    @Override
    public void setDry(int r, int g, int b) {
        colors.put("dry", new int[]{r, g, b});
    }

    @Override
    public int[] getRainy() {
        return colors.get("rainy");
    }

    @Override
    public void setRainy(int r, int g, int b) {
        colors.put("rainy", new int[]{r, g, b});
    }

    public Type getType() {
        return mType;
    }

    public enum Type {
        EMOTION, WEATHER, TIME
    }
}
