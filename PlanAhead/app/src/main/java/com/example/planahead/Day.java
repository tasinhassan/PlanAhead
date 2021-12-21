package com.example.planahead;

import java.io.Serializable;
import java.util.ArrayList;

public class Day implements Serializable
{
    public ArrayList<Event> events = new ArrayList<>();
    public ArrayList<Task> tasks = new ArrayList<>();
    public ArrayList<Object> eORt = new ArrayList<>();
    public String day;
    public String month;
    public String year;
    public EventListAdapter eventAdapter;
    public RecyclerViewAdapter adapter;

    public Day(ArrayList<Event> events, ArrayList<Task> tasks, ArrayList<Object> eORt,String day, RecyclerViewAdapter adapter, EventListAdapter eventAdapter) {
        this.events = events;
        this.tasks = tasks;
        this.eORt = eORt;
        this.day = day;
        this.adapter = adapter;
        this.eventAdapter = eventAdapter;
    }

    public Day(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public void setAdapter(RecyclerViewAdapter adapter)
    {
        this.adapter = adapter;
    }

    public void setEventAdapter(EventListAdapter eventAdapter)
    {
        this.eventAdapter = eventAdapter;
    }

    public void addEvent(Event event)
    {
        events.add(event);
        if(eventAdapter != null)
        {
            eventAdapter.notifyDataSetChanged();
        }
    }

    public void removeEvent(int position)
    {
        events.remove(position);
        if(eventAdapter != null)
        {
            eventAdapter.notifyDataSetChanged();
        }
    }

    public void addTask(Task task)
    {
        tasks.add(task);
        if(adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    public void removeTask(int position)
    {
        tasks.remove(position);
        if(adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }
}
