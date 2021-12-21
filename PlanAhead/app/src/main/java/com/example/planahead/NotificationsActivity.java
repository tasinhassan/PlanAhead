package com.example.planahead;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity implements ThemeManager {

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    ConstraintLayout parentLayout;
    TextView title;
    ListView notifications;
    static ArrayList<String> arrayList;
    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ArrayList<NotificationClass> notificationsArrayList = new ArrayList<>();

        arrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + mAuth.getUid() + "/notifications/");

        parentLayout = findViewById(R.id.parentLayout);
        title = findViewById(R.id.title);
        notifications = findViewById(R.id.notificationsListView);
        notifications.setEmptyView(findViewById(R.id.empty_notification_item));

        DatabaseHandler databaseHandler1 = new DatabaseHandler(this);
        String[] theme = databaseHandler1.getTheme("theme").toArray(new String[0]);
        if(theme.length != 0) {
            if (theme[0].equals("Light")) {
                arrayAdapter = new ArrayAdapter<>(this, R.layout.notification_item, arrayList);
            } else {
                arrayAdapter = new ArrayAdapter<>(this, R.layout.notification_item_white, arrayList);
            }
        }

        changeThemeColour(HomeActivity.colour);

        notifications.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String day = (String) messageSnapshot.child("day").getValue();
                    String name = (String) messageSnapshot.child("name").getValue();
                    String desc = (String) messageSnapshot.child("desc").getValue();
                    String eventOwnerEmail = (String) messageSnapshot.child("eventOwnerEmail").getValue();
                    String eventOwnerName = (String) messageSnapshot.child("eventOwnerName").getValue();
                    int i = 0;
                    String[] invitedUsers = new String[100];
                    for (DataSnapshot ds : messageSnapshot.child("invitedUsers").getChildren()) {
                        invitedUsers[i] = (String) ds.getValue();
                        i++;
                    }
                    String time = (String) messageSnapshot.child("time").getValue();
                    Long minTemp = (Long) messageSnapshot.child("minTemp").getValue();
                    Long maxTemp = (Long) messageSnapshot.child("maxTemp").getValue();
                    String weatherCondition = (String) messageSnapshot.child("weatherCondition").getValue();
                    NotificationClass notification = new NotificationClass(day, name, desc, eventOwnerEmail, eventOwnerName, invitedUsers, time, minTemp, maxTemp, weatherCondition);
                    boolean found = false;
                    if (!notificationsArrayList.isEmpty()) {
                        for (int j = 0; j < notificationsArrayList.size(); j++) {
                            if (notificationsArrayList.get(j).getName().equals(notification.getName())) {
                                found = true;
                            }
                        }
                        if (!found) {
                            arrayList.add(notification.getDay() + " - " + notification.getName());
                            notificationsArrayList.add(notification);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        arrayList.add(notification.getDay() + " - " + notification.getName());
                        notificationsArrayList.add(notification);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                arrayList.remove(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        notifications.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(NotificationsActivity.this, NotificationEvent.class);
            i.putExtra("position", position);
            i.putExtra("day", notificationsArrayList.get(position).getDay());
            i.putExtra("name", notificationsArrayList.get(position).getName());
            i.putExtra("desc", notificationsArrayList.get(position).getDesc());
            i.putExtra("eventOwner", notificationsArrayList.get(position).getEventOwner());
            i.putExtra("eventOwnerName", notificationsArrayList.get(position).getEventOwnerName());
            i.putExtra("invitedUsers", notificationsArrayList.get(position).getInvitedUsers());
            i.putExtra("time", notificationsArrayList.get(position).getTime());
            i.putExtra("minTemp", notificationsArrayList.get(position).getMinTemp());
            i.putExtra("maxTemp", notificationsArrayList.get(position).getMaxTemp());
            i.putExtra("weatherCondition", notificationsArrayList.get(position).getWeatherCondition());
            startActivity(i);
        });
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

        title.setTextColor(this.getResources().getColor(colour.title));
    }
}
