package com.example.meteo20;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;



public class TaskRequestFlag extends AsyncTask<String, Void, Drawable> {
    OnTaskCompletedFlag listener;

    public TaskRequestFlag(OnTaskCompletedFlag listener) {
        this.listener = listener;
    }

    @Override
    protected Drawable doInBackground(String... strings) {
        return  RequestFlag.LoadImageFromWebOperations(strings[0]);
    }

    @Override
    protected void onPostExecute(Drawable items) {
        listener.onTaskCompleted(items);
    }
}
