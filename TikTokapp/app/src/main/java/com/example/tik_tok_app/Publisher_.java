package com.example.tik_tok_app;

import android.os.AsyncTask;
import android.util.Log;

import classes_needed.Publisher;

public class Publisher_ extends AsyncTask<Publisher,Publisher,Publisher>{

    public Publisher pp;
    public int port_= 0;

    @Override
    protected Publisher doInBackground(Publisher...port) {

        port[0].start();
        return null;
    }

}
