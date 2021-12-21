package com.example.planahead;

import java.util.ArrayList;

public abstract class Calendar
{
    ArrayList<WeekCalendar> numbers;

    int currentDay;
    int lastDay;
    int lastDayOfPrev;
    int firstDay;
    int year;
    String month;
    MyDate date;
    int currentDayInArray = -1;


    abstract int generate(int n);

}
