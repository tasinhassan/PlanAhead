package com.example.planahead;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDate
{

    String date;
    Calendar calendar;
    int month;
    int year;
    int day;
    int weekday;

    MyDate()
    {
        date = new SimpleDateFormat("yyyy-MM-dd EEE", Locale.getDefault()).format(new Date());
        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        weekday = calendar.get(Calendar.DAY_OF_WEEK);
    }

    int getMonth()
    {
        return month;
    }

    int getMonthByMonth(int n)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, n);
        return cal.get(Calendar.MONTH);
    }


    String getMonth(int n)
    {
        String name ="";
        //int month_ = (month + n)%12;


        int month_ = getMonthByMonth(n);

        switch(month_) {
            case 0:
                name = "January";
                break;
            case 1:
                name = "February";
                break;
            case 2:
                name = "March";
                break;
            case 3:
                name = "April";
                break;
            case 4:
                name = "May";
                break;
            case 5:
                name = "June";
                break;
            case 6:
                name = "July";
                break;
            case 7:
                name = "August";
                break;
            case 8:
                name = "September";
                break;
            case 9:
                name = "October";
                break;
            case 10:
                name = "November";
                break;
            case 11:
                name = "December";
                break;
        }
        return name;
    }
    int getYear()
    {
        return year;
    }

    int getYear(int byMonths)
    {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, byMonths);

        return cal.get(Calendar.YEAR);

    }

    int getDay()
    {
        return day;
    }
    String getDate()
    {
        return date;
    }
    int getWeekday()
    {
        return  weekday;
    }
    int getlastday()
    {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    int getlastday(int n)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, n);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return cal.get(Calendar.DAY_OF_MONTH);
    }
    int getWeekdayfront(int n)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, n);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        return cal.get(Calendar.DAY_OF_WEEK);
    }
    void movecaldays(int n)
    {
        calendar.add(Calendar.DAY_OF_MONTH, n);
    }
    void equals(MyDate d)
    {
        this.date = d.date;
        this.calendar = d.calendar;
        this.month = d.month;
        this.day = d.day;
        this.year = d.year;
        this.month = d.month;
        this.weekday = d.weekday;
    }


}