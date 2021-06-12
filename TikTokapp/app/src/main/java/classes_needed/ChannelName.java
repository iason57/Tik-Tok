package classes_needed;

import java.util.*;

import java.lang.*;


public class ChannelName{

    private String channelName;
    private ArrayList<String> hashtagsPublished;
    //private HashMap<String, ArrayList<Value>> userVideoFilesMap; // <path ,  object of video> 
    private ArrayList<VideoFile> listofVideos;
    private VideoFile lastvid;

    public ChannelName(){
        this.channelName = "";
        this.hashtagsPublished = new ArrayList<String>();
        this.listofVideos = new ArrayList<VideoFile>();
    }

    public ChannelName(String channel){
        this.channelName = channel;
        this.hashtagsPublished = new ArrayList<String>();
        this.listofVideos = new ArrayList<VideoFile>();
    }

    public String getChannelName(){
        return channelName;
    }

    public void setChannelName(String kati){
        this.channelName=kati;
    }

    public ArrayList<String> getHashtagsPublished(){
        return hashtagsPublished;
    }

    public ArrayList<VideoFile> getAllVideos(){
        return listofVideos;
    }

    public VideoFile get_last_video(){
        return listofVideos.get(listofVideos.size()-1);
    }

    public void add_video(VideoFile x){
        listofVideos.add(x);
    }
}