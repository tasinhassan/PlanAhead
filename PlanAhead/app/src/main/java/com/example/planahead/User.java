package com.example.planahead;

public class User {
    private String id;
    private String name;
    private String email;

    public User() {}

    public User(String i, String n, String e)
    {
        id = i;
        name = n;
        email = e;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }
}
