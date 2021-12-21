package com.example.planahead;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EventListAdapter extends ArrayAdapter<Object> {

    private  Activity context;
    private ArrayList<Object> eORt;

    EventListAdapter(Activity context, ArrayList<Object> eORt) {
        super(context, R.layout.event_item, eORt);
        this.context = context;
        this.eORt = eORt;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.event_item, null, true);
        if(eORt != null) {
            LinearLayout layout = rowView.findViewById(R.id.parent);
            TextView name = rowView.findViewById(R.id.eventName);
            TextView type = rowView.findViewById(R.id.type);
            ImageButton delete = rowView.findViewById(R.id.trashButton);
            if (eORt.get(position).getClass() == Event.class) {
                Event obj = (Event) eORt.get(position);
                name.setText(obj.getName());
                type.setText("E");
            }
            else if (eORt.get(position).getClass() == com.example.planahead.Task.class) {
                com.example.planahead.Task obj = (com.example.planahead.Task) eORt.get(position);
                name.setText(obj.getName());
                type.setText("T");
            }
            delete.setOnClickListener(v -> {
                DatabaseHandler databaseHandler = new DatabaseHandler(context);
                if(eORt.get(position).getClass() == Event.class) {
                    Event obj = (Event) eORt.get(position);
                    removeEvent(obj);
                    if(!isNetworkAvailable()) {
                        databaseHandler.insertEventToBeRemoved(obj.getDay(), obj.getName(),obj.getDescription(), obj.getEventOwner(), obj.getInvitedUsers(), obj.getTime(), obj.getMinTemp(), obj.getMaxTemp(), obj.getWeatherCondition());
                    }
                    databaseHandler.deleteEvent(obj.getName());
                    MyDate date = new MyDate();
                    String today = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();
                    if(obj.getDay().equals(today)) {
                        if(DayFragment.c != null) {
                            DayFragment.c.eORt.remove(position);
                            DayFragment.eventArrayAdapter.notifyDataSetChanged();
                        }
                        HomeActivity.c.eORt.remove(position);
                        HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                    }
                    else {
                        DayFragment.c.eORt.remove(position);
                        DayFragment.eventArrayAdapter.notifyDataSetChanged();
                    }
                }
                else if (eORt.get(position).getClass() == com.example.planahead.Task.class) {
                    com.example.planahead.Task obj = (com.example.planahead.Task) eORt.get(position);
                    removeTask(obj);
                    if(!isNetworkAvailable()) {
                        databaseHandler.insertTaskToBeRemoved(obj.getDay(), obj.getName(),obj.getDescription(), obj.getTime());
                    }
                    databaseHandler.deleteTask(obj.getName());
                    MyDate date = new MyDate();
                    String today = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();
                    if(obj.getDay().equals(today)) {
                        if(DayFragment.c != null) {
                            DayFragment.c.eORt.remove(position);
                            DayFragment.eventArrayAdapter.notifyDataSetChanged();
                        }
                        HomeActivity.c.eORt.remove(position);
                        HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                    }
                    else {
                        DayFragment.c.eORt.remove(position);
                        DayFragment.eventArrayAdapter.notifyDataSetChanged();
                    }
                }
            });

            DatabaseHandler databaseHandler1 = new DatabaseHandler(context);
            String[] theme = databaseHandler1.getTheme("theme").toArray(new String[0]);
            if(theme.length != 0) {
                if(theme[0].equals("Light")) {
                    layout.setBackground(context.getResources().getDrawable(R.drawable.item_border));
                    name.setTextColor(context.getResources().getColor(R.color.black));
                    type.setTextColor(context.getResources().getColor(R.color.black));
                }
                else if (theme[0].equals("Dark")) {
                    layout.setBackground(context.getResources().getDrawable(R.drawable.item_border_green));
                    name.setTextColor(context.getResources().getColor(R.color.white));
                    type.setTextColor(context.getResources().getColor(R.color.white));
                }
                else if (theme[0].equals("Nightshade")) {
                    layout.setBackground(context.getResources().getDrawable(R.drawable.item_border_white));
                    name.setTextColor(context.getResources().getColor(R.color.white));
                    type.setTextColor(context.getResources().getColor(R.color.white));
                }
            }
        }

        return rowView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private com.google.android.gms.tasks.Task<String> removeTask(com.example.planahead.Task task) {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();
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

    private Task<String> removeEvent(Event event) {
        FirebaseFunctions mFunctions;
        mFunctions = FirebaseFunctions.getInstance();
        String id = HomeActivity.mAuth.getUid();
        Gson gson = new Gson();
        String json = gson.toJson(event);

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
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
}
