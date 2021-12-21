package com.example.planahead;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

class DaysAdapter extends ArrayAdapter<Day> implements Serializable
{

    private ArrayList<Day> numbers;
    private int currentDay;
    private int start;
    private int end;
    private int selectedDay;
    private int n;

    public DaysAdapter(@NonNull Context context, ArrayList<Day> number, int currDay, int starts, int ends, int selectedDay, int n)
    {
        super(context, R.layout.day_number_activity, number);
        numbers = number;
        currentDay = currDay;
        start = starts;
        end = ends;
        this.selectedDay = selectedDay;
        this.n = n;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(getContext());
        View customView = li.inflate(R.layout.day_number_activity, parent, false);

        TextView number = customView.findViewById(R.id.dayNumberId);

        number.setText(numbers.get(position).day);

        if(numbers.get(position).month.equals(new MyDate().getMonth(n)))
        {
            if(CalendarFragment.colour.type == 1)
            {
                number.setTextColor(Color.parseColor("#000000"));
            }
            else if (CalendarFragment.colour.type == 0)
            {
                number.setTextColor(Color.parseColor("#FFFFFF"));
            }
            else if (CalendarFragment.colour.type == 2) {
                number.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }

        MyDate d = new MyDate();
        if((numbers.get(position).day).equals(d.getDay() + "") && numbers.get(position).month.equals(d.getMonth(0)) && numbers.get(position).year.equals(d.getYear() + ""))
        {
            CalendarFragment.indexOfDay = position;
            if(CalendarFragment.colour.type == 0)
            {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.button_green));
                number.setTextColor(Color.parseColor("#FFFFFF"));
            }
            else if (CalendarFragment.colour.type == 1)
            {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.settings_button));
                number.setTextColor(Color.parseColor("#000000"));
            }
            else if (CalendarFragment.colour.type == 2) {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.button_white));
                number.setTextColor(Color.parseColor("#FFFFFF"));
            }

            number.setTypeface(number.getTypeface(), Typeface.BOLD);
        }

        return  customView;
    }
}