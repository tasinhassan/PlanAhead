package com.example.planahead;


public class ForecastDay {

    Double Minimum;
    Double Maximum;
    Boolean DayPercipitation;
    Boolean NightPercipitation;
    String DayDescription;
    String NightDescription;
    String DayPhrase;
    String NightPhrase;
    boolean IsInitialized;

    ForecastDay(Double max, Double min, Boolean dayp, Boolean nightp, String daydes, String nightdes, String dayphrase, String nightphrase)
    {
        Minimum = min;
        Maximum = max;
        DayPercipitation = dayp;
        NightPercipitation = nightp;
        DayDescription = daydes;
        NightDescription = nightdes;
        DayPhrase = dayphrase;
        NightPhrase = nightphrase;
        IsInitialized = true;
    }

    ForecastDay()
    {
        Minimum = -999.99;
        Maximum = 999.99;
        DayPercipitation = false;
        NightPercipitation =false;
        DayDescription = "Error Not Initialized";
        NightDescription = "Error Not Initialized";
        DayPhrase = "Error Not Initialized";
        NightPhrase = "Error Not Initialized";
        IsInitialized = false;
    }

    void reset(Double max, Double min, Boolean dayp, Boolean nightp, String daydes, String nightdes)
    {
        Minimum = min;
        Maximum = max;
        DayPercipitation = dayp;
        NightPercipitation = nightp;
        DayDescription = daydes;
        NightDescription = nightdes;
        IsInitialized = true;
    }

    Double getMinimum()
    {
        return Minimum;
    }
    Double getMaximum()
    {
        return Maximum;
    }
    String getDayDescription()
    {
        return DayDescription;
    }
    String getNightDescription()
    {
        return NightDescription;
    }

    String getDayPhrase()
    {
        return  DayPhrase;
    }

    String getNightPhrase()
    {
        return NightPhrase;
    }
    Boolean getDayPercipitation()
    {
        return DayPercipitation;
    }
    Boolean getNightPercipitation()
    {
        return NightPercipitation;
    }

    boolean isInitialized()
    {
        return IsInitialized;
    }



}