package com.example.meteo20;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

//todo facicio implementare questo  metodo all'attività cje fas vedere i dati.. vuoldire cjhe sono arrivati!!!
interface OnTaskCompleted {
    void onTaskCompleted(City items);
}

public class MeteoRequestTask extends AsyncTask<Double, Double, City> {
    private OnTaskCompleted listener;

    public MeteoRequestTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    private City city;
    @Override
    protected City doInBackground(Double... doubles) {
        try {
            city = RequestWeather.call_me_lat_lon(doubles[0],doubles[1]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }


    @Override
    protected void onPostExecute(City items) {
        listener.onTaskCompleted(items);
    }

}

