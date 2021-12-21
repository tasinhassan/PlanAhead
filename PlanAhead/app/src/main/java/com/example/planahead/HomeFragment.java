package com.example.planahead;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import es.dmoral.toasty.Toasty;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ThemeManager {

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseFunctions mFunctions;
    static TextView dateToday;
    static TextView today;
    static ImageView todayImage;
    static ListView eventsLV;
    static EventListAdapter eventArrayAdapter;
    static Double temp;
    static String desc;
    public static Double[] tempArr;
    public static String[] arr;
    public static String[] dates;
    public static String[] temps;
    public static String[] descriptions;

    ScrollView s;
    LinearLayout l;
    LinearLayout dialogBG;
    TextView emptyEventItem;
    TextView t;
    TextView title;
    TextView spinnerTitle;
    Button add;
    Button cancel;
    EditText nameET;
    EditText descET;
    EditText inviteET;
    EditText timeET;
    EditText minTemp;
    EditText maxTemp;
    Spinner weatherSpinner;
    FloatingActionMenu floatingMenu;
    FloatingActionButton addTask;
    FloatingActionButton addEvent;
    SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        s = v.findViewById(R.id.backgroundID);
        l = v.findViewById(R.id.weatherContainerID);
        eventsLV = v.findViewById(R.id.eventsListView);
        emptyEventItem = v.findViewById(R.id.empty_event_item);
        eventsLV.setEmptyView(emptyEventItem);
        dateToday = v.findViewById(R.id.dateToday);
        today = v.findViewById(R.id.tempTodayID);
        t = v.findViewById(R.id.taskNameID);
        floatingMenu = v.findViewById(R.id.floatingMenu);
        addTask = v.findViewById(R.id.floatingMenuAddTask);
        addEvent = v.findViewById(R.id.floatingMenuAddEvent);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialogBG = mView.findViewById(R.id.dialogBG);
        title = mView.findViewById(R.id.title);
        todayImage = v.findViewById(R.id.weatherImageTodayID);
        spinnerTitle = mView.findViewById(R.id.spinnerTitle);
        add = mView.findViewById(R.id.create);
        cancel = mView.findViewById(R.id.cancel);
        nameET = mView.findViewById(R.id.nameET);
        descET = mView.findViewById(R.id.descET);
        inviteET = mView.findViewById(R.id.invitedUsersET);
        timeET = mView.findViewById(R.id.timeET);
        minTemp = mView.findViewById(R.id.minTempET);
        maxTemp = mView.findViewById(R.id.maxTempET);

        changeThemeColour(HomeActivity.colour);

        mAuth = FirebaseAuth.getInstance();
        mFunctions = FirebaseFunctions.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("/users/" + mAuth.getUid() + "/events/");

        // FETCH CURRENT WEATHER
        FetchCurrentWeather fetchCurrentWeather = new FetchCurrentWeather(getActivity().getApplicationContext());
        fetchCurrentWeather.execute();

        // FETCH 5 DAY WEATHER
        Fetch5DayWeather fetch5DayWeather = new Fetch5DayWeather(getActivity().getApplicationContext());
        fetch5DayWeather.execute();

        LinearLayout inviteLayout = mView.findViewById(R.id.inviteLayout);
        LinearLayout tempLayout = mView.findViewById(R.id.tempLayout);
        LinearLayout preferredWeather = mView.findViewById(R.id.preferredCondition);
        LinearLayout timeLayout = mView.findViewById(R.id.timeLayout);

        weatherSpinner = mView.findViewById(R.id.spinner);
        weatherSpinner.setSelection(0);

        addTask.setOnClickListener(v1 -> {
            floatingMenu.close(true);
            title.setText("Task For Today");
            inviteLayout.setVisibility(View.GONE);
            tempLayout.setVisibility(View.GONE);
            preferredWeather.setVisibility(View.GONE);
            timeLayout.setVisibility(View.GONE);
            dialog.show();
        });

        addEvent.setOnClickListener(v1 -> {
            floatingMenu.close(true);
            title.setText("Event For Today");
            inviteLayout.setVisibility(View.VISIBLE);
            timeLayout.setVisibility(View.VISIBLE);
            dialog.show();
        });

        add.setOnClickListener(v3 -> {
            MyDate date2 = new MyDate();
            String day2 = date2.getDay() + " " + date2.getMonth(0) + " " + date2.getYear();
            Date date = new Date();
            String stringDate = DateFormat.getTimeInstance().format(date);
            if(title.getText().toString().contains("Task")){
                if(nameET.getText().toString().replace(" ", "").equals("") || TextUtils.isEmpty(nameET.getText().toString())) {
                    Toasty.warning(getActivity(), "Task Name Cannot Be Blank", Toast.LENGTH_SHORT, true).show();
                }
                else {
                    Task task = new Task(day2, nameET.getText().toString(), descET.getText().toString(), stringDate);
                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
                    if(!isNetworkAvailable()) {
                        databaseHandler.insertTaskToBeAdded(day2, nameET.getText().toString(),descET.getText().toString(), stringDate);
                    }
                    databaseHandler.insertTask(day2, nameET.getText().toString(),descET.getText().toString(), stringDate);
                    HomeActivity.c.tasks.add(task);
                    HomeActivity.c.eORt.add(task);
                    eventArrayAdapter.notifyDataSetChanged();
                    if(isNetworkAvailable()) {
                        addTask(task)
                                .addOnCompleteListener(ta -> {
                                    if (!ta.isSuccessful()) {
                                        Exception e = ta.getException();

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
                    else { max = Long.valueOf(maxTemp.getText().toString()); }
                    String weatherCondition = weatherSpinner.getSelectedItem().toString();
                    if(users[0].equals("")) { users = new String[0]; }
                    Event event = new Event(day2, nameET.getText().toString(), descET.getText().toString(), eventOwner, users, timeET.getText().toString(), min, max, weatherCondition);
                    DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
                    if(!isNetworkAvailable()) {
                        databaseHandler.insertEventToBeAdded(day2, nameET.getText().toString(), descET.getText().toString(), eventOwner, users, timeET.getText().toString(), min, max, weatherCondition);
                    }
                    databaseHandler.insertEvent(day2, nameET.getText().toString(), descET.getText().toString(), eventOwner, users, timeET.getText().toString(), min, max, weatherCondition);
                    HomeActivity.c.events.add(event);
                    HomeActivity.c.eORt.add(event);
                    eventArrayAdapter.notifyDataSetChanged();
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

        MyDate date = new MyDate();
        dateToday.setText(date.getDay() + " " + date.getMonth(0) + " " + date.getYear());

        eventArrayAdapter = new EventListAdapter(getActivity(), HomeActivity.c.eORt);
        eventsLV.setAdapter(eventArrayAdapter);

        eventsLV.setOnItemLongClickListener((arg0, v12, index, arg3) -> {
            ImageButton delete = v12.findViewById(R.id.trashButton);
            if(delete.getVisibility() == View.VISIBLE) {
                delete.setVisibility(View.GONE);
            }
            else {
                delete.setVisibility(View.VISIBLE);
            }
            return true;
        });

        eventsLV.setOnItemClickListener((parent, view, position, id) -> {
            if(HomeActivity.c.eORt.get(position).getClass() == Event.class) {
                Event obj = (Event) HomeActivity.c.eORt.get(position);
                Intent intent3 = new Intent(getActivity(), EventDetails.class);
                intent3.putExtra("calledFrom", "HomeFragment");
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
            else if(HomeActivity.c.eORt.get(position).getClass() == Task.class) {
                Task obj = (Task) HomeActivity.c.eORt.get(position);
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("calledFrom", String.valueOf(getActivity()));
                intent.putExtra("position", position);
                intent.putExtra("task", obj);
                startActivity(intent);
            }
        });

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

    private com.google.android.gms.tasks.Task<String> removeTask(Task task) {
        String id = mAuth.getUid();
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
    public void changeThemeColour(Colour colour)
    {
        if(colour.type != 0 || colour.type != 1) {
            s.setBackground(getContext().getResources().getDrawable(colour.background));
            dialogBG.setBackground(getContext().getResources().getDrawable(colour.background));
        }
        else {
            s.setBackgroundColor(getContext().getResources().getColor(colour.background));
            dialogBG.setBackgroundColor(getContext().getResources().getColor(colour.background));
        }
        dateToday.setTextColor(getContext().getResources().getColor(colour.title));
        today.setTextColor(getContext().getResources().getColor(colour.title));
        emptyEventItem.setTextColor(getContext().getResources().getColor(colour.title));


        title.setTextColor(getContext().getResources().getColor(colour.title));
        spinnerTitle.setTextColor(getContext().getResources().getColor(colour.title));
        add.setBackgroundColor(getContext().getResources().getColor(colour.buttons));
        cancel.setBackgroundColor(getContext().getResources().getColor(colour.buttons));
        nameET.setTextColor(getContext().getResources().getColor(colour.title));
        nameET.setHintTextColor(getContext().getResources().getColor(colour.text));
        descET.setTextColor(getContext().getResources().getColor(colour.title));
        descET.setHintTextColor(getContext().getResources().getColor(colour.text));
        inviteET.setTextColor(getContext().getResources().getColor(colour.title));
        inviteET.setHintTextColor(getContext().getResources().getColor(colour.text));
        timeET.setHintTextColor(getContext().getResources().getColor(colour.text));
        timeET.setTextColor(getContext().getResources().getColor(colour.title));
        minTemp.setTextColor(getContext().getResources().getColor(colour.title));
        minTemp.setHintTextColor(getContext().getResources().getColor(colour.text));
        maxTemp.setTextColor(getContext().getResources().getColor(colour.title));
        maxTemp.setHintTextColor(getContext().getResources().getColor(colour.text));
    }
}

