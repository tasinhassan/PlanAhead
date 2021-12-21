package com.example.planahead;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.time.Month;
import java.util.ArrayList;


public class CalendarFragment extends Fragment implements ThemeManager {

    private View v;
    private TextView monthName;
    private GridView daysNames;
    private DaysAdapter daysAdapter;
    private ArrayAdapter<String> daysNamesAdapter;
    MyDate date = new MyDate();
    public static Colour colour;

    Calendar c;
    public static int indexOfDay = -1;

    ScrollView s;
    TextView dayName;

    private GridView week1;
    private GridView week2;
    private GridView week3;
    private GridView week4;
    private GridView week5;
    private GridView week6;

    private LinearLayout week1Detail;
    private LinearLayout week2Detail;
    private LinearLayout week3Detail;
    private LinearLayout week4Detail;
    private LinearLayout week5Detail;
    private LinearLayout week6Detail;

    private TextView numOfTasks1;
    private TextView numOfEvents1;
    private Button b1;

    private TextView numOfTasks2;
    private TextView numOfEvents2;
    private Button b2;

    private TextView numOfTasks3;
    private TextView numOfEvents3;
    private Button b3;

    private TextView numOfTasks4;
    private TextView numOfEvents4;
    private Button b4;

    private TextView numOfTasks5;
    private TextView numOfEvents5;
    private Button b5;

    private TextView numOfTasks6;
    private TextView numOfEvents6;
    private Button b6;

    LinearLayout calendar;

    public CalendarFragment() {
        // Required empty public constructor
    }

