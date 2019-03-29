package com.example.meteo20;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import static com.example.meteo20.RequestWeather.citta;

public class FromNameToLatLon {
    private static String nameCity;
    private static String request;

    public FromNameToLatLon(String nameCity) {
        this.nameCity = nameCity;
        this.request = "https://api.opencagedata.com/geocode/v1/json?q=" + nameCity + "&key=ea341c0e70344a13bc96f6c28727735a";
    }

    public static ArrayList<City> doRequest() throws IOException, JSONException {
        ArrayList<City> c = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL obj = new URL(request);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + request);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        JSONObject myResponse = new JSONObject(response.toString());
        System.out.println("result after Reading JSON Response");
        JSONArray array = new JSONArray(myResponse.getString("results"));
        double lat = 0.0, lon = 0.0;
        for (int i = 0; i < array.length(); i++) {
            JSONObject ob = array.getJSONObject(i);
            JSONObject geometry = ob.getJSONObject("geometry");
            lat = Double.parseDouble(geometry.getString("lat"));
            lon = Double.parseDouble(geometry.getString("lng"));
            String formatted = ob.getString("formatted");
            City appoggio = new City();
            appoggio.setmCity(formatted);
            appoggio.setmLat(lat);
            appoggio.setmLon(lon);
            c.add(appoggio);
        }

        return c;
    }
}
