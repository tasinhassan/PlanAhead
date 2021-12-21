package com.example.planahead;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class HomeActivity extends AppCompatActivity {

   private static final String TAG = "Home";
    DatabaseReference ref;
    static BottomNavigationView bottomNav;
    private FirebaseFunctions mFunctions;
    static FirebaseAuth mAuth;
    static Day c;
    public static Colour colour;
    public static ArrayList<Integer> i = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.ic_house:
                selectedFragment = new HomeFragment();
                i.add(0);
                break;
            case R.id.ic_calendar:
                selectedFragment = new CalendarFragment();
                i.add(1);
                break;
            case R.id.ic_account:
                selectedFragment = new SettingsFragment();
                i.add(2);
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).addToBackStack(null).commit();

        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottomNavViewBar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        i.add(0);

        mFunctions = FirebaseFunctions.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.keepSynced(true);

        colour = new Colour();
        DatabaseHandler databaseHandler1 = new DatabaseHandler(getApplicationContext());

        String[] theme = databaseHandler1.getTheme("theme").toArray(new String[0]);
        if(theme.length != 0) {
            if (theme[0].equals("Light")) {
                colour.lightTheme();
                ColorStateList navMenuIconList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled},
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_pressed}
                        },
                        new int[] {
                                getResources().getColor(R.color.black),
                                getResources().getColor(R.color.grey),
                                getResources().getColor(R.color.black),
                                getResources().getColor(R.color.grey),
                                getResources().getColor(R.color.black)
                        }
                );
                bottomNav.setBackground(getResources().getDrawable(R.drawable.white_grey_border_top));
                bottomNav.setItemIconTintList(navMenuIconList);
            } else if (theme[0].equals("Dark")) {
                colour.darkTheme();
                ColorStateList navMenuIconList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled},
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_pressed}
                        },
                        new int[] {
                                getResources().getColor(R.color.greenbutton),
                                getResources().getColor(R.color.black),
                                getResources().getColor(R.color.greenbutton),
                                getResources().getColor(R.color.black),
                                getResources().getColor(R.color.greenbutton)
                        }
                );
                bottomNav.setBackground(getResources().getDrawable(R.drawable.black_grey_border_top));
                bottomNav.setItemIconTintList(navMenuIconList);
            } else if (theme[0].equals("Nightshade")) {
                colour.nightshadeTheme();
                ColorStateList navMenuIconList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled},
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_pressed}
                        },
                        new int[] {
                                getResources().getColor(R.color.white),
                                getResources().getColor(R.color.black),
                                getResources().getColor(R.color.white),
                                getResources().getColor(R.color.black),
                                getResources().getColor(R.color.white)
                        }
                );
                bottomNav.setBackground(getResources().getDrawable(R.drawable.nightshade_nav_border));
                bottomNav.setItemIconTintList(navMenuIconList);
            }
        }

        MyDate date = new MyDate();
        c = new Day(String.valueOf(date.getDay()), String.valueOf(date.getMonth(0)), String.valueOf(date.getYear()));

        if(isNetworkAvailable()) {
            OfflineDatabaseAsyncTask asyncTask = new OfflineDatabaseAsyncTask(getApplicationContext());
            asyncTask.execute();
        }
        else {
            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            String day2 = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();
            String day[] = databaseHandler.getAllTasks("day").toArray(new String[0]);
            String name[] = databaseHandler.getAllTasks("name").toArray(new String[0]);
            String desc[] = databaseHandler.getAllTasks("description").toArray(new String[0]);
            String time[] = databaseHandler.getAllTasks("timeAdded").toArray(new String[0]);

            for(int i = 0; i < day.length; i++) {
                com.example.planahead.Task task = new com.example.planahead.Task (day[i], name[i], desc[i], time[i]);
                boolean found = false;
                if (!c.tasks.isEmpty()) {
                    for (int j = 0; j < c.tasks.size(); j++) {
                        if (c.tasks.get(j).getName().equals(task.getName())) {
                            found = true;
                        }
                    }
                    if (!found && day2.equals(day[i])) {
                        c.tasks.add(task);
                        c.eORt.add(task);
                    }
                } else if (day2.equals(day[i])) {
                    c.tasks.add(task);
                    c.eORt.add(task);
                }
            }

            String day1[] = databaseHandler.getAllEvents("day").toArray(new String[0]);
            String name1[] = databaseHandler.getAllEvents("name").toArray(new String[0]);
            String desc1[] = databaseHandler.getAllEvents("description").toArray(new String[0]);
            String eventOwner[] = databaseHandler.getAllEvents("eventOwner").toArray(new String[0]);
            String invitedUsers[] = databaseHandler.getAllEvents("invitedUsers").toArray(new String[0]);
            String time1[] = databaseHandler.getAllEvents("time").toArray(new String[0]);
            String minTemp[] = databaseHandler.getAllEvents("minTemp").toArray(new String[0]);
            String maxTemp[] = databaseHandler.getAllEvents("maxTemp").toArray(new String[0]);
            String weatherCondition[] = databaseHandler.getAllEvents("weatherCondition").toArray(new String[0]);
            for(int i = 0; i < day1.length; i++) {
                String[] arr = invitedUsers[i].split(",");
                Event event = new Event (day1[i], name1[i], desc1[i], eventOwner[i], arr, time1[i], Long.valueOf(minTemp[i]), Long.valueOf(maxTemp[i]), weatherCondition[i]);
                boolean found = false;
                if (!c.events.isEmpty()) {
                    for (int j = 0; j < c.events.size(); j++) {
                        if (c.events.get(j).getName().equals(event.getName())) {
                            found = true;
                        }
                    }
                    if (!found && day2.equals(day1[i])) {
                        c.events.add(event);
                        c.eORt.add(event);
                    }
                } else if (day2.equals(day1[i])) {
                    c.events.add(event);
                    c.eORt.add(event);
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = "152101";
            String channelName = "PlanAhead";
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("PlanAhead")
                .addOnCompleteListener(task -> {
                    String msg = "msg";
                    if (!task.isSuccessful()) {
                        msg = "Failed";
                    }
                });

        Intent i = getIntent();
        String id = i.getStringExtra("id");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    addToken(id, token);
                });


        String name = i.getStringExtra("name");
        String email = i.getStringExtra("email");
        User u = new User(id, name, email);

        if (u.getId() != null) {
            addUser(u)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();

                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                                Log.w(TAG, "addUser:onFailure", e);
                            }
                        }
                    });
        }
    }

    private Task<String> addToken(String id, String token) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("token", token);
        return mFunctions
                .getHttpsCallable("addToken")
                .call(data)
                .continueWith(task -> {
                    String result = (String) task.getResult().getData();
                    return result;
                });
    }

    private Task<String> addUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        return mFunctions
                .getHttpsCallable("addUser")
                .call(json)
                .continueWith(task -> {
                    String result = (String) task.getResult().getData();
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
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            i.remove(i.size() - 1);
            bottomNav.getMenu().getItem(i.get(i.size() - 1)).setChecked(true);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Exit PlanAhead?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                super.onBackPressed();
                finishAffinity();
                System.exit(0);
            });

            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}

