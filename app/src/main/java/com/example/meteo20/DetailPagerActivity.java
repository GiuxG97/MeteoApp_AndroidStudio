package com.example.meteo20;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DetailPagerActivity extends AppCompatActivity  {
    //TODO POSSO CAMBIARE  QUESTO ID?
    private static final String EXTRA_ENTRY_ID = "ch.supsi.dti.isin.recyclerviewexample.entry_id";
    private ViewPager mViewPager;
    private List<City> mEntries;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, DetailPagerActivity.class);
        intent.putExtra(EXTRA_ENTRY_ID, crimeId);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuinfo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_info: {
                getInfoAboutTheCity();
                return true;
            }
            case R.id.m_maps: {
                openMaps();
                return true;
            }
            case R.id.m_setTemp: {
                setTemperatureOfNotify();
                return true;
            }
            case R.id.m_setTyeTemp: {
                changeTypeTemp();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void setTemperatureOfNotify() {
        final TextView tMax = findViewById(R.id.typeTemp6);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set the temperature maximum to be notified ("  + tMax.getText().toString() + ")");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                double temp = Double.parseDouble(input.getText().toString());
                if(tMax.getText().toString().equals("°C"))
                    temp +=  273.15;
                //todo: inserire questatemperatura nel  db, in modo  tale che in backgroundfaccio la richiesta e controllo la temperatura.

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for CANCEL button here, or leave in blank
            }
        });
        alert.show();
    }

    private void getInfoAboutTheCity() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Info about " + city.getmCity());
        alertDialog.setMessage("Latitude: " + city.getmLat() + "\n"+ "Longitude: " + city.getmLon() + "\n" + "Nation: " + city.getmNation());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void changeTypeTemp() {
        TextView actual = findViewById(R.id.textView_Actual);
        TextView min = findViewById(R.id.textView_min);
        TextView max = findViewById(R.id.textView_max);
        TextView tActual = findViewById(R.id.typeTemp);
        TextView tMin = findViewById(R.id.typeTemp2);
        TextView tMax = findViewById(R.id.typeTemp6);
        double ac = Double.parseDouble(actual.getText().toString());
        double mi = Double.parseDouble(min.getText().toString());
        double ma = Double.parseDouble(max.getText().toString());
        if (tActual.getText().toString().equals("°K")) {
            tActual.setText("°C");
            tMin.setText("°C");
            tMax.setText("°C");
            actual.setText(fromKtoC(ac));
            min.setText(fromKtoC(mi));
            max.setText(fromKtoC(ma));
        } else {
            tActual.setText("°K");
            tMin.setText("°K");
            tMax.setText("°K");
            actual.setText(formCtoK(ac));
            min.setText(formCtoK(mi));
            max.setText(formCtoK(ma));
        }
    }

    private String formCtoK(double ac) {
        return Double.toString(Math.round((ac + 273.15) * 10) / 10.0);
    }

    private String fromKtoC(double ac) {
        return Double.toString(Math.round((ac - 273.15) * 10) / 10.0);
    }

    private void openMaps() {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", city.getmLat(), city.getmLon());
        Log.e("eo", "Città " + city.getmCity() + " lat" + Double.toString(city.getmLat()) + "lon " + Double.toString(city.getmLon()));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        this.startActivity(intent);
    }

    public static City city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pager);
        UUID entryId = (UUID) getIntent().getSerializableExtra(EXTRA_ENTRY_ID);
        mViewPager = findViewById(R.id.entry_view_pager);
        mEntries = EntriesHolder.get(this).getEntries();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return EntryFragment.newInstance(city.getId());
            }

            @Override
            public int getCount() {
                return mEntries.size();
            }
        });

        for (int i = 0; i < mEntries.size(); i++) {
            if (mEntries.get(i).getId().equals(entryId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
