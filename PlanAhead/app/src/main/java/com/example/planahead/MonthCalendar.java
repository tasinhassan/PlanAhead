package com.example.planahead;

import java.util.ArrayList;


public class MonthCalendar extends Calendar
{
    public static ArrayList<String> dayNames;

    public MonthCalendar(MyDate date, int n) {
        this.date = date;
        this.year = date.getYear(n);
        this.month = date.getMonth(n);
        this.numbers = new ArrayList<>();
        dayNames = new ArrayList<>();
    }

    public int generate(int n)
    {

        dayNames.add("SUN");
        dayNames.add("MON");
        dayNames.add("TUE");
        dayNames.add("WED");
        dayNames.add("THU");
        dayNames.add("FRI");
        dayNames.add("SAT");

        ArrayList<Day> arr = new ArrayList<>();


        currentDayInArray = -1;
        year = date.getYear(n);
        month = date.getMonth(n);

        if(n == 0)
        {
            currentDay = date.getDay();
        }
        else
            currentDay = -1;

        lastDay = date.getlastday(0+n);
        lastDayOfPrev = date.getlastday(-1 + n);
        firstDay = date.getWeekdayfront(0+n);

        int totalDaysSoFar = 1;

        // add last month's last days
        int i = lastDayOfPrev - (firstDay - 2);
        int count = 1;

        while(count < firstDay)
        {
            arr.add(new Day(i + "", date.getMonth(n - 1), date.getYear(n -1) + ""));
            i++;
            count++;
            totalDaysSoFar++;
        }

        // add this month's days
        int j = 1;
        while(j <= lastDay)
        {
            arr.add(new Day(j + "", date.getMonth(n) + "", date.getYear(n + 0) + ""));
            if(j == currentDay)
                currentDayInArray = j + (firstDay - 1);
            j++;
            totalDaysSoFar++;
        }

        // add next month's first days
        i = 1;
        while(totalDaysSoFar <= 42)
        {
            arr.add(new Day(i + "", date.getMonth(1 + n), date.getYear(n + 1) + ""));
            i++;
            totalDaysSoFar++;
        }


        ArrayList<Day> w1= new ArrayList<>();
        ArrayList<Day> w2= new ArrayList<>();
        ArrayList<Day> w3= new ArrayList<>();
        ArrayList<Day> w4= new ArrayList<>();
        ArrayList<Day> w5= new ArrayList<>();
        ArrayList<Day> w6= new ArrayList<>();

        for(int l = 0; l < 7; l++)
        {
            w1.add(arr.get(l));
            w2.add(arr.get(l+7));
            w3.add(arr.get(l+7+7));
            w4.add(arr.get(l+7+7+7));
            w5.add(arr.get(l+7+7+7+7));
            w6.add(arr.get(l+7+7+7+7+7));
        }

        numbers.add(new WeekCalendar(w1));
        numbers.add(new WeekCalendar(w2));
        numbers.add(new WeekCalendar(w3));
        numbers.add(new WeekCalendar(w4));
        numbers.add(new WeekCalendar(w5));
        numbers.add(new WeekCalendar(w6));


        return 1;
    }
}
