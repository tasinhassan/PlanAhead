package com.example.planahead;

import java.io.Serializable;

public class Event implements Serializable {

    private String day;
    private String name;
    private String description;
    private String eventOwner;
    private String[] invitedUsers;
    private String time;
    private Long minTemp;
    private Long maxTemp;
    private String weatherCondition;

    public Event(String day, String name, String description, String eventOwner, String[] invitedUsers, String time,
                 Long minTemp, Long maxTemp, String weatherCondition) {
        this.day = day;
        this.name = name;
        this.description = description;
        this.eventOwner = eventOwner;
        this.invitedUsers = invitedUsers;
        this.time = time;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.weatherCondition = weatherCondition;
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

    public String getEventOwner() {
        return eventOwner;
    }

    public String[] getInvitedUsers() {
        return invitedUsers;
    }

    public String getTime() {
        return time;
    }

    public Long getMinTemp() {
        return minTemp;
    }

    public Long getMaxTemp() {
        return maxTemp;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }
}
