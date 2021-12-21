package com.example.planahead;

public class NotificationClass {

    private String day;
    private String name;
    private String desc;
    private String eventOwnerEmail;
    private String eventOwnerName;
    private String[] invitedUsers;
    private String time;
    private Long minTemp;
    private Long maxTemp;
    private String weatherCondition;

    public NotificationClass(String day, String name, String desc, String eventOwnerEmail, String eventOwnerName, String[] invitedUsers, String time, Long minTemp, Long maxTemp, String weatherCondition) {
        this.day = day;
        this.name = name;
        this.desc = desc;
        this.eventOwnerEmail = eventOwnerEmail;
        this.eventOwnerName = eventOwnerName;
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

    public String getDesc() {
        return desc;
    }

    public String toString() {
        return this.day + " - " + this.name;
    }

    public String getEventOwner() {
        return eventOwnerEmail;
    }

    public String[] getInvitedUsers() {
        return invitedUsers;
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

    public String getTime() {
        return time;
    }

    public String getEventOwnerName() {
        return eventOwnerName;
    }
}
