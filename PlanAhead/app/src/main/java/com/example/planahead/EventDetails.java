package com.example.planahead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import es.dmoral.toasty.Toasty;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class EventDetails extends AppCompatActivity implements ThemeManager {

    FirebaseFunctions mFunctions;
    FirebaseAuth mAuth;
    ConstraintLayout parentLayout;
    TextView dayTV;
    TextView descTV;
    TextView invitedUsersTV;
    TextView timeTV;
    TextView minTempTV;
    TextView maxTempTV;
    TextView weatherConditionTV;
    TextView eventDay;
    TextView eventName;
    TextView eventDesc;
    TextView invitedUsers;
    TextView time;
    TextView minTemp;
    TextView maxTemp;
    TextView condition;
    EditText updatedDayET;
    EditText updateDescET;
    EditText updatedInvitedET;
    EditText updatedTimeET;
    EditText updatedMinTempET;
    EditText updatedMaxTempET;
    Spinner updatedWeatherConditions;
    Button edit;
    Button cancel;
    Button applyChanges;
    Button remove;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        mFunctions = FirebaseFunctions.getInstance();
        mAuth = FirebaseAuth.getInstance();

        parentLayout = findViewById(R.id.parentLayout);
        dayTV = findViewById(R.id.dayTV);
        descTV = findViewById(R.id.descTV);
        invitedUsersTV = findViewById(R.id.invitedUsersTV);
        timeTV = findViewById(R.id.timeTV);
        minTempTV = findViewById(R.id.minTempTV);
        maxTempTV = findViewById(R.id.maxTempTV);
        weatherConditionTV = findViewById(R.id.weatherConditionTV);
        eventDay = findViewById(R.id.day);
        eventName = findViewById(R.id.eventName);
        eventDesc = findViewById(R.id.eventDesc);
        invitedUsers = findViewById(R.id.invitedUsers);
        time = findViewById(R.id.time);
        minTemp = findViewById(R.id.minTemp);
        maxTemp = findViewById(R.id.maxTemp);
        condition = findViewById(R.id.weatherCondition);
        edit = findViewById(R.id.editButtonID);
        cancel = findViewById(R.id.cancelEditButtonID);
        applyChanges = findViewById(R.id.applyChangesButtonID);
        remove = findViewById(R.id.removeButtonID);
        back = findViewById(R.id.backButtonID);
        updatedDayET = findViewById(R.id.updatedDayET);
        updateDescET = findViewById(R.id.updatedDescET);
        updatedInvitedET = findViewById(R.id.updatedInvitedET);
        updatedTimeET = findViewById(R.id.updatedTimeET);
        updatedMinTempET = findViewById(R.id.updatedMinTempET);
        updatedMaxTempET = findViewById(R.id.updatedMaxTempET);
        updatedWeatherConditions = findViewById(R.id.updatedWeatherConditionSpinner);

        MyDate date = new MyDate();
        String date2 = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();

        changeThemeColour(HomeActivity.colour);

        Intent intent = getIntent();
        String eventOwnerEmail = intent.getStringExtra("eventOwnerEmail");
        if (!eventOwnerEmail.equals(mAuth.getCurrentUser().getEmail())) {
            remove.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE); }
        String calledFrom = intent.getStringExtra("calledFrom");
        int pos = intent.getIntExtra("pos", 0);

        String day = intent.getStringExtra("day");
        eventDay.setText(day);

        String name = intent.getStringExtra("name");
        eventName.setText(name);

        String desc = intent.getStringExtra("description");
        if (!desc.equals("")) {
            eventDesc.setText(desc);
        }
        else { eventDesc.setText("No Description Given"); }

        String[] users = intent.getStringArrayExtra("invited");
        if(users.length != 0) {
            String inv = "";
            for(int i = 0; i < users.length; i++) {
                if(users[i] != null) {
                    inv += users[i] + ", ";
                }
            }
            inv = inv.substring(0, inv.length()-2);
            invitedUsers.setText(inv);
        }
        else {
            invitedUsers.setText("None"); }


        String timeStr = intent.getStringExtra("time");
        if(!timeStr.equals("")){
            time.setText(timeStr);
        }
        else { time.setText("Not Given"); }

        Long min = intent.getLongExtra("minTemp", -1000);
        if (min != -1000) {
            minTemp.setText(min + "°C");
            updatedMinTempET.setHint(min + "°C");
        }
        else {
            minTemp.setText("None");
            updatedMinTempET.setHint("None");
        }

        Long max = intent.getLongExtra("maxTemp", 1000);
        if (max != 1000) {
            maxTemp.setText(max + "°C");
            updatedMaxTempET.setHint(max + "°C");
        }
        else {
            maxTemp.setText("None");
            updatedMaxTempET.setHint("None");
        }

        String weatherCondition = intent.getStringExtra("weatherCondition");
        condition.setText(weatherCondition);
        if(!calledFrom.equals("HomeFragment")) {
            if (weatherCondition.equals("None")) {
                updatedWeatherConditions.setSelection(0);
            } else if (weatherCondition.equals("Sunny")) {
                updatedWeatherConditions.setSelection(1);
            } else if (weatherCondition.equals("Cloudy")) {
                updatedWeatherConditions.setSelection(2);
            } else if (weatherCondition.equals("Rainy")) {
                updatedWeatherConditions.setSelection(3);
            } else if (weatherCondition.equals("Snowy")) {
                updatedWeatherConditions.setSelection(4);
            } else {
                updatedWeatherConditions.setSelection(5);
            }
        }
        else { updatedWeatherConditions.setSelection(0); }

        LinearLayout minTempLayout = findViewById(R.id.minTempLayout);
        LinearLayout maxTempLayout = findViewById(R.id.maxTempLayout);
        LinearLayout weatherConditionLayout = findViewById(R.id.weatherConditionLayout);
        if(day.equals(date2)) {
            minTempLayout .setVisibility(View.GONE);
            maxTempLayout.setVisibility(View.GONE);
            weatherConditionLayout.setVisibility(View.GONE);
        }

        edit.setOnClickListener(v1 -> {
            updateDescET.setVisibility(View.VISIBLE);
            if(desc.equals("")) { updateDescET.setHint("Not Given"); }
            else { updateDescET.setHint(desc); }
            updatedInvitedET.setVisibility(View.VISIBLE);
            if(users.length == 0) { updatedInvitedET.setHint("None"); }
            else {
                String inv = "";
                for(int i = 0; i < users.length; i++) {
                    inv += users[i] + ", ";
                }
                inv = inv.substring(0, inv.length()-2);
                invitedUsers.setText(inv);
                updatedInvitedET.setHint(inv);
            }
            updatedTimeET.setVisibility(View.VISIBLE);
            updatedTimeET.setHint(timeStr);
            eventDesc.setVisibility(View.GONE);
            invitedUsers.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
            applyChanges.setVisibility(View.VISIBLE);
            remove.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
        });

        cancel.setOnClickListener(v2 -> {
            updateDescET.setVisibility(View.GONE);
            updatedInvitedET.setVisibility(View.GONE);
            updatedTimeET.setVisibility(View.GONE);
            updatedMinTempET.setVisibility(View.GONE);
            updatedMaxTempET.setVisibility(View.GONE);
            updatedWeatherConditions.setVisibility(View.GONE);
            eventDesc.setVisibility(View.VISIBLE);
            invitedUsers.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
            minTemp.setVisibility(View.VISIBLE);
            maxTemp.setVisibility(View.VISIBLE);
            condition.setVisibility(View.VISIBLE);
            applyChanges.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
        });

        applyChanges.setOnClickListener(v3 -> {
            Event oldEvent = new Event(day, name, desc, mAuth.getCurrentUser().getEmail(), users, timeStr, min, max, weatherCondition);
            String newDescription;
            String[] newUsers;
            String newTime;
            Long newMinTemp;
            Long newMaxTemp;
            String newWeatherCondition;
            if (!updateDescET.getText().toString().equals("")) {
                newDescription = updateDescET.getText().toString();
            } else {
                newDescription = desc;
            }

            String[] updatedNewUsers = new String[0];
            boolean foundOld = false;
            if (!updatedInvitedET.getText().toString().equals("")) {
                newUsers = updatedInvitedET.getText().toString().replace(" ", "").split(",");
                foundOld = false;
                if (users.length != 0) {
                    for (int i = 0; i < newUsers.length; i++) {
                        for (int j = 0; j < users.length; j++) {
                            if (users[j].equals(newUsers[i])) {
                                foundOld = true;
                                Toasty.warning(this, users[j] + " Has Already Been Invited" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                if (users.length != 0) {
                    updatedNewUsers = new String[users.length + newUsers.length];
                    int index = 0;
                    int z = 0;
                    for (int x = 0; x < updatedNewUsers.length; x++) {
                        if (index != users.length) {
                            updatedNewUsers[x] = users[x];
                            index++;
                        } else {
                            updatedNewUsers[x] = newUsers[z];
                            z++;
                        }
                    }
                } else {
                    updatedNewUsers = new String[newUsers.length];
                    for (int x = 0; x < newUsers.length; x++) {
                        updatedNewUsers[x] = newUsers[x];
                    }
                }
            } else {
                newUsers = users;
            }
            if (!updatedTimeET.getText().toString().equals("")) {
                newTime = updatedTimeET.getText().toString();
            } else {
                newTime = timeStr;
            }
            if (!updatedMinTempET.getText().toString().equals("")) {
                newMinTemp = Long.valueOf(updatedMinTempET.getText().toString());
            } else {
                newMinTemp = min;
            }
            if (!updatedMaxTempET.getText().toString().equals("")) {
                newMaxTemp = Long.valueOf(updatedMaxTempET.getText().toString());
            } else {
                newMaxTemp = max;
            }
            newWeatherCondition = updatedWeatherConditions.getSelectedItem().toString();
            if (updatedNewUsers.length == 0) {
                updatedNewUsers = newUsers;
            }
            Event newEvent = new Event(day, name, newDescription, mAuth.getCurrentUser().getEmail(), updatedNewUsers, newTime, newMinTemp, newMaxTemp, newWeatherCondition);

            String[] emailNewUser = new String[users.length + newUsers.length];
            if(newUsers.length != 0) {
                for (int i = 0; i < newUsers.length; i++) {
                    emailNewUser[i] = newUsers[i];
                }
            }
            if (!foundOld) {
                updateEvent(newEvent, oldEvent)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Exception e = task.getException();

                                if (e instanceof FirebaseFunctionsException) {
                                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                    FirebaseFunctionsException.Code code = ffe.getCode();
                                    Object details = ffe.getDetails();
                                    Log.w("updateEVENT", "updateEvent:onFailure", e);
                                }
                            }
                        });
                if (calledFrom.equals("HomeFragment")) {
                    HomeActivity.c.eORt.remove(pos);
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                    HomeActivity.c.eORt.add(newEvent);
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }
                else {
                    if (newEvent.getDay().equals(date2)) {
                        HomeActivity.c.eORt.remove(pos);
                        HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                        HomeActivity.c.eORt.add(newEvent);
                        HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                    }

                    DayFragment.c.eORt.remove(pos);
                    DayFragment.eventArrayAdapter.notifyDataSetChanged();
                    DayFragment.c.eORt.add(newEvent);
                    DayFragment.eventArrayAdapter.notifyDataSetChanged();
                }

                finish();
            }
        });

        remove.setOnClickListener(v4 -> {
            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            if(calledFrom.equals("HomeFragment"))
            {
                Event obj = (Event) HomeActivity.c.eORt.get(pos);
                removeEvent(HomeActivity.c.events.get(pos));
                if(!isNetworkAvailable()) {
                    databaseHandler.insertEventToBeRemoved(obj.getDay(), obj.getName(),
                            obj.getDescription(), obj.getEventOwner(), obj.getInvitedUsers(),
                            obj.getTime(), obj.getMinTemp(), obj.getMaxTemp(), obj.getWeatherCondition());
                }
                databaseHandler.deleteEvent(obj.getName());
                HomeActivity.c.eORt.remove(pos);
                HomeFragment.eventArrayAdapter.notifyDataSetChanged();
            }
            else {
                Event obj = (Event) DayFragment.c.eORt.get(pos);
                removeEvent(obj);
                if(obj.getDay().equals(date2)) {
                    HomeActivity.c.eORt.remove(pos);
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }
                if(!isNetworkAvailable()) {
                    databaseHandler.insertEventToBeRemoved(obj.getDay(), obj.getName(),
                            obj.getDescription(), obj.getEventOwner(), obj.getInvitedUsers(),
                            obj.getTime(), obj.getMinTemp(), obj.getMaxTemp(), obj.getWeatherCondition());
                }
                databaseHandler.deleteEvent(obj.getName());
                DayFragment.c.eORt.remove(pos);
                DayFragment.eventArrayAdapter.notifyDataSetChanged();
            }

            finish();
        });

        back.setOnClickListener(v -> finish());
    }


    private Task<String> updateEvent(Event newEvent, Event oldEvent) {
        String id = mAuth.getUid();
        String name = mAuth.getCurrentUser().getDisplayName();
        String email = mAuth.getCurrentUser().getEmail();
        Gson gson = new Gson();
        String json = gson.toJson(newEvent);
        String json2 = gson.toJson(oldEvent);

        Map<String, Object> data = new HashMap<>();
        data.put("eventOwner", id);
        data.put("eventOwnerName", name);
        data.put("eventOwnerEmail", email);
        data.put("day", newEvent.getDay());
        data.put("event", json);
        data.put("oldEvent", json2);

        return mFunctions
                .getHttpsCallable("updateEvent")
                .call(data)
                .continueWith(Task -> {
                    String result = (String) Task.getResult().getData();
                    return result;
                });
    }

    private Task<String> removeEvent(Event event) {
        String id = mAuth.getUid();
        Gson gson = new Gson();
        String json = gson.toJson(event);

        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        databaseHandler.deleteEvent(event.getName());

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

        dayTV.setTextColor(this.getResources().getColor(colour.title));
        descTV.setTextColor(this.getResources().getColor(colour.title));
        invitedUsersTV.setTextColor(this.getResources().getColor(colour.title));
        timeTV.setTextColor(this.getResources().getColor(colour.title));
        minTempTV.setTextColor(this.getResources().getColor(colour.title));
        maxTempTV.setTextColor(this.getResources().getColor(colour.title));
        weatherConditionTV = findViewById(R.id.weatherConditionTV);
        eventDay.setTextColor(this.getResources().getColor(colour.title));
        eventName.setTextColor(this.getResources().getColor(colour.title));
        eventDesc.setTextColor(this.getResources().getColor(colour.title));
        invitedUsers.setTextColor(this.getResources().getColor(colour.title));
        time.setTextColor(this.getResources().getColor(colour.title));
        minTemp.setTextColor(this.getResources().getColor(colour.title));
        maxTemp.setTextColor(this.getResources().getColor(colour.title));
        condition.setTextColor(this.getResources().getColor(colour.title));
        updatedDayET.setTextColor(this.getResources().getColor(colour.title));
        updatedDayET.setHintTextColor(this.getResources().getColor(colour.text));
        updateDescET.setTextColor(this.getResources().getColor(colour.title));
        updateDescET.setHintTextColor(this.getResources().getColor(colour.text));
        updatedInvitedET.setTextColor(this.getResources().getColor(colour.title));
        updatedInvitedET.setHintTextColor(this.getResources().getColor(colour.text));
        updatedTimeET.setTextColor(this.getResources().getColor(colour.title));
        updatedTimeET.setHintTextColor(this.getResources().getColor(colour.text));
        updatedMinTempET.setTextColor(this.getResources().getColor(colour.title));
        updatedMinTempET.setHintTextColor(this.getResources().getColor(colour.text));
        updatedMaxTempET.setTextColor(this.getResources().getColor(colour.title));
        updatedMaxTempET.setHintTextColor(this.getResources().getColor(colour.text));

        edit.setTextColor(this.getResources().getColor(colour.title));
        back.setTextColor(this.getResources().getColor(colour.title));
        applyChanges.setTextColor(this.getResources().getColor(colour.title));
        remove.setTextColor(this.getResources().getColor(colour.title));
        cancel.setTextColor(this.getResources().getColor(colour.title));

        DatabaseHandler databaseHandler1 = new DatabaseHandler(this);
        String[] theme = databaseHandler1.getTheme("theme").toArray(new String[0]);
        if(theme.length != 0) {
            if (theme[0].equals("Light")) {
                edit.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                applyChanges.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                back.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                cancel.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
            }
            else if (theme[0].equals("Dark")) {
                edit.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                applyChanges.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                back.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                cancel.setBackground(this.getResources().getDrawable(R.drawable.button_green));
            }
            else {
                edit.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                applyChanges.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                back.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                cancel.setBackground(this.getResources().getDrawable(R.drawable.button_white));
            }
        }
    }
}
