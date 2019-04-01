package com.example.meteo20;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LogService extends IntentService {

    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1); // min. is 1 minute!
    private static final String TAG = "LogService";

    public LogService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LogService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = LogService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn)
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
        try {
            sendNotification();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification() throws IOException, JSONException {
        List<City> cities = MainActivity.myDB.getAll();
        NotificationManager mNotificationManager;
        for (City c : cities){
            if (c.getTemperatureNotify() > 0){
                City city = RequestWeather.call_me_lat_lon(c.getmLat(), c.getmLon());
                Log.i("CITY_RETURNED", "CITY: " + city.toString());
                if (city.getmMaxTemperature() > city.getTemperatureNotify()){
                    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("default", "TEST_CHANNEL", NotificationManager.IMPORTANCE_DEFAULT);
                        channel.setDescription("Test Channel Description");
                        mNotificationManager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                            .setSmallIcon(android.R.drawable.ic_menu_report_image)
                            .setContentTitle("City: " + city.getmCity())
                            .setContentText("Max temperature: " + city.getmMaxTemperature())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    mNotificationManager.notify(0, mBuilder.build());
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        Log.i("PROVA SERVICE", "Distruzione Service");
    }

}