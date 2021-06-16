package com.example.tik_tok_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.tik_tok_app.Memory.Pref;

import java.io.File;
import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private String[] all_searched_videos;
    private ArrayList<String> kati = new ArrayList<>();
    private Button play_video;
    private VideoView videoView;
    private MediaController mc;
    private Executor new_temp;
    private boolean flag_exist;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        all_searched_videos = new String[]{"5min","count"};

        // load hashtags

        String[] data_ = new String[5];

        data_[0] = "consumer";
        data_[1] = "search";
        data_[2] = "hashtag";
        data_[3] = "ahsldkfjhaklsjdfh";
        data_[4] = "ahsldkfjhaklsjdfh";

        new_temp = new Executor(Pref.c,(ListView)findViewById(R.id.hash_list),this);
        new_temp.execute(data_);


        // Image button on bottom

        ImageButton home_ = findViewById(R.id.search_button_main_page);
        ImageButton videos_ = findViewById(R.id.search_button_videos);
        ImageButton search_ = findViewById(R.id.search_button_search_page);

        home_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, Main_Page.class);
                intent.putExtra("initialized_socket",0);
                startActivity(intent);
            }
        });

        videos_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, For_you.class);
                startActivity(intent);
            }
        });

        search_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, Search.class);
                startActivity(intent);
            }
        });

        //search me channel name

        ImageButton search_channels = findViewById(R.id.search_button_channel_name);

        search_channels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_ch = findViewById(R.id.search_input_search_channel_name);
                String channel_ = search_ch.getText().toString();
                showSearchByChannel(channel_);
            }
        });


        //search me hashtags

        ImageButton search_hashtags = findViewById(R.id.search_button_hashtag);

        search_hashtags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_hash = findViewById(R.id.search_input_search_hashtag);
                String hash_ = search_hash.getText().toString();
                showSearchByHashtags(hash_);
            }
        });


        //PROSPATHEIA GIA EMFANISI TOY VIDEO SE OLI TIN OTHONI
        play_video = findViewById(R.id.search_button_play);

        play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText str = findViewById(R.id.search_input_play);
                String name_ = str.getText().toString();
                boolean f = getVideo(name_);
                if(f==true){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String[] data_ = new String[3];

                            data_[0] = "consumer";
                            data_[1] = "choiceofvid";
                            data_[2] = name_;
                            Log.i("herechoice","choice is : "+name_);

                            for(int i =0; i<data_.length;i++){
                                Log.i("choice","data tou "+i+" : "+data_[i]);
                            }

                            Executor new_temp = new Executor(Pref.c);
                            new_temp.execute(data_);
                        }
                    }, 1000);


                    //String path = "storage/self/primary/Download/"+name_+".mp4";

                    //thelw na vrw path tou telautaioy video search
                    //diatrexw folder

                    final Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String path="";
                            File file;
                            File dir = new File("storage/self/primary/search/");
                            if (dir.exists()) {
                                File[] files = dir.listFiles();
                                for (int i = 0; i < files.length; ++i) {
                                    file = files[i];
                                    if (file.getName().split("-")[file.getName().split("-").length-1].replace(".mp4","").equals(name_)){
                                        path = file.getAbsolutePath();
                                        break;
                                    }
                                }
                            }

                            Uri a =Uri.parse(path);
                            Intent test_ = new Intent(Intent.ACTION_VIEW,a);
                            test_.setDataAndType(a,"video/mp4");
                            startActivity(test_);
                        }
                    }, 15000);



                }
                else{
                    Toast.makeText(Search.this,"No such video!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private boolean getVideo(String name_) {
        boolean flag =false;
        for(int i=0;i<all_searched_videos.length;i++){
            if(all_searched_videos[i].equals(name_)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void showSearchByHashtags(String hash_) {
        flag_exist = true;

        String[] data_ = new String[4];
        data_[0] = "consumer";
        data_[1] = "search";
        data_[2] = "hashtage";
        data_[3] = hash_;

        for(int i =0;i<data_.length;i++){
            Log.i("debugshowlist",i+" : "+ data_[i]);
        }

        Executor new_temp = new Executor(Pref.c);
        new_temp.execute(data_);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int count =0;
                all_searched_videos = new String[Pref.c.videos_searched.size()];//kati tha einai ta channels mas poy einai Array list
                if(Pref.c.videos_searched.size()==0){
                    Toast.makeText(Search.this,"Not found",Toast.LENGTH_LONG).show();
                }
                for(int i=0; i<Pref.c.videos_searched.size();i++){
                    all_searched_videos[count++] = Pref.c.videos_searched.get(i);
                }

                presentWithDelay();

            }
        }, 2000);
    }



    private void showSearchByChannel(String channel_) {
        flag_exist = false;
        Log.i("pinakas",Pref.Allchanells.length+"");
        for(int i =0; i< Pref.Allchanells.length;i++){
            Log.i("pinakas","compare : " + channel_ + ", "+ Pref.Allchanells[i]);
            if(channel_.equals(Pref.Allchanells[i])){
                flag_exist = true;
                Log.i("pinakas","compare : " + channel_ + ", "+ Pref.Allchanells[i]);
                break;
            }
        }
        if(flag_exist) {
            String[] data_ = new String[4];

            data_[0] = "consumer";
            data_[1] = "search";
            data_[2] = "name";
            data_[3] = channel_;

            Executor new_temp = new Executor(Pref.c);
            new_temp.execute(data_);


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int count =0;

                    Log.i("videosearch","size is : "+Pref.c.videos_searched.size());
                    all_searched_videos = new String[Pref.c.videos_searched.size()];//kati tha einai ta channels mas poy einai Array list
                    for(int i=0; i<Pref.c.videos_searched.size();i++){
                        all_searched_videos[count++] = Pref.c.videos_searched.get(i);
                    }

                   presentWithDelay();

                }
            }, 5000);
        }
        else{
            presentWithDelay();
        }

    }

    private void presentWithDelay() {
        if(!flag_exist){
            all_searched_videos = new String[0];
            Toast.makeText(Search.this,"Not found",Toast.LENGTH_LONG).show();
            Log.i("pinakas","Tost");
        }
        ArrayAdapter adapter_hashtags = new ArrayAdapter<String>(this,R.layout.activity_listview,all_searched_videos);
        ListView listView_hashtags = (ListView)findViewById(R.id.search_list);
        listView_hashtags.setAdapter(adapter_hashtags);
    }


}
