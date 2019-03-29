package com.example.meteo20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainActivity extends SingleFragmentActivity implements OnTaskCompleted {
    public static DBAdapter myDB;
    private RequestWeather requests;
    public static City actualCity;

    @Override
    protected Fragment createFragment() {
        askPermission();
        actualCity = new City();
        openDB();
        startLocationListener();
        //  startService();
        return new ListFragment();
    }

    public void askPermission() {
        int app = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            app);
                }
            } else {
            }
        }
    }

    private void startLocationListener() {

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(LocationAccuracy.HIGH)
                .setDistance(0)
                .setInterval(20000);
        SmartLocation.with(MainActivity.this).location().continuous().config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        final MeteoRequestTask f = new MeteoRequestTask(MainActivity.this);
                        f.execute(location.getLatitude(), location.getLongitude());

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_t, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add: {
                onClick_add();
                myDB.stampaTutto();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public static ArrayList<City> arrayMultiple = null;
    public static String c;

    private void onClick_add() {
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Aggiunta di una nuova città ")
                .setMessage("Che città vuoi vedere?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        c = taskEditText.getText().toString();
                        FromNameToLatLon f = new FromNameToLatLon(c);
                        int size = 0;
                        try {
                            arrayMultiple = f.doRequest();
                            size = arrayMultiple.size();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Choose a city");
                        String[] appogioStringa = new String[size];
                        for (int i = 0; i < size; i++) {
                            appogioStringa[i] = arrayMultiple.get(i).getmCity();
                        }
                        builder.setItems(appogioStringa, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requests = new RequestWeather("");
                                try {
                                    City a = requests.call_me_lat_lon(arrayMultiple.get(which).getmLat(), arrayMultiple.get(which).getmLon());
                                    a.setmCity(c);
                                    myDB.insertRowPro(a);
                                    ListFragment.entries.add(a);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        AlertDialog dialog2 = builder.create();
                        dialog2.show();
                        ListFragment.metodoAggiorna();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }


    private void openDB() {
        myDB = new DBAdapter(this);
        myDB.open();
    }


    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            //startLocationListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    // startLocationListener();
                    return;
            }
        }
    }

    public void startService()  //View v
    {
        startService(new Intent(this, LogService.class));
    }

    public void stopService()  //View v
    {
        stopService(new Intent(this, LogService.class));
    }


    @Override
    public void onTaskCompleted(City items) {
        actualCity = items;
        if (ListFragment.entries.size() != 0) {
            ListFragment.entries.remove(0);
            ListFragment.entries.add(0, actualCity);
            myDB.updateRow(0, actualCity);
            ListFragment.metodoAggiorna();
            myDB.stampaTutto();
        } else {
            myDB.insertRowPro(actualCity);
            ListFragment.entries.add(actualCity);
            ListFragment.metodoAggiorna();
            myDB.stampaTutto();
        }
    }
}