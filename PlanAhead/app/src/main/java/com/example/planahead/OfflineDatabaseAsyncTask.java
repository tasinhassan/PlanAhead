package com.example.planahead;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OfflineDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

    OfflineDatabaseAsyncTask(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        pushTasks();
        removeTasks();
        pushEvents();
        removeEvents();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        DatabaseReference databaseReference1;
        DatabaseReference databaseReference2;
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        MyDate date = new MyDate();
        String day3 = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("/users/" + HomeActivity.mAuth.getUid() + "/tasks/");
        Handler handler2 = new Handler();
        handler2.postDelayed(() -> {
            databaseReference2.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        String day = (String) messageSnapshot.child("day").getValue();
                        String name = (String) messageSnapshot.child("name").getValue();
                        String desc = (String) messageSnapshot.child("desc").getValue();
                        String time = (String) messageSnapshot.child("timeAdded").getValue();
                        databaseHandler.deleteTask(name);
                        databaseHandler.insertTask(day, name, desc, time);
                        com.example.planahead.Task task = new com.example.planahead.Task(day, name, desc, time);
                        String day2 = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();
                        boolean found = false;
                        if (!HomeActivity.c.tasks.isEmpty()) {
                            for (int i = 0; i < HomeActivity.c.tasks.size(); i++) {
                                if (HomeActivity.c.tasks.get(i).getName().equals(task.getName())) {
                                    found = true;
                                }
                            }
                            if (!found && day2.equals(day)) {
                                HomeActivity.c.eORt.add(task);
                                HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                                HomeActivity.c.tasks.add(task);

                            }
                        } else if (day2.equals(day)) {
                            HomeActivity.c.eORt.add(task);
                            HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                            HomeActivity.c.tasks.add(task);
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }, 500);

        String day2[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
        String name2[] = databaseHandler.getAllTasks("name").toArray(new String[0]);
        String desc2[] = databaseHandler.getAllTasks("description").toArray(new String[0]);
        String time2[] = databaseHandler.getAllTasks("timeAdded").toArray(new String[0]);
        if(day2.length != 0) {
            for (int i = 0; i < day2.length; i++) {
                com.example.planahead.Task task = new com.example.planahead.Task(day2[i], name2[i], desc2[i], time2[i]);
                boolean found = false;
                if (!HomeActivity.c.tasks.isEmpty()) {
                    for (int j = 0; j < HomeActivity.c.tasks.size(); j++) {
                        if (HomeActivity.c.tasks.get(j).getName().equals(task.getName())) {
                            found = true;
                        }
                    }
                    if (!found && day3.equals(day2[i])) {
                        HomeActivity.c.tasks.add(task);
                        HomeActivity.c.eORt.add(task);
                        HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                    }
                } else if (day3.equals(day2[i])) {
                    HomeActivity.c.tasks.add(task);
                    HomeActivity.c.eORt.add(task);
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }
            }
        }

        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("/users/" + HomeActivity.mAuth.getUid() + "/events/");
        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            databaseReference1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        String day = (String) messageSnapshot.child("day").getValue();
                        String name = (String) messageSnapshot.child("name").getValue();
                        String desc = (String) messageSnapshot.child("desc").getValue();
                        String owner = (String) messageSnapshot.child("eventOwnerEmail").getValue();
                        String time = (String) messageSnapshot.child("time").getValue();
                        Long minTemp = (Long) messageSnapshot.child("minTemp").getValue();
                        Long maxTemp = (Long) messageSnapshot.child("maxTemp").getValue();
                        String weatherCondition = (String) messageSnapshot.child("weatherCondition").getValue();
                        ArrayList<String> usersInvited = new ArrayList<>();
                        for (DataSnapshot ds : messageSnapshot.child("invitedUsers").getChildren()) {
                            usersInvited.add(ds.getValue().toString());
                        }
                        String[] users = new String[usersInvited.size()];
                        for (int i = 0; i < usersInvited.size(); i++) {
                            users[i] = usersInvited.get(i);
                        }
                        databaseHandler.deleteEvent(name);
                        databaseHandler.insertEvent(day, name, desc, owner, users, time, minTemp, maxTemp, weatherCondition);
                        Event event = new Event(day, name, desc, owner, users, time, minTemp, maxTemp, weatherCondition);
                        String day2 = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();
                        boolean found = false;
                        if (!HomeActivity.c.events.isEmpty()) {
                            for (int i = 0; i < HomeActivity.c.events.size(); i++) {
                                if (HomeActivity.c.events.get(i).getName().equals(event.getName())) {
                                    found = true;
                                }
                            }
                            if (!found && day2.equals(day)) {
                                HomeActivity.c.events.add(event);
                                HomeActivity.c.eORt.add(event);
                                HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                            }
                        } else if (day2.equals(day)) {
                            HomeActivity.c.events.add(event);
                            HomeActivity.c.eORt.add(event);
                            HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            String day1[] = databaseHandler.getAllEvents("day").toArray(new String[0]);
            String name1[] = databaseHandler.getAllEvents("name").toArray(new String[0]);
            String desc1[] = databaseHandler.getAllEvents("description").toArray(new String[0]);
            String eventOwner[] = databaseHandler.getAllEvents("eventOwner").toArray(new String[0]);
            String invitedUsers[] = databaseHandler.getAllEvents("invitedUsers").toArray(new String[0]);
            String time[] = databaseHandler.getAllEvents("time").toArray(new String[0]);
            String minTemp[] = databaseHandler.getAllEvents("minTemp").toArray(new String[0]);
            String maxTemp[] = databaseHandler.getAllEvents("maxTemp").toArray(new String[0]);
            String weatherCondition[] = databaseHandler.getAllEvents("weatherCondition").toArray(new String[0]);

            if(day1.length != 0) {
                for (int i = 0; i < day1.length; i++) {
                    Event event = new Event(day1[i], name1[i], desc1[i], eventOwner[i], invitedUsers[i].split(","), time[i], Long.valueOf(minTemp[i]), Long.valueOf(maxTemp[i]), weatherCondition[i]);
                    boolean found = false;
                    if (!HomeActivity.c.events.isEmpty()) {
                        for (int j = 0; j < HomeActivity.c.events.size(); j++) {
                            if (HomeActivity.c.events.get(j).getName().equals(event.getName())) {
                                found = true;
                            }
                        }
                        if (!found && day3.equals(day1[i])) {
                            HomeActivity.c.events.add(event);
                            HomeActivity.c.eORt.add(event);
                            HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                        }
                    } else if (day3.equals(day1[i])) {
                        HomeActivity.c.events.add(event);
                        HomeActivity.c.eORt.add(event);
                        HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, 500);
    }

    private void pushTasks() {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        String day[] = databaseHandler.getAllTasksToBeAdded("day").toArray(new String[0]);
        String name[] = databaseHandler.getAllTasksToBeAdded("name").toArray(new String[0]);
        String desc[] = databaseHandler.getAllTasksToBeAdded("description").toArray(new String[0]);
        String time[] = databaseHandler.getAllTasksToBeAdded("timeAdded").toArray(new String[0]);
        if(day.length != 0) {
            for(int i = 0; i < day.length; i++) {
                com.example.planahead.Task task = new com.example.planahead.Task(day[i], name[i], desc[i], time[i]);
                addTask(task);
            }
            databaseHandler.deleteTasksToBeAdded();
        }
    }

    private void removeTasks() {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        String day[] = databaseHandler.getAllTasksToBeRemoved("day").toArray(new String[0]);
        String name[] = databaseHandler.getAllTasksToBeRemoved("name").toArray(new String[0]);
        String desc[] = databaseHandler.getAllTasksToBeRemoved("description").toArray(new String[0]);
        String time[] = databaseHandler.getAllTasksToBeRemoved("timeAdded").toArray(new String[0]);
        if(day.length != 0) {
            for(int i = 0; i < day.length; i++) {
                com.example.planahead.Task task = new com.example.planahead.Task(day[i], name[i], desc[i], time[i]);
                removeTask(task);
                databaseHandler.deleteTask(task.getName());
            }
            databaseHandler.deleteTasksToBeRemoved();

        }
    }

    private void pushEvents() {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        String day[] = databaseHandler.getAllEventsToBeAdded("day").toArray(new String[0]);
        String name[] = databaseHandler.getAllEventsToBeAdded("name").toArray(new String[0]);
        String desc[] = databaseHandler.getAllEventsToBeAdded("description").toArray(new String[0]);
        String eventOwner[] = databaseHandler.getAllEventsToBeAdded("eventOwner").toArray(new String[0]);
        String invitedUsers[] = databaseHandler.getAllEventsToBeAdded("invitedUsers").toArray(new String[0]);
        String time[] = databaseHandler.getAllEventsToBeAdded("time").toArray(new String[0]);
        String minTemp[] = databaseHandler.getAllEventsToBeAdded("minTemp").toArray(new String[0]);
        String maxTemp[] = databaseHandler.getAllEventsToBeAdded("maxTemp").toArray(new String[0]);
        String weatherCondition[] = databaseHandler.getAllEventsToBeAdded("weatherCondition").toArray(new String[0]);
        if(day.length != 0) {
            for(int i = 0; i < day.length; i++) {
                Event event = new Event(day[i], name[i], desc[i], eventOwner[i], invitedUsers[i].split(","), time[i], Long.valueOf(minTemp[i]), Long.valueOf(maxTemp[i]), weatherCondition[i]);
                //sendEmail(invitedUsers[i].split(","));
                addEvent(event);
            }
            databaseHandler.deleteEventsToBeAdded();
        }
    }

    private void removeEvents() {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        String day[] = databaseHandler.getAllEventsToBeRemoved("day").toArray(new String[0]);
        String name[] = databaseHandler.getAllEventsToBeRemoved("name").toArray(new String[0]);
        String desc[] = databaseHandler.getAllEventsToBeRemoved("description").toArray(new String[0]);
        String eventOwner[] = databaseHandler.getAllEventsToBeRemoved("eventOwner").toArray(new String[0]);
        String invitedUsers[] = databaseHandler.getAllEventsToBeRemoved("invitedUsers").toArray(new String[0]);
        String time[] = databaseHandler.getAllEventsToBeRemoved("time").toArray(new String[0]);
        String minTemp[] = databaseHandler.getAllEventsToBeRemoved("minTemp").toArray(new String[0]);
        String maxTemp[] = databaseHandler.getAllEventsToBeRemoved("maxTemp").toArray(new String[0]);
        String weatherCondition[] = databaseHandler.getAllEventsToBeRemoved("weatherCondition").toArray(new String[0]);
        if(day.length != 0) {
            for(int i = 0; i < day.length; i++) {
                Event event = new Event(day[i], name[i], desc[i], eventOwner[i], invitedUsers[i].split(","), time[i], Long.valueOf(minTemp[i]), Long.valueOf(maxTemp[i]), weatherCondition[i]);
                removeEvent(event);
            }
            databaseHandler.deleteEventsToBeRemoved();
        }
    }

    private void sendEmail(String[] users) {
        EmailAsyncTask task = new EmailAsyncTask(users);
        task.execute();
    }

    private com.google.android.gms.tasks.Task<Object> addEvent(Event event) {
        String id = HomeActivity.mAuth.getUid();
        String name = HomeActivity.mAuth.getCurrentUser().getDisplayName();
        String email = HomeActivity.mAuth.getCurrentUser().getEmail();
        Gson gson = new Gson();
        String json = gson.toJson(event);

        Map<String, Object> data = new HashMap<>();
        data.put("eventOwner", id);
        data.put("eventOwnerName", name);
        data.put("eventOwnerEmail", email);
        data.put("day", event.getDay());
        data.put("event", json);

        return mFunctions
                .getHttpsCallable("addEvent")
                .call(data)
                .continueWith(Task -> {
                    String result = (String) Task.getResult().getData();
                    return result;
                });
    }

    private com.google.android.gms.tasks.Task<String> removeEvent(Event event) {
        String id = HomeActivity.mAuth.getUid();
        Gson gson = new Gson();
        String json = gson.toJson(event);

        Map<String, Object> data = new HashMap<>();
        data.put("eventOwner", id);
        data.put("day", event.getDay());
        data.put("event", json);

        return mFunctions
                .getHttpsCallable("removeEvent")
                .call(data)
                .continueWith(Task -> {
                    String result = (String) Task.getResult().getData();
                    return result;
                });
    }

    private com.google.android.gms.tasks.Task<Object> addTask(com.example.planahead.Task task) {
        String id = HomeActivity.mAuth.getUid();
        Gson gson = new Gson();
        String json = gson.toJson(task);

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("day", task.getDay());
        data.put("task", json);

        return mFunctions
                .getHttpsCallable("addTask")
                .call(data)
                .continueWith(Task -> {
                    String result = (String) Task.getResult().getData();
                    return result;
                });
    }

    private com.google.android.gms.tasks.Task<String> removeTask(com.example.planahead.Task task) {
        String id = HomeActivity.mAuth.getUid();
        Gson gson = new Gson();
        String json = gson.toJson(task);

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("day", task.getDay());
        data.put("task", json);

        return mFunctions
                .getHttpsCallable("removeTask")
                .call(data)
                .continueWith(Task -> {
                    String result = (String) Task.getResult().getData();
                    return result;
                });
    }
}