package com.example.meteo20;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;


public class EntryFragment extends Fragment implements OnTaskCompleted {
    private static final String ARG_ENTRY_ID = "entry_id";
    public City mCity;
    private TextView mIdTextView;
    private TextView mTitleTextView;
    private TextView mActualTempView;
    private TextView mMaxTempView;
    private TextView mMinTempView;
    private ImageView immagine;

    public static EntryFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ENTRY_ID, crimeId);
        EntryFragment fragment = new EntryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID entryId = (UUID) getArguments().getSerializable(ARG_ENTRY_ID);
        mCity = EntriesHolder.get(getActivity()).getEntry(entryId);
    }

    static String appoggio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry, container, false);
        mIdTextView = v.findViewById(R.id.id_textView);
        mTitleTextView = v.findViewById(R.id.title_textView);
        mActualTempView = v.findViewById(R.id.textView_Actual);
        mMaxTempView = v.findViewById(R.id.textView_max);
        mMinTempView = v.findViewById(R.id.textView_min);
        immagine = v.findViewById(R.id.imageView);
        appoggio = mCity.getmCity();
        MeteoRequestTask f = new MeteoRequestTask(EntryFragment.this);
        f.execute(mCity.getmLat(), mCity.getmLon());

        return v;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            Log.e(" url", url);
            return d;
        } catch (Exception e) {
            Log.e("errore", url);
            return null;
        }
    }

    @Override
    public void onTaskCompleted(City items) {
        mCity = items;
        items.setmCity(appoggio);
        if (items.getmCity().equals(""))
            mIdTextView.setText("Unknow City");
        else
            mIdTextView.setText((items.getmCity().substring(0, 1).toUpperCase() + items.getmCity().substring(1).toLowerCase()));
        mTitleTextView.setText(items.getmDescription());
        mActualTempView.setText(Double.toString(items.getmActualTemperature()));
        mMaxTempView.setText(Double.toString(items.getmMaxTemperature()));
        mMinTempView.setText(Double.toString(items.getmMinTemperature()));

//todo: non si vedono più le icone, forse è un problema  momentaneo loro.. sono semrpe andate!

        immagine.setImageDrawable(LoadImageFromWebOperations("http://openweathermap.org/img/w/" + items.getmIcon() + ".png"));

    }
}


