package com.example.tik_tok_app.Memory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import classes_needed.Consumer;
import classes_needed.Publisher;

public class Pref {

    public static Publisher p = new Publisher();
    public static Consumer c = new Consumer();
    public static String [] Allchanells;
    public static String [] Allhashes;
    /*

    private static final String LIST_KEY = "publisher_save_key";

    public static void write(Context context, Publisher _publisher_){

        Gson gson =new Gson();
        String jsonString = gson.toJson(_publisher_);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, jsonString);
        editor.apply();
    }

    public static Publisher readPubFromPref(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(LIST_KEY, "");

        Gson gson = new Gson();
        Type type = new TypeToken<Publisher>() {}.getType();
        Publisher this_pub = gson.fromJson(jsonString, type);
        Log.i("test","eimai mesa sto read twn cust : "+this_pub);
        //if(jsonString.equals("")) return new ArrayList<Customer>();
        return this_pub;
    }
    */
}