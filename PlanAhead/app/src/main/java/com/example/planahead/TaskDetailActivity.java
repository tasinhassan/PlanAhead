package com.example.planahead;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TaskDetailActivity extends AppCompatActivity implements Serializable, ThemeManager {

    FirebaseFunctions mFunctions;
    FirebaseAuth mAuth;
    LinearLayout parentLayout;
    TextView taskName;
    TextView descTV;
    TextView taskDesc;
    TextView timeAddedTV;
    TextView taskTime;
    EditText updatedDesc;
    Button editButton;
    Button backButton;
    Button applyButton;
    Button removeButton;
    Button cancelButton;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        mFunctions = FirebaseFunctions.getInstance();
        mAuth = FirebaseAuth.getInstance();

        MyDate date = new MyDate();
        String today = date.getDay() + " " + date.getMonth(0) + " " + date.getYear();

        parentLayout = findViewById(R.id.parentLayout);
        taskName = findViewById(R.id.taskName);

        descTV = findViewById(R.id.descTV);
        taskDesc = findViewById(R.id.desc);
        timeAddedTV = findViewById(R.id.timeAddedTV);
        taskTime = findViewById(R.id.timeAdded);
        updatedDesc = findViewById(R.id.updatedDescET);
        editButton = findViewById(R.id.editButtonID);
        backButton = findViewById(R.id.backButtonID);
        applyButton = findViewById(R.id.applyChangesButtonID);
        removeButton = findViewById(R.id.removeButtonID);
        cancelButton = findViewById(R.id.cancelEditButtonID);

        Intent intent = getIntent();
        String calledFrom = intent.getStringExtra("calledFrom");
        int position = intent.getIntExtra("position", 0);
        task = (Task) intent.getSerializableExtra("task");
        taskName.setText(task.getName());

        if (!task.getDescription().equals("")) {
            taskDesc.setText(task.getDescription());
        }
        else { taskDesc.setText("No Description Given"); }

        taskTime.setText(task.getTime());

        changeThemeColour(HomeActivity.colour);

        editButton.setOnClickListener(v1 -> {
            if(task.getDescription().equals("")) { updatedDesc.setHint("Not Given"); }
            else { updatedDesc.setHint(task.getDescription()); }
            updatedDesc.setVisibility(View.VISIBLE);
            applyButton.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            taskDesc.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
        });

        cancelButton.setOnClickListener(v2 -> {
            updatedDesc.setVisibility(View.GONE);
            applyButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            taskDesc.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        });

        applyButton.setOnClickListener(v3 -> {
            Task oldTask = new Task(task.getDay(), task.getName(), task.getDescription(),task.getTime());
            String newDescription;
            if(updatedDesc.getText().toString().replace(" ", "").equals("")) {
                newDescription = task.getDescription();
            }
            else { newDescription = updatedDesc.getText().toString(); }
            Task newTask = new Task(task.getDay(), task.getName(), newDescription, task.getTime());
            updateTask(newTask)
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
            if(newTask.getDay().equals(today)) {
                HomeActivity.c.eORt.remove(position);
                HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                HomeActivity.c.eORt.add(newTask);
                HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                if(!calledFrom.contains("com.example.planahead.HomeActivity")) {
                    DayFragment.c.eORt.remove(position);
                    DayFragment.eventArrayAdapter.notifyDataSetChanged();
                }
            }
            else {
                DayFragment.c.eORt.remove(position);
                DayFragment.eventArrayAdapter.notifyDataSetChanged();
                DayFragment.c.eORt.add(newTask);
                DayFragment.eventArrayAdapter.notifyDataSetChanged();
            }

            finish();
        });

        removeButton.setOnClickListener(v4 -> {
            DatabaseHandler databaseHandler = new DatabaseHandler(this);

            if(!calledFrom.contains("com.example.planahead.HomeActivity")) {
                Task obj = (Task) DayFragment.c.eORt.get(position);
                databaseHandler.deleteTask(obj.getName());
                removeTask(obj);
                if(task.getDay().equals(today)) {
                    HomeActivity.c.eORt.remove(position);
                    HomeFragment.eventArrayAdapter.notifyDataSetChanged();
                    DayFragment.c.eORt.remove(position);
                    DayFragment.eventArrayAdapter.notifyDataSetChanged();
                }
                else {
                    DayFragment.c.eORt.remove(position);
                    DayFragment.eventArrayAdapter.notifyDataSetChanged();
                }
            }
            else {
                Task obj = (Task) HomeActivity.c.eORt.get(position);
                databaseHandler.deleteTask(obj.getName());
                removeTask(obj);
                HomeActivity.c.eORt.remove(position);
                HomeFragment.eventArrayAdapter.notifyDataSetChanged();
            }

            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }

    private com.google.android.gms.tasks.Task<Object> updateTask(Task task) {
        String id = mAuth.getUid();
        Gson gson = new Gson();
        String json = gson.toJson(task);

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("day", task.getDay());
        data.put("task", json);

        return mFunctions
                .getHttpsCallable("updateTask")
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

    @Override
    public void changeThemeColour(Colour colour)
    {
        if(colour.type != 0 || colour.type != 1) {
            parentLayout.setBackground(getResources().getDrawable(colour.background));
        }
        else {
            parentLayout.setBackgroundColor(this.getResources().getColor(colour.background));
        }

        taskName.setTextColor(this.getResources().getColor(colour.title));
        descTV.setTextColor(this.getResources().getColor(colour.title));
        taskDesc.setTextColor(this.getResources().getColor(colour.title));
        timeAddedTV.setTextColor(this.getResources().getColor(colour.title));
        taskTime.setTextColor(this.getResources().getColor(colour.title));
        updatedDesc.setTextColor(this.getResources().getColor(colour.title));
        updatedDesc.setHintTextColor(this.getResources().getColor(colour.text));

        editButton.setTextColor(this.getResources().getColor(colour.title));
        backButton.setTextColor(this.getResources().getColor(colour.title));
        applyButton.setTextColor(this.getResources().getColor(colour.title));
        removeButton.setTextColor(this.getResources().getColor(colour.title));
        cancelButton.setTextColor(this.getResources().getColor(colour.title));

        DatabaseHandler databaseHandler1 = new DatabaseHandler(this);
        String[] theme = databaseHandler1.getTheme("theme").toArray(new String[0]);
        if(theme.length != 0) {
            if (theme[0].equals("Light")) {
                editButton.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                applyButton.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                backButton.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
                cancelButton.setBackground(this.getResources().getDrawable(R.drawable.settings_button));
            }
            else if (theme[0].equals("Dark")) {
                editButton.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                applyButton.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                backButton.setBackground(this.getResources().getDrawable(R.drawable.button_green));
                cancelButton.setBackground(this.getResources().getDrawable(R.drawable.button_green));
            }
            else {
                editButton.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                applyButton.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                backButton.setBackground(this.getResources().getDrawable(R.drawable.button_white));
                cancelButton.setBackground(this.getResources().getDrawable(R.drawable.button_white));
            }
        }
    }
}
