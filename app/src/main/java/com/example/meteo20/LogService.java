package com.example.meteo20;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

public class LogService extends IntentService {

    public LogService() {
        super("LogService");
    }

    @Override
    protected void onHandleIntent(Intent i) {
        int n = 0;
        while (true) {
            Log.i("PROVA SERVICE", "Evento n." + n++);

            try {
                refresh();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.i("PROVA SERVICE", "Distruzione Service");
    }


    public static void refresh() throws IOException, JSONException {
        int count = 0;
        for (City c : ListFragment.entries
        ) {
            if (count == 0) {

            } else {
                c.refresh();
            }
            count++;
        }
    }

}