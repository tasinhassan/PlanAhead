package com.example.planahead;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    DatabaseHandler(Context paramContext) {
        super(paramContext, "Database", null, 1);
    }

    List<String> getTheme(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();

        try
        {
            getTheTheme(sQLiteDatabase, arrayList, paramString);
        }catch (Exception e)
        {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS themeColour");
            sQLiteDatabase.execSQL("CREATE TABLE themeColour (theme TEXT)");
            getTheTheme(sQLiteDatabase, arrayList, paramString);
            e.printStackTrace();
        }
        finally {
            sQLiteDatabase.close();
        }

        return arrayList;
    }

    public void getTheTheme(SQLiteDatabase sQLiteDatabase, ArrayList<String> arrayList, String paramString)
    {
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM themeColour", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
    }


    List<String> getAllEvents(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM events", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    List<String> getAllTasks(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM tasks", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    List<String> getAllTasksToBeAdded(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM addedTasks", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    List<String> getAllEventsToBeAdded(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM addedEvents", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    List<String> getAllTasksToBeRemoved(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM removedTasks", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    List<String> getAllEventsToBeRemoved(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM removedEvents", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    void insertTheme(String theme) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("theme", theme);
        sqLiteDatabase.insert("themeColour", null, contentValues);
        sqLiteDatabase.close();
    }

    void insertEvent(String day, String name, String description, String eventOwner, String[] invitedUsers, String time, Long minTemp, Long maxTemp, String weatherCondition) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("eventOwner", eventOwner);
        String str = "";
        for (int i = 0;i < invitedUsers.length; i++) {
            str = str + invitedUsers[i];
            if(i < invitedUsers.length - 1){
                str = str + ",";
            }
        }
        contentValues.put("invitedUsers", str);
        contentValues.put("time", time);
        contentValues.put("minTemp", minTemp);
        contentValues.put("maxTemp", maxTemp);
        contentValues.put("weatherCondition", weatherCondition);
        sqLiteDatabase.insert("events", null, contentValues);
        sqLiteDatabase.close();
    }

    void insertEventToBeAdded(String day, String name, String description, String eventOwner, String[] invitedUsers, String time, Long minTemp, Long maxTemp, String weatherCondition) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("eventOwner", eventOwner);
        String str = "";
        for (int i = 0;i < invitedUsers.length; i++) {
            str = str + invitedUsers[i];
            if(i < invitedUsers.length - 1){
                str = str + ",";
            }
        }
        contentValues.put("invitedUsers", str);
        contentValues.put("time", time);
        contentValues.put("minTemp", minTemp);
        contentValues.put("maxTemp", maxTemp);
        contentValues.put("weatherCondition", weatherCondition);
        sqLiteDatabase.insert("addedEvents", null, contentValues);
        sqLiteDatabase.close();
    }

    void insertEventToBeRemoved(String day, String name, String description, String eventOwner, String[] invitedUsers, String time, Long minTemp, Long maxTemp, String weatherCondition) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("eventOwner", eventOwner);
        String str = "";
        for (int i = 0;i < invitedUsers.length; i++) {
            str = str + invitedUsers[i];
            if(i < invitedUsers.length - 1){
                str = str + ",";
            }
        }
        contentValues.put("invitedUsers", str);
        contentValues.put("time", time);
        contentValues.put("minTemp", minTemp);
        contentValues.put("maxTemp", maxTemp);
        contentValues.put("weatherCondition", weatherCondition);
        sqLiteDatabase.insert("removedEvents", null, contentValues);
        sqLiteDatabase.close();
    }

    void deleteEventsToBeAdded() {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM " + "addedEvents");
        sQLiteDatabase.close();
    }

    void deleteEventsToBeRemoved() {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM " + "removedEvents");
        sQLiteDatabase.close();
    }

    void deleteEvents(String day) {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        String where = "day = ?";
        sQLiteDatabase.delete("events", where, new String[]{day});
        sQLiteDatabase.close();
    }

    void deleteEvent(String name) {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        String where = "name = ?";
        sQLiteDatabase.delete("events", where, new String[]{name});
        sQLiteDatabase.close();
    }

    void insertTask(String day, String name, String description, String timeAdded) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("timeAdded", timeAdded);
        sqLiteDatabase.insert("tasks", null, contentValues);
        sqLiteDatabase.close();
    }

    void insertTaskToBeAdded(String day, String name, String description, String timeAdded) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("timeAdded", timeAdded);
        sqLiteDatabase.insert("addedTasks", null, contentValues);
        sqLiteDatabase.close();
    }

    void insertTaskToBeRemoved(String day, String name, String description, String timeAdded) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("name", name);
        contentValues.put("description", description);
        contentValues.put("timeAdded", timeAdded);
        sqLiteDatabase.insert("removedTasks", null, contentValues);
        sqLiteDatabase.close();
    }

    void deleteTheme() {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM " + "themeColour");
        sQLiteDatabase.close();
    }

    void deleteTasksToBeAdded() {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM " + "addedTasks");
        sQLiteDatabase.close();
    }

    void deleteTasksToBeRemoved() {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM " + "removedTasks");
        sQLiteDatabase.close();
    }

    void deleteTasks(String day) {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        String where = "day = ?";
        sQLiteDatabase.delete("tasks", where, new String[]{day});
        sQLiteDatabase.close();
    }

    void deleteTask(String name) {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        String where = "name = ?";
        sQLiteDatabase.delete("tasks", where, new String[]{name});
        sQLiteDatabase.close();
    }

    List<String> getCurrentWeather(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM currentWeather", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    void insertCurrentWeather(Double temp, String description) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("temperature", temp);
        contentValues.put("description", description);
        sqLiteDatabase.insert("currentWeather", null, contentValues);
        sqLiteDatabase.close();
    }

    void deleteCurrentWeather() {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM " + "currentWeather");
        sQLiteDatabase.close();
    }

    List<String> getForecast(String paramString) {
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase sQLiteDatabase = getReadableDatabase();
        Cursor cursor = sQLiteDatabase.rawQuery("SELECT * FROM forecast", null);
        if (cursor.moveToFirst())
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex(paramString)));
            } while (cursor.moveToNext());
        cursor.close();
        sQLiteDatabase.close();
        return arrayList;
    }

    void insertForecast(String day, Double temp, String description) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("temperature", temp);
        contentValues.put("description", description);
        sqLiteDatabase.insert("forecast", null, contentValues);
        sqLiteDatabase.close();
    }

    void deleteForecast() {
        SQLiteDatabase sQLiteDatabase = getWritableDatabase();
        sQLiteDatabase.execSQL("DELETE FROM " + "forecast");
        sQLiteDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE themeColour (theme TEXT)");
        db.execSQL("CREATE TABLE events (day TEXT, name TEXT, description TEXT, eventOwner TEXT, invitedUsers TEXT, time TEXT, minTemp INTEGER, maxTemp INTEGER, weatherCondition TEXT)");
        db.execSQL("CREATE TABLE tasks (day TEXT, name TEXT, description TEXT, timeAdded TEXT)");
        db.execSQL("CREATE TABLE addedTasks (day TEXT, name TEXT, description TEXT, timeAdded TEXT)");
        db.execSQL("CREATE TABLE removedTasks (day TEXT, name TEXT, description TEXT, timeAdded TEXT)");
        db.execSQL("CREATE TABLE addedEvents (day TEXT, name TEXT, description TEXT, eventOwner TEXT, invitedUsers TEXT, time TEXT, minTemp INTEGER, maxTemp INTEGER, weatherCondition TEXT)");
        db.execSQL("CREATE TABLE removedEvents (day TEXT, name TEXT, description TEXT, eventOwner TEXT, invitedUsers TEXT, time TEXT, minTemp INTEGER, maxTemp INTEGER, weatherCondition TEXT)");
        db.execSQL("CREATE TABLE currentWeather (temperature INTEGER, description TEXT)");
        db.execSQL("CREATE TABLE forecast (day TEXT, temperature INTEGER, description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS themeColour");
        db.execSQL("DROP TABLE IF EXISTS events");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS addedTasks");
        db.execSQL("DROP TABLE IF EXISTS removedTasks");
        db.execSQL("DROP TABLE IF EXISTS addedEvents");
        db.execSQL("DROP TABLE IF EXISTS removedEvents");
        db.execSQL("DROP TABLE IF EXISTS currentWeather");
        db.execSQL("DROP TABLE IF EXISTS forecast");
    }
}
