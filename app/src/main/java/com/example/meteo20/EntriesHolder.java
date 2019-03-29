package com.example.meteo20;

import android.content.Context;
import java.util.List;
import java.util.UUID;

public class EntriesHolder {
    private static EntriesHolder sEntriesHolder;
    private List<City> mEntries;
    public static EntriesHolder get(Context context) {
        if (sEntriesHolder == null)
            sEntriesHolder = new EntriesHolder(context);
        return sEntriesHolder;
    }

    private EntriesHolder(Context context) {
           mEntries =  MainActivity.myDB.getAll();
           mEntries.add(0, new City("Localizzazione in corso..."));
    }

    public List<City> getEntries() {
        return mEntries;
    }

    public City getEntry(UUID id) {
        for (City city : mEntries) {
            if (city.getId().equals(id))
                return city;
        }
        return null;
    }
}
