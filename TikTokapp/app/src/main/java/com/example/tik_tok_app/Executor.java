package com.example.tik_tok_app;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import classes_needed.Publisher;

public class Executor extends AsyncTask<String,String,String> {

    public Publisher p;
    public ArrayList<String> ch = new ArrayList<>();
    public String temp;
    private  ListView viewById;
    private Main_Page main_page;

    public Executor(Publisher pub){
        this.p = pub;
    }

    public Executor(Publisher p, ListView viewById, Main_Page main_page) {
        this.p = p;
        this.viewById = viewById;
        this.main_page = main_page;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String...dedomena) {
        Log.i("test","do in back port "+p.port);
        // first pos is the id of the task
        if(dedomena[0].equals("push")){
            p.dealWithInterface(p.port+3000,dedomena);
        }else if(dedomena[0].equals("subscribe")){
            p.dealWithInterface(p.port+3000,dedomena);
        }else if (dedomena[0].equals("get_channels")){
            ch = p.channels_present;
            String temp = "";
            for(int i =0;i<ch.size();i++){
                if(i!= ch.size()-1){
                    temp = temp + ch.get(i)+",";
                }
                else{
                    temp = temp + ch.get(i);
                }
            }

            return temp;
        }
        else{
            // name of channel to sub
            p.dealWithInterface(p.port+3000,dedomena);
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("")){
            //do stuff
            Log.i("debugpresentlist","temp "+ result);
            temp = result;
            String [] channels_searched = result.split(",");

            ArrayAdapter adapter_channel = new ArrayAdapter<String>(main_page,R.layout.activity_listview,channels_searched);
            ListView listView_channels = viewById;
            listView_channels.setAdapter(adapter_channel);
            //myMethod(result);
        }

    }



}
