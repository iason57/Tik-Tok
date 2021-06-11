package com.example.tik_tok_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class For_you extends AppCompatActivity {

    private Button pick;
    private VideoView videoView;
    private MediaController mc;
    private String [] videos_subscribed;
    private ArrayList<String> kati = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_you);


        presentSubVideos();

        // Image button on bottom
        ImageButton home_ = findViewById(R.id.videos_button_main_page);
        ImageButton videos_ = findViewById(R.id.videos_button_videos);
        ImageButton search_ = findViewById(R.id.videos_button_search);

        home_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(For_you.this, Main_Page.class);
                startActivity(intent);
            }
        });

        videos_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(For_you.this, For_you.class);
                startActivity(intent);
            }
        });

        search_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(For_you.this, Search.class);
                startActivity(intent);
            }
        });


        pick = findViewById(R.id.videos_button_play);
        videoView =findViewById(R.id.videos_videoPlayer);
        mc = new MediaController(For_you.this);
        videoView.setMediaController(mc);




        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText str = findViewById(R.id.videos_input);
                String name_ = str.getText().toString();

                String path = "storage/self/primary/Download/"+name_+".mp4";
                Uri a =Uri.parse(path);
                Intent pickIntent = new Intent (Intent.ACTION_VIEW,a);
                pickIntent.setDataAndType(a,"video/mp4");
                //startActivity(pickIntent);

                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(a);
                videoView.start();


            }
        });

    }


    private void presentSubVideos() {
        kati.add("test");
        //prepeu me to channel search poy edwse o xristis ma broume se poia kanalia ginete contain kai ayto ton pinaka na ton dwsome sthn 71(list_food)
        int count =0;
        videos_subscribed = new String[kati.size()];//kati tha einai ta channels mas poy einai Array list
        for(int i=0; i<kati.size();i++){
            videos_subscribed[count++] = kati.get(i);
        }

        ArrayAdapter adapter_videos = new ArrayAdapter<String>(this,R.layout.activity_listview,videos_subscribed);
        ListView listView_video_sub = (ListView)findViewById(R.id.videos_video_list);
        listView_video_sub.setAdapter(adapter_videos);
    }

    @Override
    public void onActivityResult(int req , int resultCode, Intent data){
        super.onActivityResult(req,resultCode,data);
        if (req == 1){
            Uri videoUri = data.getData();
            //save video sto channel ekeinou poy patise to upload
            //give name of the video hashtags ..


            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();


        }
    }

}
