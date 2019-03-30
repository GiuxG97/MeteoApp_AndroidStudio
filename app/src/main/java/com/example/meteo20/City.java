package com.example.meteo20;

import org.json.JSONException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class City {
    private UUID Id;
    private String mCity;
    private Date mDateOfAdding;
    private double mLat;
    private double mLon;
    private String mNation;
    private String mDescription;
    private double mActualTemperature;
    private double mMaxTemperature;
    private double mMinTemperature;
    private double temperatureNotify = 0;
    private String mIcon;

    public City() {
        Id = UUID.randomUUID();
        mDateOfAdding = new Date();
    }

    public City(String mCity) {
        this.mCity = mCity;
        mDateOfAdding = new Date();
        this.mNation = "";
    }

    public City(String mCity, String mNation, String mDescription, double mActualTemperature, double mMaxTemperature, double mMinTemperature, String mIcon) {
        this.Id = UUID.randomUUID();
        this.mCity = mCity;
        this.mNation = mNation;
        this.mDescription = mDescription;
        this.mActualTemperature = mActualTemperature;
        this.mMaxTemperature = mMaxTemperature;
        this.mMinTemperature = mMinTemperature;
        this.mIcon = mIcon;
        this.mDateOfAdding = new Date();
    }

    public City(String mCity, String mNation, String mDescription, double lat, double lon, double mActualTemperature, double mMaxTemperature, double mMinTemperature, String mIcon) {
        this.Id = UUID.randomUUID();
        this.mCity = mCity;
        this.mNation = mNation;
        this.mDescription = mDescription;
        this.mLat = lat;
        this.mLon = lon;
        this.mActualTemperature = mActualTemperature;
        this.mMaxTemperature = mMaxTemperature;
        this.mMinTemperature = mMinTemperature;
        this.mIcon = mIcon;
        this.mDateOfAdding = new Date();
    }

    public City(String mCity, String mNation, double lat, double lon, double mActualTemperature, double tempNotify) {
        this.Id = UUID.randomUUID();
        this.mCity = mCity;
        this.mNation = mNation;
        this.mLat = lat;
        this.mLon = lon;
        this.mActualTemperature = mActualTemperature;
        this.temperatureNotify = tempNotify;
        this.mDateOfAdding = new Date();
    }


    public void refresh() throws IOException, JSONException {
        City appoggio = new RequestWeather("").call_me_lat_lon(this.getmLat(), this.getmLon());
        this.copy(appoggio);
    }

    private void copy(City appoggio) {
        this.Id = appoggio.getId();
        this.mCity = appoggio.mCity;
        this.mDateOfAdding = appoggio.mDateOfAdding;
        this.mLat = appoggio.getmLat();
        this.mLon = appoggio.getmLon();
        this.mNation = appoggio.getmNation();
        this.mDescription = appoggio.mDescription;
        this.mActualTemperature = appoggio.getmActualTemperature();
        this.mMaxTemperature = appoggio.getmMaxTemperature();
        this.mMinTemperature = appoggio.getmMinTemperature();
        this.mIcon = appoggio.getmIcon();
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getTitle() {
        return mCity;
    }

    public void setTitle(String title) {
        mCity = title;
    }

    public Date getDate() {
        return mDateOfAdding;
    }

    public void setDate(Date date) {
        mDateOfAdding = date;
    }

    public String getmCity() {
        return mCity;
    }

    public Date getmDateOfAdding() {
        return mDateOfAdding;
    }

    public String getmNation() {
        return mNation;
    }

    public String getmDescription() {
        return mDescription;
    }

    public double getmActualTemperature() {
        return mActualTemperature;
    }

    public double getmMaxTemperature() {
        return mMaxTemperature;
    }

    public double getmMinTemperature() {
        return mMinTemperature;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public void setmDateOfAdding(Date mDateOfAdding) {
        this.mDateOfAdding = mDateOfAdding;
    }

    public void setmNation(String mNation) {
        this.mNation = mNation;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmActualTemperature(double mActualTemperature) {
        this.mActualTemperature = mActualTemperature;
    }

    public void setmMaxTemperature(double mMaxTemperature) {
        this.mMaxTemperature = mMaxTemperature;
    }

    public void setmMinTemperature(double mMinTemperature) {
        this.mMinTemperature = mMinTemperature;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public void setmLon(double mLon) {
        this.mLon = mLon;
    }

    public double getmLat() {
        return mLat;
    }

    public double getmLon() {
        return mLon;
    }

    public double getTemperatureNotify() {
        return temperatureNotify;
    }

    public void setTemperatureNotify(double temperatureNotify) {
        this.temperatureNotify = temperatureNotify;
    }

    @Override
    public String toString() {
        return "City{" +
                "Id=" + Id +
                ", mCity='" + mCity + '\'' +
                ", mDateOfAdding=" + mDateOfAdding +
                ", mLat=" + mLat +
                ", mLon=" + mLon +
                ", mNation='" + mNation + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mActualTemperature=" + mActualTemperature +
                ", mMaxTemperature=" + mMaxTemperature +
                ", mMinTemperature=" + mMinTemperature +
                ", mIcon='" + mIcon + '\'' +
                '}';
    }
}