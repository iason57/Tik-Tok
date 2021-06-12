package com.example.tik_tok_app;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import classes_needed.Broker;
import classes_needed.ChannelName;
import classes_needed.Consumer;
import classes_needed.Node_;
import classes_needed.Publisher;
import classes_needed.VideoFile;
import java.io.*;

public class Main_Page extends AppCompatActivity {
    private String [] channels_searched;
    private ArrayList<String> kati = new ArrayList<>();//tha tin sbisoume
    private Button submit_upload;
    private VideoView videoView;
    private MediaController mc;
    private static int VIDEO_REQUEST = 101;
    private Uri videoUri = null;
    private Socket clientSocket;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        int is_init = getIntent().getIntExtra("initialized_socket",-2); // not initialized --> initialized : 0

        presentList();

        class Publisher_ extends AsyncTask<Integer,Integer,Integer> {

            @Override
            protected Integer doInBackground(Integer...port) {
                Publisher t2 = new Publisher(port[0],1);
                t2.start();
                return 1;
            }

            /*
            @Override
            protected void onPostExecute(String result) {
                // do in screen when you receive something from server in doInBackground
                // when thread finishes he will call onPostExecute and transform some EditText
                // or something like that !

            }
            */
        }

        if(is_init == -2 ) {
            Publisher_ t1 = new Publisher_();

            t1.execute(5668);
        }

        // testing sockets




        // end test

        // Image button on bottom

        ImageButton home_ = findViewById(R.id.main_button_main_page);
        ImageButton videos_ = findViewById(R.id.main_button_videos);
        ImageButton search_ = findViewById(R.id.main_button_search_page);

        home_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Page.this, Main_Page.class);
                intent.putExtra("initialized_socket",0);
                startActivity(intent);
            }
        });

        videos_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Page.this, For_you.class);
                startActivity(intent);
            }
        });

        search_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Page.this, Search.class);
                startActivity(intent);
            }
        });



        //button search channel name

        ImageButton button_search = findViewById(R.id.main_button_search);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //main input Search
                EditText main_search = findViewById(R.id.main_input_search);
                String channel_search = main_search.getText().toString();
                findChannelName(channel_search);
                //

            }
        });



        //button search channel name

        Button sub_submit = findViewById(R.id.main_button_sub);

        sub_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //main input subscribe
               EditText main_sub = findViewById(R.id.main_input_sub);
               String subscribe_ = main_sub.getText().toString();
               boolean flag = false;
                for(int i=0; i<kati.size();i++){
                    if(kati.get(i).equals(subscribe_)){
                        subscribe(subscribe_);
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    Toast.makeText(Main_Page.this,"Channel name doesn't exists!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        submit_upload = findViewById(R.id.main_button_upload);
        videoView =findViewById(R.id.kourada);
        mc = new MediaController(Main_Page.this);
        videoView.setMediaController(mc);

        submit_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Search video name and hashtags
                EditText video_name = findViewById(R.id.main_input_upload_videoName);
                String v_name = video_name.getText().toString();


                EditText hashtags = findViewById(R.id.main_input_upload_hashtags);
                String [] v_hashtags = video_name.getText().toString().split(",");


                Intent pickIntent = new Intent (Intent.ACTION_PICK);
                pickIntent.setType("video/*");
                startActivityForResult(pickIntent,1);


            }
        });

        //Image button rec

        ImageButton rec = findViewById(R.id.main_button_rec);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent,VIDEO_REQUEST);
            }
        });

    }

    @Override
    public void onActivityResult(int req , int resultCode, Intent data){
        super.onActivityResult(req,resultCode,data);

        if(req == VIDEO_REQUEST && resultCode==RESULT_OK){
            videoUri = data.getData();

            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }else if (req == 1){
            videoUri = data.getData();
            //save video sto channel ekeinou poy patise to upload
            //give name of the video hashtags ..


            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }

    private void subscribe(String subscribe_) {//------------------------------------------------------------>>>>>>LEIPEI O EGKEFALOS TOY IASONA

    }

    private void findChannelName(String channel_search) {
        kati.add("o iasonas kai i elena einai malakes");
        //prepeu me to channel search poy edwse o xristis ma broume se poia kanalia ginete contain kai ayto ton pinaka na ton dwsome sthn 71(list_food)
        int count =0;
        channels_searched = new String[kati.size()];//kati tha einai ta channels mas poy einai Array list
        for(int i=0; i<kati.size();i++){
            if(kati.get(i).contains(channel_search)){
                channels_searched[count++] = kati.get(i);
            }
        }

        ArrayAdapter adapter_channel = new ArrayAdapter<String>(this,R.layout.activity_listview,channels_searched);
        ListView listView_channels = (ListView)findViewById(R.id.main_list);
        listView_channels.setAdapter(adapter_channel);

    }

    private void presentList(){

        kati.add("o iasonas kai i elena einai malakes222");

        channels_searched = new String[kati.size()];//kati tha einai ta channels mas poy einai Array list
        for(int i=0; i<kati.size();i++){
            channels_searched[i] = kati.get(i);
        }


        ArrayAdapter adapter_channel = new ArrayAdapter<String>(this,R.layout.activity_listview,channels_searched);
        ListView listView_channels = (ListView)findViewById(R.id.main_list);
        listView_channels.setAdapter(adapter_channel);
    }

}
