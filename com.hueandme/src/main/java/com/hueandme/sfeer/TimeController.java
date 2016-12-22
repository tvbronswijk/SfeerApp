package com.hueandme.sfeer;

import java.util.GregorianCalendar;

/**
 * Created by Tobi on 16-Dec-16.
 */

public class TimeController {

    public enum TimeOfDay{
        Morning,
        Noon,
        Afternoon,
        Evening,
        Night
    }

    public GregorianCalendar getTime(){
        throw new UnsupportedOperationException();
    }

    public TimeOfDay getTimeOfDay(){
        throw new UnsupportedOperationException();
    }
}
