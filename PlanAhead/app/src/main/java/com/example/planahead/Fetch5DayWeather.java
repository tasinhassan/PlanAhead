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
import java.util.ArrayList;

public class Fetch5DayWeather extends AsyncTask<Void,Void,Void> {

    private Context context;

    public Fetch5DayWeather(Context applicationContext) {
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
                MyWeather.Forecast = new ArrayList<>();
                JSONObject obj = getJSONObjectFromURL("http://planahead-weather.herokuapp.com/api/weather/5days");
                JSONArray arr = obj.getJSONArray("DailyForecasts");
                String blurb = obj.getJSONObject("Headline").getString("Text");
                MyWeather.DailyBlurb = blurb;

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject day = arr.getJSONObject(i);
                    JSONObject temperature = day.getJSONObject("Temperature");
                    JSONObject minimum = temperature.getJSONObject("Minimum");
                    JSONObject maximum = temperature.getJSONObject("Maximum");
                    JSONObject forecastday = day.getJSONObject("Day");
                    JSONObject forecastnight = day.getJSONObject("Night");

                    // Constructor Values
                    Double min = minimum.getDouble("Value");
                    Double max = maximum.getDouble("Value");
                    Boolean dayHasPrecipitation = forecastday.getBoolean("HasPrecipitation");
                    Boolean nightHasPrecipitation = forecastnight.getBoolean("HasPrecipitation");
                    String dayPrecipitationDescription = "No Precipitation";
                    String nightPrecipitationDescription = "No Precipitation";
                    String dayPhrase = forecastday.getString("IconPhrase");
                    String nightPhrase = forecastnight.getString("IconPhrase");

                    if (dayHasPrecipitation) {
                        dayPrecipitationDescription = forecastday.getString("PrecipitationIntensity") + " " + forecastday.getString("PrecipitationType");
                    }
                    if (nightHasPrecipitation) {
                        nightPrecipitationDescription = forecastnight.getString("PrecipitationIntensity") + " " + forecastnight.getString("PrecipitationType");
                    }
                    MyWeather.Forecast.add(new ForecastDay(max, min, dayHasPrecipitation, nightHasPrecipitation, dayPrecipitationDescription, nightPrecipitationDescription, dayPhrase, nightPhrase));
                }
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
        MyDate date = new MyDate();
        ArrayList<String> days = new ArrayList<>();
        days.add(date.day+1+"");
        days.add(date.day+2+"");
        days.add(date.day+3+"");
        days.add(date.day+4+"");
        days.add(date.day+5+"");
        if(isNetworkAvailable()) {
            databaseHandler.deleteForecast();
            Double[] temps = getTempFiveDay();
            String[] descriptions = getFiveDayDescription();
            for(int i = 0; i < temps.length; i++) {
                databaseHandler.insertForecast(days.get(i), temps[i], descriptions[i]);
            }
        }
        String[] temps = databaseHandler.getForecast("temperature").toArray(new String[0]);
        String[] day = databaseHandler.getForecast("day").toArray(new String[0]);
        Double[] parsed = new Double[temps.length];
        for (int i = 0; i < temps.length; i++) parsed[i] = Double.valueOf(temps[i]);
        HomeFragment.tempArr = parsed;
        HomeFragment.arr = databaseHandler.getForecast("description").toArray(new String[0]);
        HomeFragment.dates = new String[]{date.getMonth(0) + " " + day[0], date.getMonth(0) + " " + day[1], date.getMonth(0) + " " + day[2], date.getMonth(0) + " " + day[3], date.getMonth(0) + " " + day[4]};
        HomeFragment.temps = new String[]{HomeFragment.tempArr[0] + "°C", HomeFragment.tempArr[1] + "°C", HomeFragment.tempArr[2] + "°C", HomeFragment.tempArr[3] + "°C", HomeFragment.tempArr[4] + "°C"};
        HomeFragment.descriptions = new String[]{HomeFragment.arr[0], HomeFragment.arr[1], HomeFragment.arr[2], HomeFragment.arr[3], HomeFragment.arr[4]};
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

    Double[] getTempFiveDay()
    {
        Double[] temp = new Double[MyWeather.Forecast.size()];
        for(int i = 0; i < temp.length;i++)
        {
            temp[i] = MyWeather.Forecast.get(i).getMaximum();
        }
        return temp;
    }

    String[] getFiveDayDescription()
    {
        String[] temp = new String[MyWeather.Forecast.size()];
        for(int i = 0; i < temp.length;i++)
        {
            temp[i] = MyWeather.Forecast.get(i).getDayPhrase();
        }
        return temp;
    }
}
