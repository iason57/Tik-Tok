package com.example.tik_tok_app;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;

import classes_needed.Consumer;
import classes_needed.Publisher;

public class Executor extends AsyncTask<String,String,String> {

    public Publisher p;
    public Consumer c;
    public ArrayList<String> ch = new ArrayList<>();
    public String temp;
    private  ListView viewById;
    private Main_Page main_page;
    private Search search;
    private  ListView hashList;

    public Executor(Publisher pub){
        this.p = pub;
    }

    public Executor(Publisher p, ListView viewById, Main_Page main_page) {
        this.p = p;
        this.viewById = viewById;
        this.main_page = main_page;
    }

    public Executor(Consumer cons){
        this.c = cons;
    }

    public Executor(Consumer c, ListView viewById, Search search) {
        this.c = c;
        this.hashList = viewById;
        this.search = search;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String...dedomena) {
        //Log.i("test","do in back port "+p.port);
        // first pos is the id of the task
        if(dedomena[0].equals("consumer")){
            if(dedomena[1].equals("search") && dedomena.length == 5 ){ //mono gia na bgazw ta hashtags : present list
                try {
                    c.dealWithInterface(c.port+1000,dedomena);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ch = c.hashtags_for_present;
                String temp = "searchhashtaglist,";
                for(int i =0;i<ch.size();i++){
                    if(i!= ch.size()-1){
                        temp = temp + ch.get(i)+",";
                    }
                    else{
                        temp = temp + ch.get(i);
                    }
                }
                Log.i("testsame",temp);
                return temp;
            }
            else if(dedomena[1].equals("choiceofvid")){
                try {
                    c.dealWithInterface(c.port+1000,dedomena);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    c.dealWithInterface(c.port+1000,dedomena);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            if(dedomena[0].equals("push")){
                p.dealWithInterface(p.port+3000,dedomena);
            }else if(dedomena[0].equals("subscribe")){
                p.dealWithInterface(p.port+3000,dedomena);
            }else if (dedomena[0].equals("get_channels")){
                ch = p.channels_present;
                String temp = "mainpagelist,";
                for(int i =0;i<ch.size();i++){
                    if(i!= ch.size()-1){
                        temp = temp + ch.get(i)+",";
                    }
                    else{
                        temp = temp + ch.get(i);
                    }
                }
                Log.i("testsame",temp);
                return temp;
            }
            else if(dedomena[0].equals("set channel name")){
                p.dealWithInterface(p.port+3000,dedomena);
            }
            else{
                // name of channel to sub
                p.dealWithInterface(p.port+3000,dedomena);
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.contains("mainpagelist")){
            //do stuff
            Log.i("debugpresentlist","temp "+ result);
            temp = result;
            String [] channels_searched = result.split(",");
            String [] channels_searched2 = new String[channels_searched.length-1];

            for(int i=0;i<channels_searched2.length;i++){
                channels_searched2[i] = channels_searched[i+1];
            }

            ArrayAdapter adapter_channel = new ArrayAdapter<String>(main_page,R.layout.activity_listview,channels_searched2);
            ListView listView_channels = viewById;
            listView_channels.setAdapter(adapter_channel);
            //myMethod(result);
        }
        else if(result.contains("searchhashtaglist")){
            //do stuff
            Log.i("debugpresentlist","temp "+ result);
            temp = result;
            String [] channels_searched = result.split(",");
            String [] channels_searched2 = new String[channels_searched.length-1];

            for(int i=0;i<channels_searched2.length;i++){
                channels_searched2[i] = channels_searched[i+1];
            }

            ArrayAdapter adapter_channel = new ArrayAdapter<String>(search,R.layout.activity_listview,channels_searched2);
            ListView listView_channels = hashList;
            listView_channels.setAdapter(adapter_channel);
            //myMethod(result);
        }

    }



}
