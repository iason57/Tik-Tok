package classes_needed;

import java.util.*;
import java.lang.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 


public class VideoFile{

    //Variables
    private String videoName;
    private ChannelName channelName;
    private String dateCreated;
    private String path;
    public String path_in_broker; 
    //private String length;
    //private String framerate;
    //private String frameWidth;
    //private ArrayList<String> frameHeight;
    private ArrayList<String> associatedHashtags;
    //private byte videoFileChunk;

    public String getName(){
        return videoName;
    }

    public ChannelName getChannelName(){
        return channelName;
    }

    public String getDateCreated(){
        return dateCreated;
    }

    public String getPath(){
        return path;
    }

    public ArrayList<String> getHashtags(){
        return associatedHashtags;
    }


    public VideoFile(String videoname, ChannelName channel, String date, String path, ArrayList<String> hashtags){//, String len, String frate, String width, ArrayList<String> height, byte chunk
        this.videoName = videoname;
        this.channelName = channel;
        this.dateCreated = date;
        this.path = path;
        this.associatedHashtags = new ArrayList<String>(hashtags);
        //this.length = len;
        //this.framerate = frate;
        //this.frameWidth = width;
        //this.frameHeight = height;
        //this.videoFileChunk = chunk;
    }

    public VideoFile(){
        this.videoName = "";
        this.channelName = new ChannelName();
        this.dateCreated = "";
        this.path = "";
        this.associatedHashtags = new ArrayList<String>();
        //this.length = "";
        //this.framerate = "";
        //this.frameWidth = "";
        //this.frameHeight = new ArrayList<String>();
        //this.videoFileChunk = 0;
   }
}