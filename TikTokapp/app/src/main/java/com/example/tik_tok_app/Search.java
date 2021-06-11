package com.example.tik_tok_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private String[] all_searched_videos;
    private ArrayList<String> kati = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Image button on bottom

        ImageButton home_ = findViewById(R.id.search_button_main_page);
        ImageButton videos_ = findViewById(R.id.search_button_videos);
        ImageButton search_ = findViewById(R.id.search_button_search_page);

        home_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, Main_Page.class);
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
        EditText search_ch = findViewById(R.id.search_input_search_channel_name);
        String channel_ = search_ch.getText().toString();
        ImageButton search_channels = findViewById(R.id.search_button_channel_name);

        search_channels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchByChannel(channel_);
            }
        });


        //search me hashtags
        EditText search_hash = findViewById(R.id.search_input_search_hashtag);
        String hash_ = search_hash.getText().toString();
        ImageButton search_hashtags = findViewById(R.id.search_button_hashtag);

        search_hashtags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchByHashtags(hash_);
            }
        });

    }

    private void showSearchByHashtags(String hash_) {
        kati.add("hashtag");
        int count =0;
        all_searched_videos = new String[kati.size()];//kati tha einai ta channels mas poy einai Array list
        for(int i=0; i<kati.size();i++){
            if(kati.get(i).contains(hash_)){//--------------------->>> PROSOXI EDW SYGKRINW TA HASHTAGS  .getHashtags()
                all_searched_videos[count++] = kati.get(i);
            }
        }

        ArrayAdapter adapter_channels = new ArrayAdapter<String>(this,R.layout.activity_listview,all_searched_videos);
        ListView listView_channels = (ListView)findViewById(R.id.search_list);
        listView_channels.setAdapter(adapter_channels);
    }



    private void showSearchByChannel(String channel_) {
        kati.add("channel");
        int count =0;
        all_searched_videos = new String[kati.size()];//kati tha einai ta channels mas poy einai Array list
        for(int i=0; i<kati.size();i++){
            if(kati.get(i).contains(channel_)){
                all_searched_videos[count++] = kati.get(i);
            }
        }

        ArrayAdapter adapter_hashtags = new ArrayAdapter<String>(this,R.layout.activity_listview,all_searched_videos);
        ListView listView_hashtags = (ListView)findViewById(R.id.search_list);
        listView_hashtags.setAdapter(adapter_hashtags);
    }
}
