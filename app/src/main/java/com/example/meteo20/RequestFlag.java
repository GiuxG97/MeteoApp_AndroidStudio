package com.example.meteo20;

import android.graphics.drawable.Drawable;
import java.io.InputStream;
import java.net.URL;


interface OnTaskCompletedFlag {
    void onTaskCompleted(Drawable items);
}
public class RequestFlag {

    public static  Drawable LoadImageFromWebOperations(String name) {
        String url = "https://www.countryflags.io/" +name + "/flat/64.png";
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
