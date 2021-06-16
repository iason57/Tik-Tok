package com.example.tik_tok_app;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
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

import com.example.tik_tok_app.Memory.Pref;

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
    private String v_name;
    private String [] v_hashtags;
    private Publisher_ t1;
    private Consumer_ t2;
    private Publisher p;
    private Consumer c;
    private Executor new_temp;
    private ArrayList<String> all_channels;
    private int count_searches = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        int is_init = getIntent().getIntExtra("initialized_socket",-2); // not initialized --> initialized : 0

        if(is_init == -2 ) {
            // auto ginetai mono thn proti for
            // ara prepei seiriaziable publisher
            // kai na bei se intent
            // giati alliws apla den ginetai initialize
            // thn deyterh fora
            // den yphrxei o publisher
            t1 = new Publisher_();
            t2 = new Consumer_();
            p = new Publisher(5666,1);
            c = new Consumer(6666,1);
            t1.execute(p);
            t2.execute(c);
            Log.i("testsame","init pub "+p.port);
            //Pref.write(getApplicationContext(),p);
            //p = Pref.readPubFromPref(this);
            Pref.p = p;
            Pref.c = c;
            Log.i("testsame","init pub after read from memory"+Pref.p.port);
            Log.i("testsame","init pub after read from memory"+Pref.c.port);

            String [] init_name = new String[2];
            init_name[0]="set channel name";
            init_name[1]="mpampis";
            new_temp = new Executor(p);
            new_temp.execute(init_name);
        }
        else{
            p = Pref.p;
        }


        String [] data_ = new String[2];
        data_[0]="subscribe";
        data_[1]="present";
        new_temp = new Executor(p);
        new_temp.execute(data_);

        presentList();

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

                if(count_searches==0){
                    ListView listView_channels = (ListView)findViewById(R.id.main_list);

                    for(int i=0; i<listView_channels.getAdapter().getCount();i++){
                        all_channels.add(listView_channels.getAdapter().getItem(i).toString());
                    }
                }
                findChannelName(channel_search);


            }
        });



        //button for subscribe

        Button sub_submit = findViewById(R.id.main_button_sub);

        sub_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //main input subscribe
               EditText main_sub = findViewById(R.id.main_input_sub);
               String subscribe_ = main_sub.getText().toString().replace(" ","");

               ListView temp_list = (ListView)findViewById(R.id.main_list);

               Log.i("debuglista"," compare this : "+subscribe_);
               Log.i("debuglista",""+temp_list.getAdapter().getItem(0).toString());


               boolean flag = false;
                for(int i=0; i<temp_list.getAdapter().getCount();i++){
                    if(temp_list.getAdapter().getItem(i).toString().replace(" ","").equals(subscribe_)){
                        //subscribe(subscribe_);

                        String [] _data_ = new String[2];
                        _data_[0] = "subscribe";
                        _data_[1] = "";


                        new_temp = new Executor(p);
                        new_temp.execute(_data_);

                        String [] _data__ = new String[2];
                        _data__[0] = subscribe_.replace(" ","");
                        _data__[1] = "";

                        new_temp = new Executor(p);
                        new_temp.execute(_data__);

                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    Toast.makeText(Main_Page.this,"Channel name doesn't exists!",Toast.LENGTH_SHORT).show();
                }
                else{
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main_Page.this,p.interface_sub_message,Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);

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
                v_name = video_name.getText().toString();


                EditText hashtags = findViewById(R.id.main_input_upload_hashtags);
                v_hashtags = hashtags.getText().toString().split(",");


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

                //Search video name and hashtags
                EditText video_name = findViewById(R.id.main_input_upload_videoName);
                v_name = video_name.getText().toString();


                EditText hashtags = findViewById(R.id.main_input_upload_hashtags);
                v_hashtags = hashtags.getText().toString().split(",");

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent,VIDEO_REQUEST);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int req , int resultCode, Intent data){
        super.onActivityResult(req,resultCode,data);

        if(req == VIDEO_REQUEST && resultCode==RESULT_OK) { // record button
            videoUri = data.getData();

            Cursor cursor = getContentResolver().query(videoUri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);

            String new_path = "/storage/self/primary/DCIM/Camera/"+path.split("/")[path.split("/").length - 1].replace("/","");
            Log.i("paramOfPush", "new path" +new_path);

            /*
            System.out.println("file path > "+  path );
            try {
                String temp = "adb.exe pull "+new_path+" C:\\Users\\iason\\Desktop\\personal"; //adb.exe push C:\\Users\\iason\\Desktop\\personal\\video1.mp4 /storage/self/primary/DCIM/Camera/
                Log.i("paramOfPush", "command :" +temp );
                Log.i("paramOfPush", "video real name " + path.split("/")[path.split("/").length -1]);
                Runtime run2 = Runtime. getRuntime();
                Process pr2= run2. exec(temp);

            } catch (Exception e) {
                //TODO: handle exception
                Log.i("paramOfPush", "gamiemai me : "+e.toString() );
            }
            */



            Log.i("paramOfPush", "video name " + v_name);

            Log.i("paramOfPush", "path : " + path);

            ArrayList<String> hash_ = new ArrayList<>();

            for (int i = 0; i < v_hashtags.length; i++) {
                hash_.add(v_hashtags[i]);
                Log.i("paramOfPush", "hashtag : " + v_hashtags[i]);
            }

            String[] data_ = new String[v_hashtags.length + 3];

            data_[0] = "push";
            data_[1] = v_name;
            //data_[2] = path;
            data_[2] = "C:\\Users\\iason\\Documents\\AndroidStudio\\DeviceExplorer\\emulator-5554\\storage\\self\\primary\\DCIM\\Camera\\"+path.split("/")[path.split("/").length - 1].replace("/","");
            for (int i = 3; i < data_.length; i++) {
                data_[i] = v_hashtags[i - 3];
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Executor new_temp = new Executor(p);
                    new_temp.execute(data_);
                }
            }, 5000);



            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }else if (req == 1){ // upload button

            videoUri = data.getData();

            Cursor cursor = getContentResolver().query(videoUri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);

            Log.i("paramOfPush","video name "+v_name);

            Log.i("paramOfPush","path : "+path);

            ArrayList<String> hash_ = new ArrayList<>();

            for(int i=0;i<v_hashtags.length;i++){
                hash_.add(v_hashtags[i]);
                Log.i("paramOfPush","hashtag : "+v_hashtags[i]);
            }

            String[] data_ = new String[v_hashtags.length+3];

            data_[0] = "push";
            data_[1] = v_name;
            data_[2] = path;
            data_[2] = "C:\\Users\\iason\\Documents\\AndroidStudio\\DeviceExplorer\\emulator-5554\\storage\\self\\primary\\DCIM\\Camera\\"+path.split("/")[path.split("/").length-1];
            for(int i = 3;i<data_.length;i++){
                data_[i] = v_hashtags[i-3];
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Executor new_temp = new Executor(p);
                    new_temp.execute(data_);
                }
            }, 5000);


            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }

    private void findChannelName(String channel_search) {
        ListView listView_channels = (ListView)findViewById(R.id.main_list);

        ArrayList<String> get_count_temp = new ArrayList<>();

        for(int i=0; i<all_channels.size();i++){
            if(all_channels.get(i).contains(channel_search)){
                get_count_temp.add(all_channels.get(i));
            }
        }

        channels_searched = new String[get_count_temp.size()];//kati tha einai ta channels mas poy einai Array list

        for(int i=0; i<get_count_temp.size();i++){
            channels_searched[i] = get_count_temp.get(i);
        }

        ArrayAdapter adapter_channel = new ArrayAdapter<String>(this,R.layout.activity_listview,channels_searched);
        listView_channels.setAdapter(adapter_channel);
        count_searches++;
    }


    private void presentList(){

        Log.i("debugpresentlist","edwwwwwww-------> "+ p.port);

        new_temp = new Executor(Pref.p,(ListView)findViewById(R.id.main_list),this);
        String[] data_ = new String[1];
        data_[0]= "get_channels";
        new_temp.execute(data_);

        all_channels = new ArrayList<>();


    }

}
