package com.example.planahead;


import java.util.ArrayList;

public class MyWeather {

    static Double CurrentTemp;
    static String CurrentDescription;
    static String DailyBlurb;
    static ArrayList<ForecastDay> Forecast;

    String getCurrentDescription()
    {
        return CurrentDescription;
    }

    Double getCurrentTemp()
    {
        return CurrentTemp;
    }
}