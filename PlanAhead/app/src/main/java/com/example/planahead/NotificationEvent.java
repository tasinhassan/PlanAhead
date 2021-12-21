package com.example.planahead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class NotificationEvent extends AppCompatActivity implements ThemeManager {

    FirebaseFunctions mFunctions;
    FirebaseAuth mAuth;
    Integer position;
    ConstraintLayout parentLayout;
    TextView eventName;
    TextView dayTV;
    TextView nameTV;
    TextView descTV;
    TextView invitedUsersTV;
    TextView timeTV1;
    TextView day;
    TextView name;
    TextView desc;
    TextView invitedUsers;
    TextView timeTV;
    Button acceptButton;
    Button declineButton;

    String ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_event);
        mFunctions = FirebaseFunctions.getInstance();
        mAuth = FirebaseAuth.getInstance();

        parentLayout = findViewById(R.id.parentLayout);
        eventName = findViewById(R.id.eventName);
        dayTV = findViewById(R.id.dayTV);
        nameTV = findViewById(R.id.eventNameTV);
        descTV = findViewById(R.id.descTV);
        invitedUsersTV = findViewById(R.id.invitedUsersTV);
        timeTV1 = findViewById(R.id.timeTV);
        day = findViewById(R.id.day);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        invitedUsers = findViewById(R.id.invitedUsers);
        timeTV = findViewById(R.id.time);
        acceptButton = findViewById(R.id.accept);
        declineButton = findViewById(R.id.decline);

        changeThemeColour(HomeActivity.colour);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        String d = intent.getStringExtra("day");
        day.setText(d);
        String n = intent.getStringExtra("name");
        name.setText(n);
        String des = intent.getStringExtra("desc");
        desc.setText(des);
        String owner = intent.getStringExtra("eventOwner");
        ownerName = intent.getStringExtra("eventOwnerName");
        String[] users = intent.getStringArrayExtra("invitedUsers");
        String inv = "";
        for(int i = 0; i < users.length; i++) {
            if(users[i] != null) {
                inv += users[i] + ", ";
            }
        }
        inv = inv.substring(0, inv.length()-2);
        invitedUsers.setText(inv);
        String time = intent.getStringExtra("time");
        timeTV.setText(time);
        Long minTemp = intent.getLongExtra("minTemp", 0);
        Long maxTemp = intent.getLongExtra("maxTemp", 0);
        String weatherCondition = intent.getStringExtra("weatherCondition");

        acceptButton.setOnClickListener(v -> {
            Event event = new Event(d, n, des, owner, users , time, minTemp, maxTemp, weatherCondition);
            NotificationsActivity.arrayList.remove(position);
            NotificationsActivity.arrayAdapter.remove(NotificationsActivity.arrayList.get(position));
            NotificationsActivity.arrayAdapter.notifyDataSetChanged();
            MyDate date = new MyDate();
            String today = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();
            if(event.getDay().equals(today)) {
                HomeActivity.c.events.add(event);
                HomeFragment.eventArrayAdapter.notifyDataSetChanged();
            }
            acceptEvent(event)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();

                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                                Log.w("acceptEventInvite", "acceptEventInvite:onFailure", e);
                            }
                        }
                    });
            finish();
        });

        declineButton.setOnClickListener(v -> {
            Event event = new Event(d, n, des, owner, users , time, minTemp, maxTemp, weatherCondition);
            NotificationsActivity.arrayList.remove(position);
            NotificationsActivity.arrayAdapter.remove(NotificationsActivity.arrayList.get(position));
            NotificationsActivity.arrayAdapter.notifyDataSetChanged();
            declineEvent(event)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();

                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                                Log.w("declineEventInvite", "declineEventInvite:onFailure", e);
                            }
                        }
                    });
            finish();
        });
    }

    private Task<Object> acceptEvent(Event event) {
        String id = mAuth.getUid();
        String name = mAuth.getCurrentUser().getDisplayName();
        String email = event.getEventOwner();
        Gson gson = new Gson();
        String json = gson.toJson(event);

        String url = "http://planahead-weather.herokuapp.com/api/email/" + email + "/" + name + "/" + event.getName() + "/accepted";

        EmailAsyncTask2 emailAsyncTask = new EmailAsyncTask2(email, url);
        emailAsyncTask.execute();

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("day", event.getDay());
        data.put("eventOwnerEmail", email);
        data.put("eventOwnerName", ownerName);
        data.put("event", json);
        data.put("action", "Accepted");

        return mFunctions
                .getHttpsCallable("acceptEventInvite")
                .call(data)
                .continueWith(Task -> {
                    String result = (String) Task.getResult().getData();
                    return result;
                });
    }

    private Task<Object> declineEvent(Event event) {
        String id = mAuth.getUid();
        String name = mAuth.getCurrentUser().getDisplayName();
        String email = event.getEventOwner();
        Gson gson = new Gson();
        String json = gson.toJson(event);

        String url = "http://planahead-weather.herokuapp.com/api/email/" + email + "/" + name + "/" + event.getName() + "/declined";

        EmailAsyncTask2 emailAsyncTask = new EmailAsyncTask2(email, url);
        emailAsyncTask.execute();

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("day", event.getDay());
        data.put("event", json);
        data.put("action", "Declined");

        return mFunctions
                .getHttpsCallable("declineEventInvite")
                .call(data)
                .continueWith(Task -> {
                    String result = (String) Task.getResult().getData();
                    return result;
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void changeThemeColour(Colour colour)
    {
        if(colour.type != 0 || colour.type != 1) {
            parentLayout.setBackground(getResources().getDrawable(colour.background));
        }
        else {
            parentLayout.setBackgroundColor(this.getResources().getColor(colour.background));
        }
        eventName.setTextColor(this.getResources().getColor(colour.title));
        dayTV.setTextColor(this.getResources().getColor(colour.title));
        nameTV.setTextColor(this.getResources().getColor(colour.title));
        descTV.setTextColor(this.getResources().getColor(colour.title));
        invitedUsersTV.setTextColor(this.getResources().getColor(colour.title));
        timeTV1.setTextColor(this.getResources().getColor(colour.title));
        day.setTextColor(this.getResources().getColor(colour.title));
        name.setTextColor(this.getResources().getColor(colour.title));
        desc.setTextColor(this.getResources().getColor(colour.title));
        invitedUsers.setTextColor(this.getResources().getColor(colour.title));
        timeTV.setTextColor(this.getResources().getColor(colour.title));

        acceptButton.setTextColor(this.getResources().getColor(colour.title));
        declineButton.setTextColor(this.getResources().getColor(colour.title));

        DatabaseHandler databaseHandler1 = new DatabaseHandler(this);
        String[] theme = databaseHandler1.getTheme("theme").toArray(new String[0]);
        if(theme.length != 0) {
            if (theme[0].equals("Light")) {
                acceptButton.setBackground(this.getResources().getDrawable(R.drawable.settings_button));

            } else if (theme[0].equals("Dark")){
                acceptButton.setBackground(this.getResources().getDrawable(R.drawable.button_green));
            }
            else if (theme[0].equals("Nightshade")) {
                acceptButton.setBackground(this.getResources().getDrawable(R.drawable.button_white));
            }
        }
    }
}

