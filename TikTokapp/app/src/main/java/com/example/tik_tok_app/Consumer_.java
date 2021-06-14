package com.example.tik_tok_app;

import android.os.AsyncTask;

import classes_needed.Consumer;
import classes_needed.Publisher;

public class Consumer_ extends AsyncTask<Consumer,Consumer,Consumer> {

    @Override
    protected Consumer doInBackground(Consumer...port) {

        port[0].start();
        return null;
    }

}
