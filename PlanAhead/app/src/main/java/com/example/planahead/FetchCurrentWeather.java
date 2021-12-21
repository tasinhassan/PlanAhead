package com.example.planahead;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchCurrentWeather extends AsyncTask<Void,Void,Void> {
    private Context context;

    public FetchCurrentWeather(Context applicationContext) {
        context = applicationContext;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(isNetworkAvailable()) {
            try {
                JSONObject obj = getJSONObjectFromURL("http://planahead-weather.herokuapp.com/api/weather/current");
                JSONObject main = obj.getJSONObject("main");
                MyWeather.CurrentTemp = main.getDouble("temp");
                JSONArray jarr = obj.getJSONArray("weather");
                JSONObject job = jarr.getJSONObject(0);
                MyWeather.CurrentDescription = job.getString("description");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        if(isNetworkAvailable()) {
            databaseHandler.deleteCurrentWeather();
            databaseHandler.insertCurrentWeather(MyWeather.CurrentTemp, MyWeather.CurrentDescription);
        }
        String[] temp = databaseHandler.getCurrentWeather("temperature").toArray(new String[0]);
        MyWeather.CurrentTemp = Double.valueOf(temp[0]);
        String[] description = databaseHandler.getCurrentWeather("description").toArray(new String[0]);
        MyWeather.CurrentDescription = description[0];
        HomeFragment.temp = Double.valueOf(temp[0]);
        HomeFragment.desc = description[0];
        String[] strArray = description[0].split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        //HomeFragment.today.setText(temp[0] + "°C\n" + builder.toString());
        String t = String.format("%.1f", Double.valueOf(temp[0]));
        HomeFragment.today.setText(t + "°C");
        if (description[0].equals("clear sky")){
            HomeFragment.todayImage.setImageResource(R.drawable.sunny1);
        }
        else if(description[0].equals("overcast clouds") || description[0].equals("broken clouds")
                || description[0].contains("Dreary") || description[0].contains("Cloudy") || description[0].contains("cloudy")) {
            HomeFragment.todayImage.setImageResource(R.drawable.cloudy2);
        }
        else if (description[0].equals("scattered clouds")) {
            HomeFragment.todayImage.setImageResource(R.drawable.cloudy1);
        }
        else if (description[0].equals("few clouds")) {
            HomeFragment.todayImage.setImageResource(R.drawable.cloudy);
        }
        else if (description[0].contains("Snow") || description[0].contains("snow")
                || description[0].contains("Sleet") || description[0].contains("sleet")) {
            HomeFragment.todayImage.setImageResource(R.drawable.snowy);
        }
        else if (description[0].contains("Drizzle") || description[0].contains("drizzle")){
            HomeFragment.todayImage.setImageResource(R.drawable.drizzle);
        }
        else if (description[0].equals("light rain") || description[0].equals("moderate rain")
                || description[0].equals("heavy intensity rain") || description[0].equals("very heavy rain")
                || description[0].equals("extreme rain")) {
            HomeFragment.todayImage.setImageResource(R.drawable.rain);
        }
        else if (description[0].equals("light intensity shower rain") || description[0].equals("shower rain")
                || description[0].equals("heavy intensity shower rain") || description[0].equals("ragged shower rain")) {
            HomeFragment.todayImage.setImageResource(R.drawable.drizzle);
        }
        else if (description[0].equals("freezing rain")) {
            HomeFragment.todayImage.setImageResource(R.drawable.freezingrain);
        }
        else if (description[0].contains("thunderstorm") || description[0].contains("Thunderstorm")) {
            HomeFragment.todayImage.setImageResource(R.drawable.thunderstorm);
        }
        else if (description[0].equals("fog") || description[0].equals("mist")
                || description[0].equals("Smoke") || description[0].equals("Haze")
                || description[0].equals("sand/ dust whirls") || description[0].equals("sand")
                || description[0].equals("dust") || description[0].equals("squalls")
                || description[0].equals("tornado")) {
            HomeFragment.todayImage.setImageResource(R.drawable.fog);
        }
    }

    private static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        String jsonString;

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        jsonString = sb.toString();

        urlConnection.disconnect();

        return new JSONObject(jsonString);
    }
}

