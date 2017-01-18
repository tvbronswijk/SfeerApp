package com.hueandme.sfeer.sfeerconfig;

/**
 * Created by Tobi on 18-Jan-17.
 */

public interface TimeSetting {

    int[] getMorning();

    void setMorning(int r, int g, int b);

    int[] getAfternoon();

    void setAfternoon(int r, int g, int b);

    int[] getEvening();

    void setEvening(int r, int g, int b);

    int[] getNight();

    void setNight(int r, int g, int b);
}
