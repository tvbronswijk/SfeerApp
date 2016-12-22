package com.hueandme.sfeer;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Tobi on 16-Dec-16.
 */

public class TimeController {

    public enum TimeOfDay{
        Morning,
        Afternoon,
        Evening,
        Night
    }

    private GregorianCalendar getTime(){
       return new GregorianCalendar(getTimeZone(), getLocale());
    }

    public TimeOfDay getTimeOfDay(){
        if(getTime().HOUR_OF_DAY < 6 && getTime().HOUR_OF_DAY >= 0)
        {
            return TimeOfDay.Night;
        }
        else if(getTime().HOUR_OF_DAY < 12 && getTime().HOUR_OF_DAY >= 6)
        {
            return TimeOfDay.Morning;
        }
        else if(getTime().HOUR_OF_DAY < 18 && getTime().HOUR_OF_DAY >= 12)
        {
            return TimeOfDay.Afternoon;
        }
        else
        {
            return TimeOfDay.Evening;
        }
    }

    private TimeZone getTimeZone()
    {
        return TimeZone.getDefault();
    }

    private Locale getLocale()
    {
        return Locale.getDefault();
    }
}
