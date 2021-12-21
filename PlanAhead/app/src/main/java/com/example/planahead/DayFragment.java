package com.example.planahead;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DayFragment extends Fragment implements ThemeManager{

    private View v;
    DatabaseReference databaseReference;
    FirebaseFunctions mFunctions;
    FirebaseAuth mAuth;

    static EventListAdapter eventArrayAdapter;
    static Day c;
    ScrollView ll;
    LinearLayout lll;
    LinearLayout l;
    LinearLayout dialogBG;
    static TextView date;
    TextView title;
    TextView temperature;
    TextView weatherDescription;
    TextView spinnerTitle;
    TextView emptyView;
    ImageView weatherImage;

    MyWeather weather = new MyWeather();
    String eventOwnerEmail;
    String datee;

    Button add ;
    Button cancel ;
    EditText nameET;
    EditText descET;
    EditText inviteET ;
    EditText timeET;
    EditText minTemp ;
    EditText maxTemp ;
    ListView listView;
    Spinner weatherSpinner;
    FloatingActionMenu floatingMenu;
    FloatingActionButton addTask;
    FloatingActionButton addEvent;

    public DayFragment() {
        // Required empty public constructor
    }

    public DayFragment(Day d, int pos, int mon, ArrayList<Day> nums)
    {
        c = d;
        //position = pos;
        //m = mon;
        //numbers = nums;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_day, container, false);
        mFunctions = FirebaseFunctions.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("/users/" + mAuth.getUid() + "/events/");

        title = v.findViewById(R.id.title);
        weatherImage = v.findViewById(R.id.weatherImageID);
        weatherDescription = v.findViewById(R.id.weatherDescription);
        listView = v.findViewById(R.id.eventsListView);
        emptyView = v.findViewById(R.id.empty_event_item);
        ll = v.findViewById(R.id.parentScrollView);
        lll = v.findViewById(R.id.linLayid2);
        l = v.findViewById(R.id.weatherContainerID);
        floatingMenu = v.findViewById(R.id.floatingMenu);
        addTask = v.findViewById(R.id.floatingMenuAddTask);
        addEvent = v.findViewById(R.id.floatingMenuAddEvent);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialogBG = mView.findViewById(R.id.dialogBG);
        title = mView.findViewById(R.id.title);
        spinnerTitle = mView.findViewById(R.id.spinnerTitle);
        add = mView.findViewById(R.id.create);
        cancel = mView.findViewById(R.id.cancel);
        nameET = mView.findViewById(R.id.nameET);
        descET = mView.findViewById(R.id.descET);
        inviteET = mView.findViewById(R.id.invitedUsersET);
        timeET = mView.findViewById(R.id.timeET);
        minTemp = mView.findViewById(R.id.minTempET);
        maxTemp = mView.findViewById(R.id.maxTempET);
        weatherSpinner = mView.findViewById(R.id.spinner);
        weatherSpinner.setSelection(0);
        LinearLayout inviteLayout = mView.findViewById(R.id.inviteLayout);
        LinearLayout tempLayout = mView.findViewById(R.id.tempLayout);
        LinearLayout timeLayout = mView.findViewById(R.id.timeLayout);
        LinearLayout preferredWeather = mView.findViewById(R.id.preferredCondition);

        temperature = v.findViewById(R.id.tempID);
        date = v.findViewById(R.id.date);

        datee = c.day + " " + c.month + " " + c.year;
        MyDate date3 = new MyDate();
        String today = date3.getDay() + " " + date3.getMonth(0) + " " + date3.getYear();

        date.setText(datee);

        changeThemeColour(HomeActivity.colour);

        addTask.setOnClickListener(v1 -> {
            floatingMenu.close(true);
            if(datee.equals(today))
            {
                title.setText("Task For Today");
            }
            else { title.setText("Task For " + datee); }

            inviteLayout.setVisibility(View.GONE);
            tempLayout.setVisibility(View.GONE);
            preferredWeather.setVisibility(View.GONE);
            timeLayout.setVisibility(View.GONE);
            dialog.show();
        });

        addEvent.setOnClickListener(v1 -> {
            floatingMenu.close(true);
            if(datee.equals(today))
            {
                title.setText("Event For Today");
            }
            else { title.setText("Event For " + datee); }
            inviteLayout.setVisibility(View.VISIBLE);
            timeLayout.setVisibility(View.VISIBLE);
            tempLayout.setVisibility(View.VISIBLE);
            preferredWeather.setVisibility(View.VISIBLE);
            dialog.show();
        });

        add.setOnClickListener(v3 -> {
            Date date = new Date();
            String stringDate = DateFormat.getTimeInstance().format(date);
            String day = c.day + " " + c.month + " " + c.year;
            MyDate date2 = new MyDate();
            String day2 = date2.getDay() + " " + date2.getMonth(0) + " " + date2.getYear();
            if(title.getText().toString().contains("Task")){
                if(nameET.getText().toString().replace(" ", "").equals("")) {
                    Toasty.warning(getActivity(), "Task Name Cannot Be Blank", Toast.LENGTH_SHORT, true).show();
                }
                else {
                    Task task = new Task(day, nameET.getText().toString(), descET.getText().toString(), stringDate);
                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
                    if(!isNetworkAvailable()) {
                        databaseHandler.insertTaskToBeAdded(day, nameET.getText().toString(),descET.getText().toString(), stringDate);
                        databaseHandler.insertTask(day, nameET.getText().toString(),descET.getText().toString(), stringDate);
                    }
                    databaseHandler.insertTask(day, nameET.getText().toString(),descET.getText().toString(), stringDate);
                    c.tasks.add(task);
                    c.eORt.add(task);
                    eventArrayAdapter.notifyDataSetChanged();
                    if(task.getDay().equals(day2)) {
                        HomeActivity.c.tasks.add(task);
                        HomeActivity.c.eORt.add(task);
                    }
                    if(isNetworkAvailable()) {
                        addTask(task)
                                .addOnCompleteListener(t -> {
                                    if (!t.isSuccessful()) {
                                        Exception e = t.getException();

                                        if (e instanceof FirebaseFunctionsException) {
                                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                            FirebaseFunctionsException.Code code = ffe.getCode();
                                            Object details = ffe.getDetails();
                                            Log.w("AddTASK", "addTask:onFailure", e);
                                        }
                                    }
                                });
                    }
                    nameET.setText("");
                    descET.setText("");
                    inviteET.setText("");
                    timeET.setText("");
                    minTemp.setText("");
                    maxTemp.setText("");
                    weatherSpinner.setSelection(0);
                    dialog.cancel();
                }
            }
            else {
                if(nameET.getText().toString().replace(" ", "").equals("")) {
                    Toasty.warning(getActivity(), "Event Name Cannot Be Blank", Toast.LENGTH_SHORT, true).show();
                }
                else if (timeET.getText().toString().replace(" ", "").equals("")) {
                    Toasty.warning(getActivity(), "Time Cannot Be Blank", Toast.LENGTH_SHORT, true).show();
                }
                else if(timeET.getText().toString().toUpperCase().replace(" ", "").contains("PM") ||
                        timeET.getText().toString().toUpperCase().replace(" ", "").contains("AM") ||
                        timeET.getText().toString().toUpperCase().replace(" ", "").contains("P.M.") ||
                        timeET.getText().toString().toUpperCase().replace(" ", "").contains("A.M.")) {
                    String[] users = inviteET.getText().toString().replace(" ", "").split(",");
                    String eventOwner = mAuth.getCurrentUser().getEmail();
                    Long min;
                    Long max;
                    if(minTemp.getText().toString().equals("")){
                        min = Long.valueOf(-1000);
                    }
                    else { min = Long.valueOf(minTemp.getText().toString());}
                    if(maxTemp.getText().toString().equals("")){
                        max = Long.valueOf(1000);
                    }
                    else { max = Long.valueOf(maxTemp.getText().toString());}
                    String weatherCondition = weatherSpinner.getSelectedItem().toString();
                    if(users[0].equals("")) { users = new String[0]; }
                    Event event = new Event(DayFragment.date.getText().toString(), nameET.getText().toString(), descET.getText().toString(), eventOwner, users, timeET.getText().toString(), min, max, weatherCondition);
                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
                    if(!isNetworkAvailable()) {
                        databaseHandler.insertEventToBeAdded(day2, nameET.getText().toString(), descET.getText().toString(), eventOwner, users, timeET.getText().toString(), min, max, weatherCondition);
                        databaseHandler.insertEvent(day2, nameET.getText().toString(), descET.getText().toString(), eventOwner, users, timeET.getText().toString(), min, max, weatherCondition);
                    }
                    databaseHandler.insertEvent(day2, nameET.getText().toString(), descET.getText().toString(), eventOwner, users, timeET.getText().toString(), min, max, weatherCondition);
                    c.events.add(event);
                    c.eORt.add(event);
                    eventArrayAdapter.notifyDataSetChanged();
                    if(event.getDay().equals(day2)) {
                        HomeActivity.c.events.add(event);
                        HomeActivity.c.eORt.add(event);
                        HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                    }
                    if(isNetworkAvailable()) {
                        sendEmail(users);
                        addEvent(event)
                                .addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()) {
                                        Exception e = task.getException();

                                        if (e instanceof FirebaseFunctionsException) {
                                            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                            FirebaseFunctionsException.Code code = ffe.getCode();
                                            Object details = ffe.getDetails();
                                            Log.w("AddEVENT", "addEvent:onFailure", e);
                                        }
                                    }
                                });
                    }
                    nameET.setText("");
                    descET.setText("");
                    inviteET.setText("");
                    timeET.setText("");
                    minTemp.setText("");
                    maxTemp.setText("");
                    weatherSpinner.setSelection(0);
                    dialog.cancel();
                }
                else {
                    Toasty.warning(getActivity(), "Please Mention AM or PM For Time", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        cancel.setOnClickListener(v4 -> {
            nameET.setText("");
            descET.setText("");
            inviteET.setText("");
            timeET.setText("");
            minTemp.setText("");
            maxTemp.setText("");
            weatherSpinner.setSelection(0);
            dialog.cancel();
        });

        MyDate mydate = new MyDate();
        int g = mydate.getDay();
        String checkDay = g+"";
        if(c.day.equals(checkDay) && c.month.equals(mydate.getMonth(0)))
        {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                String[] strArray = weather.getCurrentDescription().split(" ");
                StringBuilder builder = new StringBuilder();
                for (String s : strArray) {
                    String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                    builder.append(cap + " ");
                }
                String t = String.format("%.1f", weather.getCurrentTemp());
                temperature.setText(t + "°C");
                weatherDescription.setText( builder.toString() + "");
                if (weather.getCurrentDescription().equals("clear sky")){
                    weatherImage.setImageResource(R.drawable.sunny1);
                }
                else if(weather.getCurrentDescription().equals("overcast clouds") || weather.getCurrentDescription().equals("broken clouds")
                        || weather.getCurrentDescription().contains("Dreary") || weather.getCurrentDescription().contains("Cloudy") || weather.getCurrentDescription().contains("cloudy")) {
                    weatherImage.setImageResource(R.drawable.cloudy2);
                }
                else if (weather.getCurrentDescription().equals("scattered clouds")) {
                    weatherImage.setImageResource(R.drawable.cloudy1);
                }
                else if (weather.getCurrentDescription().equals("few clouds")) {
                    weatherImage.setImageResource(R.drawable.cloudy);
                }
                else if (weather.getCurrentDescription().contains("Snow") || weather.getCurrentDescription().contains("snow")
                        || weather.getCurrentDescription().contains("Sleet") || weather.getCurrentDescription().contains("sleet")) {
                    weatherImage.setImageResource(R.drawable.snowy);
                }
                else if (weather.getCurrentDescription().contains("Drizzle") || weather.getCurrentDescription().contains("drizzle")){
                    weatherImage.setImageResource(R.drawable.drizzle);
                }
                else if (weather.getCurrentDescription().equals("light rain") || weather.getCurrentDescription().equals("moderate rain")
                        || weather.getCurrentDescription().equals("heavy intensity rain") || weather.getCurrentDescription().equals("very heavy rain")
                        || weather.getCurrentDescription().equals("extreme rain")) {
                    weatherImage.setImageResource(R.drawable.rain);
                }
                else if (weather.getCurrentDescription().equals("light intensity shower rain") || weather.getCurrentDescription().equals("shower rain")
                        || weather.getCurrentDescription().equals("heavy intensity shower rain") || weather.getCurrentDescription().equals("ragged shower rain")) {
                    weatherImage.setImageResource(R.drawable.drizzle);
                }
                else if (weather.getCurrentDescription().equals("freezing rain")) {
                    weatherImage.setImageResource(R.drawable.freezingrain);
                }
                else if (weather.getCurrentDescription().contains("thunderstorm") || weather.getCurrentDescription().contains("Thunderstorm")) {
                    weatherImage.setImageResource(R.drawable.thunderstorm);
                }
                else if (weather.getCurrentDescription().equals("fog") || weather.getCurrentDescription().equals("mist")
                        || weather.getCurrentDescription().equals("Smoke") || weather.getCurrentDescription().equals("Haze")
                        || weather.getCurrentDescription().equals("sand/ dust whirls") || weather.getCurrentDescription().equals("sand")
                        || weather.getCurrentDescription().equals("dust") || weather.getCurrentDescription().equals("squalls")
                        || weather.getCurrentDescription().equals("tornado")) {
                    weatherImage.setImageResource(R.drawable.fog);
                }
                else {
                    weatherImage.setImageResource(R.drawable.noweather);
                }
            }, 0);
        }
        else if(c.day.equals(checkDay = g+1+"") && c.month.equals(mydate.getMonth(0)))
        {
            String[] strArray = HomeFragment.arr[0].split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }
            String t = String.format("%.1f", HomeFragment.tempArr[0]);
            temperature.setText(t + "°C");
            weatherDescription.setText( builder.toString() + "");
            if(builder.toString().equals("Mostly Cloudy W/ Snow ") || builder.toString().equals("Mostly Cloudy W/ Flurries ")
                    || builder.toString().equals("Snow ") || builder.toString().equals("Partly Sunny W/ Flurries ") || builder.toString().contains("Flurries ")){
                weatherImage.setImageResource(R.drawable.snowy);
            }
            else if (builder.toString().equals("Rain ") || builder.toString().equals("Showers ")
                    || builder.toString().equals("Mostly Cloudy W/ Showers ") || builder.toString().equals("Mostly Cloudy W/ Showers ")
                    || builder.toString().equals("Partly Cloudy W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain2);
            }
            else if (builder.toString().equals("Partly Sunny W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain);
            }
            else if (builder.toString().equals("Mostly Cloudy ") || builder.toString().equals("Cloudy ")
                    || builder.toString().equals("Dreary ") || builder.toString().equals("Partly Cloudy ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.cloudy2);
            }
            else if (builder.toString().equals("Freezing Rain ") || builder.toString().equals("Sleet ")
                    || builder.toString().equals("Rain And Snow ") || builder.toString().equals("Ice ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.freezingrain);
            }
            else if (builder.toString().equals("Windy ") || builder.toString().equals("Fog ")){
                weatherImage.setImageResource(R.drawable.fog);
            }
            else if (builder.toString().contains("Sunny ")){
                weatherImage.setImageResource(R.drawable.sunny1);
            }
            else if (builder.toString().contains("Storms") || builder.toString().contains("Thunderstorms")){
                weatherImage.setImageResource(R.drawable.thunderstorm);
            }
            else {
                weatherImage.setImageResource(R.drawable.noweather);
            }
        }
        else if(c.day.equals(checkDay = g+2+"") && c.month.equals(mydate.getMonth(0)))
        {
            String[] strArray = HomeFragment.arr[1].split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }
            String t = String.format("%.1f", HomeFragment.tempArr[1]);
            temperature.setText(t + "°C");
            weatherDescription.setText( builder.toString() + "");
            if(builder.toString().equals("Mostly Cloudy W/ Snow ") || builder.toString().equals("Mostly Cloudy W/ Flurries ")
                    || builder.toString().equals("Snow ") || builder.toString().equals("Partly Sunny W/ Flurries ") || builder.toString().contains("Flurries ")){
                weatherImage.setImageResource(R.drawable.snowy);
            }
            else if (builder.toString().equals("Rain ") || builder.toString().equals("Showers ")
                    || builder.toString().equals("Mostly Cloudy W/ Showers ") || builder.toString().equals("Mostly Cloudy W/ Showers ")
                    || builder.toString().equals("Partly Cloudy W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain2);
            }
            else if (builder.toString().equals("Partly Sunny W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain);
            }
            else if (builder.toString().equals("Mostly Cloudy ") || builder.toString().equals("Cloudy ")
                    || builder.toString().equals("Dreary ") || builder.toString().equals("Partly Cloudy ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.cloudy2);
            }
            else if (builder.toString().equals("Freezing Rain ") || builder.toString().equals("Sleet ")
                    || builder.toString().equals("Rain And Snow ") || builder.toString().equals("Ice ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.freezingrain);
            }
            else if (builder.toString().equals("Windy ") || builder.toString().equals("Fog ")){
                weatherImage.setImageResource(R.drawable.fog);
            }
            else if (builder.toString().contains("Sunny ")){
                weatherImage.setImageResource(R.drawable.sunny1);
            }
            else if (builder.toString().contains("Storms") || builder.toString().contains("Thunderstorms")){
                weatherImage.setImageResource(R.drawable.thunderstorm);
            }
            else {
                weatherImage.setImageResource(R.drawable.noweather);
            }
        }
        else if(c.day.equals(checkDay = g+3+"") && c.month.equals(mydate.getMonth(0)))
        {
            String[] strArray = HomeFragment.arr[2].split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }
            String t = String.format("%.1f", HomeFragment.tempArr[2]);
            temperature.setText(t + "°C");
            weatherDescription.setText( builder.toString() + "");
            if(builder.toString().equals("Mostly Cloudy W/ Snow ") || builder.toString().equals("Mostly Cloudy W/ Flurries ")
                    || builder.toString().equals("Snow ") || builder.toString().equals("Partly Sunny W/ Flurries ") || builder.toString().contains("Flurries ")){
                weatherImage.setImageResource(R.drawable.snowy);
            }
            else if (builder.toString().equals("Rain ") || builder.toString().equals("Showers ")
                    || builder.toString().equals("Mostly Cloudy W/ Showers ") || builder.toString().equals("Mostly Cloudy W/ Showers ")
                    || builder.toString().equals("Partly Cloudy W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain2);
            }
            else if (builder.toString().equals("Partly Sunny W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain);
            }
            else if (builder.toString().equals("Mostly Cloudy ") || builder.toString().equals("Cloudy ")
                    || builder.toString().equals("Dreary ") || builder.toString().equals("Partly Cloudy ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.cloudy2);
            }
            else if (builder.toString().equals("Freezing Rain ") || builder.toString().equals("Sleet ")
                    || builder.toString().equals("Rain And Snow ") || builder.toString().equals("Ice ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.freezingrain);
            }
            else if (builder.toString().equals("Windy ") || builder.toString().equals("Fog ")){
                weatherImage.setImageResource(R.drawable.fog);
            }
            else if (builder.toString().contains("Sunny ")){
                weatherImage.setImageResource(R.drawable.sunny1);
            }
            else if (builder.toString().contains("Storms") || builder.toString().contains("Thunderstorms")){
                weatherImage.setImageResource(R.drawable.thunderstorm);
            }
            else {
                weatherImage.setImageResource(R.drawable.noweather);
            }
        }
        else if(c.day.equals(checkDay = g+4+"") && c.month.equals(mydate.getMonth(0)))
        {
            String[] strArray = HomeFragment.arr[3].split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }
            String t = String.format("%.1f", HomeFragment.tempArr[3]);
            temperature.setText(t + "°C");
            weatherDescription.setText( builder.toString() + "");
            if(builder.toString().equals("Mostly Cloudy W/ Snow ") || builder.toString().equals("Mostly Cloudy W/ Flurries ")
                    || builder.toString().equals("Snow ") || builder.toString().equals("Partly Sunny W/ Flurries ") || builder.toString().contains("Flurries ")){
                weatherImage.setImageResource(R.drawable.snowy);
            }
            else if (builder.toString().equals("Rain ") || builder.toString().equals("Showers ")
                    || builder.toString().equals("Mostly Cloudy W/ Showers ") || builder.toString().equals("Mostly Cloudy W/ Showers ")
                    || builder.toString().equals("Partly Cloudy W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain2);
            }
            else if (builder.toString().equals("Partly Sunny W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain);
            }
            else if (builder.toString().equals("Mostly Cloudy ") || builder.toString().equals("Cloudy ")
                    || builder.toString().equals("Dreary ") || builder.toString().equals("Partly Cloudy ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.cloudy2);
            }
            else if (builder.toString().equals("Freezing Rain ") || builder.toString().equals("Sleet ")
                    || builder.toString().equals("Rain And Snow ") || builder.toString().equals("Ice ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.freezingrain);
            }
            else if (builder.toString().equals("Windy ") || builder.toString().equals("Fog ")){
                weatherImage.setImageResource(R.drawable.fog);
            }
            else if (builder.toString().contains("Sunny ")){
                weatherImage.setImageResource(R.drawable.sunny1);
            }
            else if (builder.toString().contains("Storms") || builder.toString().contains("Thunderstorms")){
                weatherImage.setImageResource(R.drawable.thunderstorm);
            }
            else {
                weatherImage.setImageResource(R.drawable.noweather);
            }
        }
        else if(c.day.equals(checkDay = g+5+"") && c.month.equals(mydate.getMonth(0)))
        {
            String[] strArray = HomeFragment.arr[4].split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap + " ");
            }
            String t = String.format("%.1f", HomeFragment.tempArr[4]);
            temperature.setText(t + "°C");
            weatherDescription.setText( builder.toString() + "");
            if(builder.toString().equals("Mostly Cloudy W/ Snow ") || builder.toString().equals("Mostly Cloudy W/ Flurries ")
                    || builder.toString().equals("Snow ") || builder.toString().equals("Partly Sunny W/ Flurries ") || builder.toString().contains("Flurries ")){
                weatherImage.setImageResource(R.drawable.snowy);
            }
            else if (builder.toString().equals("Rain ") || builder.toString().equals("Showers ")
                    || builder.toString().equals("Mostly Cloudy W/ Showers ") || builder.toString().equals("Mostly Cloudy W/ Showers ")
                    || builder.toString().equals("Partly Cloudy W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain2);
            }
            else if (builder.toString().equals("Partly Sunny W/ Showers ")){
                weatherImage.setImageResource(R.drawable.rain);
            }
            else if (builder.toString().equals("Mostly Cloudy ") || builder.toString().equals("Cloudy ")
                    || builder.toString().equals("Dreary ") || builder.toString().equals("Partly Cloudy ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.cloudy2);
            }
            else if (builder.toString().equals("Freezing Rain ") || builder.toString().equals("Sleet ")
                    || builder.toString().equals("Rain And Snow ") || builder.toString().equals("Ice ")
                    || builder.toString().equals("Intermittent Clouds ")){
                weatherImage.setImageResource(R.drawable.freezingrain);
            }
            else if (builder.toString().equals("Windy ") || builder.toString().equals("Fog ")){
                weatherImage.setImageResource(R.drawable.fog);
            }
            else if (builder.toString().contains("Sunny ")){
                weatherImage.setImageResource(R.drawable.sunny1);
            }
            else if (builder.toString().contains("Storms") || builder.toString().contains("Thunderstorms")){
                weatherImage.setImageResource(R.drawable.thunderstorm);
            }
            else {
                weatherImage.setImageResource(R.drawable.noweather);
            }
        }
        else
        {
            temperature.setText("0.0°C");
            weatherImage.setImageResource(R.drawable.noweather);
            weatherDescription.setText("Unavailable");
        }

        if (datee.equals(today)) {
            emptyView.setText("No tasks or events for today.");
        }
        else { emptyView.setText("No tasks or events for " + datee + "."); }

        listView.setEmptyView(emptyView);
        eventArrayAdapter = new EventListAdapter(getActivity(), c.eORt);
        listView.setAdapter(eventArrayAdapter);

        listView.setOnItemLongClickListener((arg0, v12, index, arg3) -> {
            ImageButton delete = v12.findViewById(R.id.trashButton);
            if(delete.getVisibility() == View.VISIBLE) {
                delete.setVisibility(View.GONE);
            }
            else {
                delete.setVisibility(View.VISIBLE);
            }
            return true;
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if(c.eORt.get(position).getClass() == Event.class) {
                Event obj = (Event) c.eORt.get(position);
                Intent intent3 = new Intent(getActivity(), EventDetails.class);
                intent3.putExtra("calledFrom", "TaskActivity");
                intent3.putExtra("pos", position);
                intent3.putExtra("eventOwnerEmail", obj.getEventOwner());
                intent3.putExtra("day", obj.getDay());
                intent3.putExtra("name", obj.getName());
                intent3.putExtra("description", obj.getDescription());
                intent3.putExtra("invited", obj.getInvitedUsers());
                intent3.putExtra("time", obj.getTime());
                intent3.putExtra("minTemp", obj.getMinTemp());
                intent3.putExtra("maxTemp", obj.getMaxTemp());
                intent3.putExtra("weatherCondition", obj.getWeatherCondition());
                startActivity(intent3);
            }
            else if(c.eORt.get(position).getClass() == Task.class) {
                Task obj = (Task) c.eORt.get(position);
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("calledFrom", String.valueOf(getActivity()));
                intent.putExtra("position", position);
                intent.putExtra("task", obj);
                startActivity(intent);
            }
        });

        String date1 = c.day + " " + c.month + " " + c.year;
        if(isNetworkAvailable()) {
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        String day = (String) messageSnapshot.child("day").getValue();
                        String name = (String) messageSnapshot.child("name").getValue();
                        String desc = (String) messageSnapshot.child("desc").getValue();
                        String owner = (String) messageSnapshot.child("eventOwner").getValue();
                        eventOwnerEmail = (String) messageSnapshot.child("eventOwnerEmail").getValue();
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
                        Event event = new Event(day, name, desc, owner, users, time, minTemp, maxTemp, weatherCondition);
                        boolean found = false;
                        if (!c.events.isEmpty()) {
                            for (int i = 0; i < c.events.size(); i++) {
                                if (c.events.get(i).getName().equals(event.getName())) {
                                    found = true;
                                }
                            }
                            if (!found && event.getDay().equals(date1)) {
                                c.events.add(event);
                                c.eORt.add(event);
                                eventArrayAdapter.notifyDataSetChanged();
                                HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                            }
                        } else if (event.getDay().equals(date1)) {
                            c.events.add(event);
                            c.eORt.add(event);
                            eventArrayAdapter.notifyDataSetChanged();
                            HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    eventArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    eventArrayAdapter.notifyDataSetChanged();
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
            String day1[] = databaseHandler.getAllEvents("day").toArray(new String[0]);
            String name1[] = databaseHandler.getAllEvents("name").toArray(new String[0]);
            String desc1[] = databaseHandler.getAllEvents("description").toArray(new String[0]);
            String eventOwner[] = databaseHandler.getAllEvents("eventOwner").toArray(new String[0]);
            String invitedUsers[] = databaseHandler.getAllEvents("invitedUsers").toArray(new String[0]);
            String time1[] = databaseHandler.getAllEvents("time").toArray(new String[0]);
            String minTemp2[] = databaseHandler.getAllEvents("minTemp").toArray(new String[0]);
            String maxTemp2[] = databaseHandler.getAllEvents("maxTemp").toArray(new String[0]);
            String weatherCondition[] = databaseHandler.getAllEvents("weatherCondition").toArray(new String[0]);
            for(int i = 0; i < day1.length; i++) {
                String[] arr = invitedUsers[i].split(",");
                Event event = new Event (day1[i], name1[i], desc1[i], eventOwner[i], arr, time1[i], Long.valueOf(minTemp2[i]), Long.valueOf(maxTemp2[i]), weatherCondition[i]);
                eventOwnerEmail = eventOwner[i];
                boolean found = false;
                if (!c.events.isEmpty()) {
                    for (int j = 0; j < c.events.size(); j++) {
                        if (c.events.get(j).getName().equals(event.getName())) {
                            found = true;
                        }
                    }
                    if (!found && date1.equals(day1[i])) {
                        c.events.add(event);
                        c.eORt.add(event);
                        eventArrayAdapter.notifyDataSetChanged();
                    }
                } else if (date1.equals(day1[i])) {
                    c.events.add(event);
                    c.eORt.add(event);
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        }

        if(isNetworkAvailable()) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("/users/" + mAuth.getUid() + "/tasks/");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        String day = (String) messageSnapshot.child("day").getValue();
                        String name = (String) messageSnapshot.child("name").getValue();
                        String desc = (String) messageSnapshot.child("desc").getValue();
                        String time = (String) messageSnapshot.child("timeAdded").getValue();
                        com.example.planahead.Task task = new com.example.planahead.Task(day, name, desc, time);
                        boolean found = false;
                        if (!c.tasks.isEmpty()) {
                            for (int i = 0; i < c.tasks.size(); i++) {
                                if (c.tasks.get(i).getName().equals(task.getName())) {
                                    found = true;
                                }
                            }
                            if (!found && task.getDay().equals(date1)) {
                                c.tasks.add(task);
                                c.eORt.add(task);
                                eventArrayAdapter.notifyDataSetChanged();
                            }
                        } else if (task.getDay().equals(date1)) {
                            c.tasks.add(task);
                            c.eORt.add(task);
                            eventArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    eventArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    eventArrayAdapter.notifyDataSetChanged();
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
            String day[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
            String name[]= databaseHandler.getAllTasks("name").toArray(new String[0]);
            String desc[]= databaseHandler.getAllTasks("description").toArray(new String[0]);
            String time[]= databaseHandler.getAllTasks("timeAdded").toArray(new String[0]);

            for(int i = 0; i < day.length; i++) {
                com.example.planahead.Task task = new com.example.planahead.Task (day[i], name[i], desc[i], time[i]);
                boolean found = false;
                if (!c.tasks.isEmpty()) {
                    for (int j = 0; j < c.tasks.size(); j++) {
                        if (c.tasks.get(j).getName().equals(task.getName())) {
                            found = true;
                        }
                    }
                    if (!found && task.getDay().equals(date1)) {
                        c.tasks.add(task);
                        c.eORt.add(task);
                        eventArrayAdapter.notifyDataSetChanged();
                    }
                } else if (task.getDay().equals(date1)) {
                    c.tasks.add(task);
                    c.eORt.add(task);
                    eventArrayAdapter.notifyDataSetChanged();
                }
            }
        }

        return v;
    }

    private com.google.android.gms.tasks.Task<Object> addTask(Task task) {
        String id = mAuth.getUid();
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

    private void sendEmail(String[] users) {
        EmailAsyncTask task = new EmailAsyncTask(users);
        task.execute();
    }

    private com.google.android.gms.tasks.Task<Object> addEvent(Event event) {
        String id = mAuth.getUid();
        String name = mAuth.getCurrentUser().getDisplayName();
        String email = mAuth.getCurrentUser().getEmail();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void changeThemeColour(Colour colour) {
        if(colour.type != 0 && colour.type != 1) {
            lll.setBackground(getContext().getResources().getDrawable(colour.background));
            dialogBG.setBackground(getContext().getResources().getDrawable(colour.background));
        }
        else {
            lll.setBackgroundColor(getContext().getResources().getColor(colour.background));
            dialogBG.setBackgroundColor(getContext().getResources().getColor(colour.background));
        }
        date.setTextColor(getContext().getResources().getColor(colour.title));
        temperature.setTextColor(getContext().getResources().getColor(colour.title));
        weatherDescription.setTextColor(getContext().getResources().getColor(colour.title));

        title.setTextColor(getContext().getResources().getColor(colour.title));
        add.setBackgroundColor(getContext().getResources().getColor(colour.buttons));
        cancel.setBackgroundColor(getContext().getResources().getColor(colour.buttons));
        nameET.setTextColor(getContext().getResources().getColor(colour.title));
        nameET.setHintTextColor(getContext().getResources().getColor(colour.text));
        descET.setTextColor(getContext().getResources().getColor(colour.title));
        descET.setHintTextColor(getContext().getResources().getColor(colour.text));
        inviteET.setTextColor(getContext().getResources().getColor(colour.title));
        inviteET.setHintTextColor(getContext().getResources().getColor(colour.text));
        timeET.setTextColor(getContext().getResources().getColor(colour.title));
        timeET.setHintTextColor(getContext().getResources().getColor(colour.text));
        minTemp.setTextColor(getContext().getResources().getColor(colour.title));
        minTemp.setHintTextColor(getContext().getResources().getColor(colour.text));
        maxTemp.setTextColor(getContext().getResources().getColor(colour.title));
        maxTemp.setHintTextColor(getContext().getResources().getColor(colour.text));
        emptyView.setTextColor(getContext().getResources().getColor(colour.title));
    }
}
