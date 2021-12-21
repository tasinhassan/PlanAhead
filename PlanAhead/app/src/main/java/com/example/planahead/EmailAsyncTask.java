package com.example.planahead;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmailAsyncTask extends AsyncTask<String[], Void, String> {

    String[] users;

    public EmailAsyncTask(String[] users) {
        this.users = users;
    }

    @Override
    protected String doInBackground(String[]... Strings) {
        HttpURLConnection urlConnection = null;
        if (users.length != 0) {
            for (int i = 0; i < users.length; i++) {
                try {
                    urlConnection = null;
                    String user = users[i].replace(" ", "");
                    URL url = new URL("http://planahead-weather.herokuapp.com/api/email/" + user + "/invite");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.connect();
                    String response = String.valueOf(urlConnection.getResponseCode());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        }

        return null;
    }
}
