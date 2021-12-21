package com.example.planahead;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeatherListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> forecast;
    private ArrayList<String> desc;

    public WeatherListAdapter(@NonNull Activity context, ArrayList<String> forecast, ArrayList<String> desc) {
        super(context, R.layout.weather_item, forecast);
        this.context = context;
        this.forecast = forecast;
        this.desc = desc;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.weather_item, null, true);

        ImageView weatherImage = rowView.findViewById(R.id.weatherImage);
        TextView text = rowView.findViewById(R.id.temp);

        if(desc.get(position).equals("Mostly cloudy w/ snow") || desc.get(position).equals("Mostly cloudy w/ flurries")
                || desc.get(position).equals("Snow") || desc.get(position).equals("Partly Sunny w/ flurries") || desc.get(position).contains("Flurries") || desc.get(position).contains("flurries")){
            weatherImage.setImageResource(R.drawable.snowy);
        }
        else if (desc.get(position).equals("Rain") || desc.get(position).equals("Showers")
                || desc.get(position).equals("Mostly cloudy w/ showers")){
            weatherImage.setImageResource(R.drawable.rain2);
        }
        else if (desc.get(position).equals("Partly Sunny w/ showers")){
            weatherImage.setImageResource(R.drawable.rain);
        }
        else if (desc.get(position).equals("Mostly cloudy") || desc.get(position).equals("Cloudy")
                || desc.get(position).equals("Dreary") || desc.get(position).equals("Partly cloudy")
                || desc.get(position).equals("Intermittent clouds")){
            weatherImage.setImageResource(R.drawable.cloudy2);
        }
        else if (desc.get(position).equals("Freezing rain") || desc.get(position).equals("Sleet")
                || desc.get(position).equals("Rain and snow") || desc.get(position).equals("Ice")){
            weatherImage.setImageResource(R.drawable.freezingrain);
        }
        else if (desc.get(position).equals("Windy") || desc.get(position).equals("Fog")){
            weatherImage.setImageResource(R.drawable.fog);
        }
        else if (desc.get(position).contains("Sunny") || desc.get(position).contains("sunny")){
            weatherImage.setImageResource(R.drawable.sunny1);
        }
        else if (desc.get(position).contains("Storms") || desc.get(position).contains("storms")){
            weatherImage.setImageResource(R.drawable.thunderstorm);
        }

        text.setText(forecast.get(position));

        return rowView;
    }
}