    int i = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        c = new MonthCalendar(date, 0);

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_calendar, container, false);


        calendar = v.findViewById(R.id.calendarId);

        week1 = v.findViewById(R.id.week1Id);
        week2 = v.findViewById(R.id.week2Id);
        week3 = v.findViewById(R.id.week3Id);
        week4 = v.findViewById(R.id.week4Id);
        week5 = v.findViewById(R.id.week5Id);
        week6 = v.findViewById(R.id.week6Id);

        week1Detail = v.findViewById(R.id.week1detailId);
        week2Detail = v.findViewById(R.id.week2detailId);
        week3Detail = v.findViewById(R.id.week3detailId);
        week4Detail = v.findViewById(R.id.week4detailId);
        week5Detail = v.findViewById(R.id.week5detailId);
        week6Detail = v.findViewById(R.id.week6detailId);

        s = v.findViewById(R.id.scrollViewID);
        monthName = v.findViewById(R.id.monthId);
        daysNames = v.findViewById(R.id.nameOfDaysId);
        dayName = v.findViewById(R.id.dayName);

        numOfTasks1 = v.findViewById(R.id.numOfTasksId);
        numOfEvents1 = v.findViewById(R.id.numOfEventsId);
        b1 = v.findViewById(R.id.b1ID);
        numOfTasks2 = v.findViewById(R.id.numOfTasksId2);
        numOfEvents2 = v.findViewById(R.id.numOfEventsId2);
        b2 = v.findViewById(R.id.b2ID);
        numOfTasks3 = v.findViewById(R.id.numOfTasksId3);
        numOfEvents3 = v.findViewById(R.id.numOfEventsId3);
        b3 = v.findViewById(R.id.b3ID);
        numOfTasks4 = v.findViewById(R.id.numOfTasksId4);
        numOfEvents4 = v.findViewById(R.id.numOfEventsId4);
        b4 = v.findViewById(R.id.b4ID);
        numOfTasks5 = v.findViewById(R.id.numOfTasksId5);
        numOfEvents5 = v.findViewById(R.id.numOfEventsId5);
        b5 = v.findViewById(R.id.b5ID);
        numOfTasks6 = v.findViewById(R.id.numOfTasksId6);
        numOfEvents6 = v.findViewById(R.id.numOfEventsId6);
        b6 = v.findViewById(R.id.b6ID);

        String monthAndYear = c.month + " " + c.year;

        monthName.setText(monthAndYear);
        if(HomeActivity.colour.type == 1) {
            daysNamesAdapter = new ArrayAdapter<>(getActivity(), R.layout.day_names_layout, R.id.dayName, MonthCalendar.dayNames);
        }
        else if (HomeActivity.colour.type == 0) {
            daysNamesAdapter = new ArrayAdapter<>(getActivity(), R.layout.day_names_layout_dark, R.id.dayName, MonthCalendar.dayNames);
        }
        else {
            daysNamesAdapter = new ArrayAdapter<>(getActivity(), R.layout.day_names_layout_nightshade, R.id.dayName, MonthCalendar.dayNames);
        }

        daysNames.setAdapter(daysNamesAdapter);

        changeCal(0);

        changeThemeColour(HomeActivity.colour);

        week1.setOnTouchListener(new OnSwipeTouchListener(getActivity())
        {
            public void onSwipeLeft() {
                changeLeft();
            }

            public void onSwipeRight() {
                changeRight();
            }
        });

        week2.setOnTouchListener(new OnSwipeTouchListener(getActivity())
        {
            public void onSwipeLeft() {
                changeLeft();
            }

            public void onSwipeRight() {
                changeRight();
            }
        });

        week3.setOnTouchListener(new OnSwipeTouchListener(getActivity())
        {
            public void onSwipeLeft() {
                changeLeft();
            }

            public void onSwipeRight() {
                changeRight();
            }
        });

        week4.setOnTouchListener(new OnSwipeTouchListener(getActivity())
        {
            public void onSwipeLeft() {
                changeLeft();
            }

            public void onSwipeRight() {
                changeRight();
            }
        });

        week5.setOnTouchListener(new OnSwipeTouchListener(getActivity())
        {
            public void onSwipeLeft() {
                changeLeft();
            }

            public void onSwipeRight() {
                changeRight();
            }
        });

        week6.setOnTouchListener(new OnSwipeTouchListener(getActivity())
        {
            public void onSwipeLeft() {
                changeLeft();
            }

            public void onSwipeRight() {
                changeRight();
            }
        });





        week1.setOnItemClickListener((parent, view, position, id) -> {

            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
            String yeep[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
            String peey[] = databaseHandler.getAllEvents("day").toArray(new String[0]);


            setAllClickedGone(week1);
            setAllClickedGone(week2);
            setAllClickedGone(week3);
            setAllClickedGone(week4);
            setAllClickedGone(week5);
            setAllClickedGone(week6);
            changeCal(1);

            week1Detail.setVisibility(View.GONE);


            if(c.numbers.get(0).days.get(position).month.equals(date.getMonth(i))) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
                week1Detail.setVisibility(View.VISIBLE);
                week2Detail.setVisibility(View.GONE);
                week3Detail.setVisibility(View.GONE);
                week4Detail.setVisibility(View.GONE);
                week5Detail.setVisibility(View.GONE);
                week6Detail.setVisibility(View.GONE);

                int countTask = 0;
                String dayString = c.numbers.get(0).days.get(position).day + " " + c.numbers.get(0).days.get(position).month + " " + c.numbers.get(0).days.get(position).year;
                for(int p = 0; p < yeep.length; p++)
                {
                    if(yeep[p].equals(dayString))
                    {
                        countTask++;
                    }
                }
                String s;
                if(countTask > 1) {
                    s = countTask + " Tasks";
                }
                else if (countTask == 0) {
                    s = "No Tasks";
                }
                else {
                    s = countTask + " Task";
                }

                numOfTasks1.setText(s);

                int countEvent = 0;
                for(int p = 0; p < peey.length; p++)
                {
                    if(peey[p].equals(dayString))
                    {
                        countEvent++;
                    }
                }

                if(countEvent > 1) {
                    s = countEvent + " Events";
                }
                else if (countEvent == 0) {
                    s = "No Events";
                }
                else {
                    s = countEvent + " Event";
                }
                numOfEvents1.setText(s);

                b1.setOnClickListener(v -> {
                    FragmentManager m = getFragmentManager();
                    m.beginTransaction().replace(R.id.container, new DayFragment(c.numbers.get(0).days.get(position), position, i, c.numbers.get(0).days)).addToBackStack(null).commit();
                    HomeActivity.i.add(1);
                });
            }
        });

        week2.setOnItemClickListener((parent, view, position, id) -> {

            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
            String yeep[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
            String peey[] = databaseHandler.getAllEvents("day").toArray(new String[0]);

            setAllClickedGone(week1);
            setAllClickedGone(week2);
            setAllClickedGone(week3);
            setAllClickedGone(week4);
            setAllClickedGone(week5);
            setAllClickedGone(week6);
            changeCal(2);

            week2Detail.setVisibility(View.GONE);

            if(c.numbers.get(1).days.get(position).month.equals(date.getMonth(i))) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
                week2Detail.setVisibility(View.VISIBLE);
                week1Detail.setVisibility(View.GONE);
                week3Detail.setVisibility(View.GONE);
                week4Detail.setVisibility(View.GONE);
                week5Detail.setVisibility(View.GONE);
                week6Detail.setVisibility(View.GONE);

                int countTask = 0;
                String dayString = c.numbers.get(1).days.get(position).day + " " + c.numbers.get(1).days.get(position).month + " " + c.numbers.get(1).days.get(position).year;
                for(int p = 0; p < yeep.length; p++)
                {
                    if(yeep[p].equals(dayString))
                    {
                        countTask++;
                    }
                }
                String s;
                if(countTask > 1) {
                    s = countTask + " Tasks";
                }
                else if (countTask == 0) {
                    s = "No Tasks";
                }
                else {
                    s = countTask + " Task";
                }
                numOfTasks2.setText(s);

                int countEvent = 0;
                for(int p = 0; p < peey.length; p++)
                {
                    if(peey[p].equals(dayString))
                    {
                        countEvent++;
                    }
                }
                if(countEvent > 1) {
                    s = countEvent + " Events";
                }
                else if (countEvent == 0) {
                    s = "No Events";
                }
                else {
                    s = countEvent + " Event";
                }
                numOfEvents2.setText(s);

                b2.setOnClickListener(v -> {
                    FragmentManager m = getFragmentManager();
                    m.beginTransaction().replace(R.id.container, new DayFragment(c.numbers.get(1).days.get(position), position, i, c.numbers.get(1).days)).addToBackStack(null).commit();
                    HomeActivity.i.add(1);
                });
            }
        });

        week3.setOnItemClickListener((parent, view, position, id) -> {

            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
            String yeep[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
            String peey[] = databaseHandler.getAllEvents("day").toArray(new String[0]);

            setAllClickedGone(week1);
            setAllClickedGone(week2);
            setAllClickedGone(week3);
            setAllClickedGone(week4);
            setAllClickedGone(week5);
            setAllClickedGone(week6);
            changeCal(3);

            week3Detail.setVisibility(View.GONE);

            if(c.numbers.get(2).days.get(position).month.equals(date.getMonth(i))) {

                view.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
                week3Detail.setVisibility(View.VISIBLE);
                week1Detail.setVisibility(View.GONE);
                week2Detail.setVisibility(View.GONE);
                week4Detail.setVisibility(View.GONE);
                week5Detail.setVisibility(View.GONE);
                week6Detail.setVisibility(View.GONE);

                int countTask = 0;
                String dayString = c.numbers.get(2).days.get(position).day + " " + c.numbers.get(2).days.get(position).month + " " + c.numbers.get(2).days.get(position).year;
                for(int p = 0; p < yeep.length; p++)
                {
                    if(yeep[p].equals(dayString))
                    {
                        countTask++;
                    }
                }
                String s;
                if(countTask > 1) {
                    s = countTask + " Tasks";
                }
                else if (countTask == 0) {
                    s = "No Tasks";
                }
                else {
                    s = countTask + " Task";
                }
                numOfTasks3.setText(s);

                int countEvent = 0;
                for(int p = 0; p < peey.length; p++)
                {
                    if(peey[p].equals(dayString))
                    {
                        countEvent++;
                    }
                }

                if(countEvent > 1) {
                    s = countEvent + " Events";
                }
                else if (countEvent == 0) {
                    s = "No Events";
                }
                else {
                    s = countEvent + " Event";
                }
                numOfEvents3.setText(s);

                b3.setOnClickListener(v -> {
                    FragmentManager m = getFragmentManager();
                    m.beginTransaction().replace(R.id.container, new DayFragment(c.numbers.get(2).days.get(position), position, i, c.numbers.get(2).days)).addToBackStack(null).commit();
                    HomeActivity.i.add(1);
                });
            }
        });

        week4.setOnItemClickListener((parent, view, position, id) -> {

            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
            String yeep[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
            String peey[] = databaseHandler.getAllEvents("day").toArray(new String[0]);

            setAllClickedGone(week1);
            setAllClickedGone(week2);
            setAllClickedGone(week3);
            setAllClickedGone(week4);
            setAllClickedGone(week5);
            setAllClickedGone(week6);
            changeCal(4);

            week4Detail.setVisibility(View.GONE);

            if(c.numbers.get(3).days.get(position).month.equals(date.getMonth(i))) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
                week4Detail.setVisibility(View.VISIBLE);
                week1Detail.setVisibility(View.GONE);
                week2Detail.setVisibility(View.GONE);
                week3Detail.setVisibility(View.GONE);
                week5Detail.setVisibility(View.GONE);
                week6Detail.setVisibility(View.GONE);

                int countTask = 0;
                String dayString = c.numbers.get(3).days.get(position).day + " " + c.numbers.get(3).days.get(position).month + " " + c.numbers.get(3).days.get(position).year;
                for(int p = 0; p < yeep.length; p++)
                {
                    if(yeep[p].equals(dayString))
                    {
                        countTask++;
                    }
                }
                String s;
                if(countTask > 1) {
                    s = countTask + " Tasks";
                }
                else if (countTask == 0) {
                    s = "No Tasks";
                }
                else {
                    s = countTask + " Task";
                }
                numOfTasks4.setText(s);

                int countEvent = 0;
                for(int p = 0; p < peey.length; p++)
                {
                    if(peey[p].equals(dayString))
                    {
                        countEvent++;
                    }
                }

                if(countEvent > 1) {
                    s = countEvent + " Events";
                }
                else if (countEvent == 0) {
                    s = "No Events";
                }
                else {
                    s = countEvent + " Event";
                }
                numOfEvents4.setText(s);

                b4.setOnClickListener(v -> {
                    FragmentManager m = getFragmentManager();
                    m.beginTransaction().replace(R.id.container, new DayFragment(c.numbers.get(3).days.get(position), position, i, c.numbers.get(3).days)).addToBackStack(null).commit();
                    HomeActivity.i.add(1);
                });
            }
        });

        week5.setOnItemClickListener((parent, view, position, id) -> {

            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
            String yeep[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
            String peey[] = databaseHandler.getAllEvents("day").toArray(new String[0]);

            setAllClickedGone(week1);
            setAllClickedGone(week2);
            setAllClickedGone(week3);
            setAllClickedGone(week4);
            setAllClickedGone(week5);
            setAllClickedGone(week6);
            changeCal(5);

            week5Detail.setVisibility(View.GONE);

            if(c.numbers.get(4).days.get(position).month.equals(date.getMonth(i))) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
                week5Detail.setVisibility(View.VISIBLE);
                week1Detail.setVisibility(View.GONE);
                week2Detail.setVisibility(View.GONE);
                week3Detail.setVisibility(View.GONE);
                week4Detail.setVisibility(View.GONE);
                week6Detail.setVisibility(View.GONE);

                int countTask = 0;
                String dayString = c.numbers.get(4).days.get(position).day + " " + c.numbers.get(4).days.get(position).month + " " + c.numbers.get(4).days.get(position).year;
                for(int p = 0; p < yeep.length; p++)
                {
                    if(yeep[p].equals(dayString))
                    {
                        countTask++;
                    }
                }
                String s;
                if(countTask > 1) {
                    s = countTask + " Tasks";
                }
                else if (countTask == 0) {
                    s = "No Tasks";
                }
                else {
                    s = countTask + " Task";
                }
                numOfTasks5.setText(s);

                int countEvent = 0;
                for(int p = 0; p < peey.length; p++)
                {
                    if(peey[p].equals(dayString))
                    {
                        countEvent++;
                    }
                }

                if(countEvent > 1) {
                    s = countEvent + " Events";
                }
                else if (countEvent == 0) {
                    s = "No Events";
                }
                else {
                    s = countEvent + " Event";
                }
                numOfEvents5.setText(s);

                b5.setOnClickListener(v -> {
                    FragmentManager m = getFragmentManager();
                    m.beginTransaction().replace(R.id.container, new DayFragment(c.numbers.get(4).days.get(position), position, i, c.numbers.get(4).days)).addToBackStack(null).commit();
                    HomeActivity.i.add(1);
                });
            }
        });

        week6.setOnItemClickListener((parent, view, position, id) -> {
            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
            String yeep[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
            String peey[] = databaseHandler.getAllEvents("day").toArray(new String[0]);

            setAllClickedGone(week1);
            setAllClickedGone(week2);
            setAllClickedGone(week3);
            setAllClickedGone(week4);
            setAllClickedGone(week5);
            setAllClickedGone(week6);
            changeCal(6);

            week6Detail.setVisibility(View.GONE);

            if(c.numbers.get(5).days.get(position).month.equals(date.getMonth(i))) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
                week6Detail.setVisibility(View.VISIBLE);
                week1Detail.setVisibility(View.GONE);
                week2Detail.setVisibility(View.GONE);
                week3Detail.setVisibility(View.GONE);
                week4Detail.setVisibility(View.GONE);
                week5Detail.setVisibility(View.GONE);

                int countTask = 0;
                String dayString = c.numbers.get(5).days.get(position).day + " " + c.numbers.get(5).days.get(position).month + " " + c.numbers.get(5).days.get(position).year;
                for(int p = 0; p < yeep.length; p++)
                {
                    if(yeep[p].equals(dayString))
                    {
                        countTask++;
                    }
                }
                String s;
                if(countTask > 1) {
                    s = countTask + " Tasks";
                }
                else if (countTask == 0) {
                    s = "No Tasks";
                }
                else {
                    s = countTask + " Task";
                }
                numOfTasks6.setText(s);

                int countEvent = 0;
                for(int p = 0; p < peey.length; p++)
                {
                    if(peey[p].equals(dayString))
                    {
                        countEvent++;
                    }
                }

                if(countEvent > 1) {
                    s = countEvent + " Events";
                }
                else if (countEvent == 0) {
                    s = "No Events";
                }
                else {
                    s = countEvent + " Event";
                }
                numOfEvents6.setText(s);

                b6.setOnClickListener(v -> {
                    FragmentManager m = getFragmentManager();
                    m.beginTransaction().replace(R.id.container, new DayFragment(c.numbers.get(5).days.get(position), position, i, c.numbers.get(5).days)).addToBackStack(null).commit();
                    HomeActivity.i.add(1);
                });

            }
        });

        return v;
    }

    private void setAllClickedGone(GridView griddy)
    {
        if(griddy.equals(week4))
        {
            for(int u = 0; u < 6; u++)
            {
                if(u != indexOfDay)
                    griddy.getChildAt(u).setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
                else {
                    if(colour.type == 1)
                    {
                        griddy.getChildAt(u).setBackground(getContext().getResources().getDrawable(R.drawable.settings_button));
                    }
                    else if (colour.type == 0)
                    {
                        griddy.getChildAt(u).setBackground(getContext().getResources().getDrawable(R.drawable.button_green));
                    }
                    else if (colour.type == 2) {
                        griddy.getChildAt(u).setBackground(getContext().getResources().getDrawable(R.drawable.button_white));
                    }

                }
            }
        }
        else
        {
            griddy.getChildAt(0).setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            griddy.getChildAt(1).setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            griddy.getChildAt(2).setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            griddy.getChildAt(3).setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            griddy.getChildAt(4).setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            griddy.getChildAt(5).setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            griddy.getChildAt(6).setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        }
    }


    private void setAllGone()
    {
        week6Detail.setVisibility(View.GONE);
        week1Detail.setVisibility(View.GONE);
        week2Detail.setVisibility(View.GONE);
        week3Detail.setVisibility(View.GONE);
        week4Detail.setVisibility(View.GONE);
        week5Detail.setVisibility(View.GONE);
    }


    private void changeCal(int except)
    {
        setAllGone();
        int a = c.generate(i+0);

        if(except == 1)
        {
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(1).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week2.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(2).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week3.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(3).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week4.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(4).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week5.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(5).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week6.setAdapter(daysAdapter);
        }
        else if (except == 2)
        {
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(0).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week1.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(2).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week3.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(3).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week4.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(4).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week5.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(5).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week6.setAdapter(daysAdapter);
        }
        else if(except == 3)
        {
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(0).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week1.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(1).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week2.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(3).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week4.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(4).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week5.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(5).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week6.setAdapter(daysAdapter);
        }
        else if(except == 4)
        {
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(0).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week1.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(1).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week2.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(2).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week3.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(4).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week5.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(5).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week6.setAdapter(daysAdapter);
        }
        else if(except == 5)
        {
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(0).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week1.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(1).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week2.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(2).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week3.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(3).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week4.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(5).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week6.setAdapter(daysAdapter);
        }
        else if(except == 6)
        {
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(0).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week1.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(1).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week2.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(2).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week3.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(3).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week4.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(4).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week5.setAdapter(daysAdapter);
        }
        else
        {
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(0).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week1.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(1).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week2.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(2).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week3.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(3).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week4.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(4).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week5.setAdapter(daysAdapter);
            daysAdapter = new DaysAdapter(getActivity(), c.numbers.get(5).days, c.currentDayInArray, c.firstDay, c.lastDay, -1, i+0);
            week6.setAdapter(daysAdapter);
        }
    }

    private void changeLeft()
    {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.righttoleft);
        calendar.startAnimation(animation);
        i++;
        c.numbers.clear();
        changeCal(0);
        String monthAndYear12 = c.month + " " + c.year;
        monthName.setText(monthAndYear12);
    }

    private void changeRight()
    {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.lefttoright);
        calendar.startAnimation(animation);
        i--;
        c.numbers.clear();
        changeCal(0);
        String monthAndYear1 = c.month + " " + c.year;
        monthName.setText(monthAndYear1);
    }

    @Override
    public void changeThemeColour(Colour colour)
    {
        if(colour.type != 0 || colour.type != 1) {
            s.setBackground(getResources().getDrawable(colour.background));
        }
        else {
            s.setBackgroundColor(getContext().getResources().getColor(colour.background));
        }
        monthName.setTextColor(getContext().getResources().getColor(colour.title));
        daysNames.setBackgroundColor(getContext().getResources().getColor(colour.calendarbar));
    }
}