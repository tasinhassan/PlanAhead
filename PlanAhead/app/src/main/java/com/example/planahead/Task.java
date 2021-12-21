package com.example.planahead;

import java.io.Serializable;

public class Task implements Serializable
{
    private String day;
    private String name;
    private String description;
    private String time;

    public Task(String day, String name, String description, String time) {
        this.day = day;
        this.name = name;
        this.description = description;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }
}
