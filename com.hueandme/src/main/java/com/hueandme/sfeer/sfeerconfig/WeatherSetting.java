package com.hueandme.sfeer.sfeerconfig;

/**
 * Created by Tobi on 18-Jan-17.
 */

public interface WeatherSetting {

    int[] getCold();

    void setCold(int r, int g, int b);

    int[] getWarm();

    void setWarm(int r, int g, int b);

    int[] getDry();

    void setDry(int r, int g, int b);

    int[] getRainy();

    void setRainy(int r, int g, int b);
}
