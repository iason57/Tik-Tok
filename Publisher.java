import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.Desktop;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Publisher extends Thread implements Publisher_interface,Node{

    ArrayList<String> hash = new ArrayList<String>();
    public Socket clientSocket;
    public Socket clientSocket_tosend;
    public PrintWriter out;
    public BufferedReader in;
    public Broker broker;
    public int port;
    public int id;
    public ChannelName channelName = new ChannelName();
    private int counting_port = 0;
    private int ok = 0 ; // not added in channels
    public String video_name_temp;

    public Publisher(){

    }

    public Publisher(int p, int di){
        port = p;
        id =di;
    }

    public Publisher(Socket clSocket,int number_of_thread,int port,String k){
        clientSocket = clSocket;
        id = number_of_thread;
        this.port = port;
        counting_port = port;
        channelName.setChannelName(k);
    }

    public Publisher(Publisher x) {
        this.clientSocket = x.clientSocket;
        this.out = x.out;
        this.in = x.in;
        this.broker = x.broker;
        this.port = x.port;
        this.id = x.id;
        this.channelName.setChannelName(x.channelName.getChannelName());
    }

    //--------------------------------------------------------------

    public ChannelName getChannel(){
        return channelName;
    }

    public void setBroker(Broker br){
        this.broker = br;
    }

    public void addHashTag(String str){
        hash.add(str);
    }
    public void removeHashTag(String str){

    }
    public void getBrokerList(){

    }
    public Broker HashTopic(String str){
        return null;
    }
    
    public void notifyFailure(Broker b){
        
    }
    public void notifyBrokersForHashTags(String str){
        
    }
    public void generateChunks(String str){
        
    }

    public void push(String new_video_name, String path, ArrayList<String> hash ){
        System.out.println("Pushing video!");
        // v   : video 
        // elegxei an yparxei hdh ayto to video
        boolean flag = false;
        ArrayList<VideoFile> videos = new ArrayList<VideoFile>(channelName.getAllVideos());
        //System.out.println("get video!");
        for(int i=0; i<videos.size();i++){
            if(videos.get(i).getName().equals(new_video_name)){
                // apostolh kapws ston broker
                flag = true;
                break;
            }
        }
        if(!flag) upload_video(new_video_name,path,hash);
        else System.out.println("Video already uploaded!");
    }

    public String calculateKeys(String input){ // topics = channel_name, hashtags
        try {
  
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
  
            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());
  
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
  
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public int thesi_broker_hash(String s){
        String hashed_ = (this.calculateKeys(s));
        int len = hashed_.length();
        String test2 = hashed_.substring(len-3);
        int hashed,hashed_key;
        //System.out.println("flag 2");
        //System.out.println("Test 2 : "+test2);
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < test2.length(); k++) {
            if (Character.isLetter(test2.charAt(k))) {
                int m = test2.charAt(k);
                sb.append(m); // add the ascii value to the string
            } else {
                sb.append(test2.charAt(k)); // add the normal character
            }
        }        
        //System.out.println("flag 3");
        hashed = Integer.parseInt(sb.toString());
              
        hashed_key = hashed % brokers.size(); 
        return hashed_key;
    }

    public void upload_video(String new_video_name,String path,ArrayList<String> hash){
        //System.out.println("In upload");
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String name,str;//path,
        int hashed,hashed_key;
        
        name = new_video_name;
        try{
            //System.out.println("Give the path for the video");
            //path =  reader.readLine(); 
                
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();
            
            //channel name already a variable in this class

            /*
            ArrayList<String> hash = new ArrayList<String>();
            System.out.println("Give the hashes (exit to exit)");
            while(true){
                str =  reader.readLine(); 
                if(!str.equals("exit")) hash.add(str);
                else break;
            }
            */

            VideoFile new_video = new VideoFile(name,channelName,(String)dtf.format(now),path,hash);
            broker.last_video = new_video;
            last_video_search.set(0, new_video);
            System.out.println("VideoFile created");
            channelName.add_video(new_video);
            //System.out.println("VideoFile added to list");
            hashed_key = thesi_broker_hash(channelName.getChannelName());
            //System.out.println("hashing and hash key : "+hashed_key);

            boolean flag=false;
            
            if(ok==0) {
                allChannels.add(channelName);
                for(int j =0; j<brokers.size();j++){
                    if(hashed_key == j ){
                        flag = false;
                        for(ChannelName x : brokers.get(j).channels_serviced){
                            if(x.getChannelName().equals( channelName.getChannelName() )){
                                flag = true;
                                break;
                            }
                        }
                        if(!flag) {
                            brokers.get(j).channels_serviced.add(channelName);
                            //System.out.println("hohoho");
                            brokers.get(j).subscribers.add( new ArrayList<Consumer>() );
                            brokers.get(j).subscribers_p.add( new ArrayList<Publisher>() );
                        }
                    }   
                }
                ok =1;
                System.out.println("Added channel to all channels");
            }
            for(int k=0;k<brokers.size();k++){
                System.out.print("edw typwnw ta hashed channel name tou broker : "+k+" > ");
                for(ChannelName x : brokers.get(k).channels_serviced){
                    System.out.print(x.getChannelName()+", ");
                }
                System.out.println(" ");
            }

            for (int i=0; i<hash.size(); i++){
                hashed_key = thesi_broker_hash(hash.get(i));

                for(int j =0; j<brokers.size();j++){
                    //brokers.get(j).videos_hash.add(new ArrayList<VideoFile>());
                    if(hashed_key == j && !brokers.get(j).hashtags_serviced.contains(hash.get(i)) ){
                        brokers.get(j).hashtags_serviced.add(hash.get(i));
                        brokers.get(j).videos_hash.add(new ArrayList<VideoFile>());
                        brokers.get(j).videos_hash.get( brokers.get(j).videos_hash.size()-1).add(new_video);
                        break;
                    }else if(hashed_key == j && brokers.get(j).hashtags_serviced.contains(hash.get(i))){
                        for (int k=0;k<brokers.get(j).hashtags_serviced.size();k++){
                            if(brokers.get(j).hashtags_serviced.get(k).equals(hash.get(i))){
                                brokers.get(j).videos_hash.get(k).add(new_video);
                            }

                        }
                       
                    }
                }
            }
            
            System.out.println("edw typwnw ta hashed hashtags tou broker 0: " +brokers.get(0).hashtags_serviced);
            //System.out.println("edw typwnw ta hashed hashtags tou broker 1: " +brokers.get(1).hashtags_serviced);
            //System.out.println("edw typwnw ta hashed hashtags tou broker 2: " +brokers.get(2).hashtags_serviced);
            //System.out.println("edw typwnw ta hashed hashtags tou broker 3: " +brokers.get(3).hashtags_serviced);

            for(int i=0; i<hash.size(); i++){
                flag = true;
                for(int j=0; j<channelName.getHashtagsPublished().size();j++){
                    if( (hash.get(i)).equals( channelName.getHashtagsPublished().get(j) ) ) flag = false;
                }
                if(flag) channelName.getHashtagsPublished().add(hash.get(i));
            }
            //System.out.println("done with hash");
            String video_file_to_send = new_video.getPath();
            File myFile = new File (video_file_to_send);
            byte [] allfile  = new byte [(int)myFile.length()];
            int pointer_in_file=0;
            int chunk = 100000;
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os = null;
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(allfile,0,(int)myFile.length());
            clientSocket_tosend = new Socket(Inet4Address.getLocalHost().getHostAddress(), port-1000); // htan port + 2000 : emeis theloume video 5666
            //counting_port +=1;
            os = clientSocket_tosend.getOutputStream();                                           // kai messages 7666
            //System.out.println("after read");
            while((int)myFile.length() > pointer_in_file){
                if( (pointer_in_file + 100000) > (int)myFile.length() ){
                    chunk = (int)myFile.length()%100000;
                }
                byte [] mybytearray  = new byte [chunk+3];
                String k = "end";
                byte[] b = k.getBytes();
                mybytearray[mybytearray.length - 3] = b[0];
                mybytearray[mybytearray.length  - 2] = b[1];
                mybytearray[mybytearray.length  - 1] = b[2];
                for(int i=pointer_in_file;i < (pointer_in_file+chunk);i++){
                    mybytearray[i-pointer_in_file] = allfile[i];
                }
                
                System.out.println("Sending " + video_file_to_send + "(" + (mybytearray.length -3) + "bytes) part :"+(pointer_in_file/100000 +1 ) );    
                os.write(mybytearray,0,mybytearray.length);
                os.flush();
                System.out.println("Done sending(upload to broker).");
                pointer_in_file += 100000;         
            }
            fis.close();
            bis.close();
            os.close();

            System.out.println("File total size is : "+(int)myFile.length());
        }
        catch(Exception e){
            System.out.println("Problem with the video upload.");
        } 
    }

    //Node methods

    public void init(int i){
        //while(true){
            try{
                clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), port);
                clientSocket_tosend = new Socket(Inet4Address.getLocalHost().getHostAddress(), port+3000);
            }
            catch(Exception e){
    
            }
        //}
    }

    public void connect(int port1){

        System.out.println("Message section : ");
        String str;
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String message_from_server;
        //System.out.println("Reader ok ");
        System.out.println("port is  "+ port1);
        try{
            Socket clientSocket2 = clientSocket_tosend;
            out = new PrintWriter(clientSocket2.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
            //System.out.println("out kai in ok.");
            message_from_server = in.readLine();
            System.out.println("Connection established, server said : "+message_from_server);
            while(true){
                str =  reader.readLine();
                out.println(str);
                if(str.equals("subscribe")){
                    System.out.println("Available channels : ");
                    message_from_server = in.readLine();
                    message_from_server = message_from_server.replace("Available channels : ", "");
                    int size = Integer.parseInt(message_from_server); 
                    for(int i =0; i< size ;i++){
                        message_from_server = in.readLine();
                        System.out.println(""+message_from_server);
                    }
                }
                else if(str.equals("search")){
                    message_from_server = in.readLine(); //want to search name or hash?
                    System.out.println(""+message_from_server);
                    str =  reader.readLine();
                    out.println(str); // name or hashtag option

                    if(str.contains("name") || str.contains("Name")){
                        System.out.println("Available channels : ");
                        message_from_server = in.readLine(); //size
                        message_from_server = message_from_server.replace("Available channels : ", "");
                        int size = Integer.parseInt(message_from_server); 
                        for(int i =0; i< size ;i++){
                            message_from_server = in.readLine();
                            System.out.println(""+message_from_server);
                        }

                        message_from_server = in.readLine();
                        System.out.println(""+message_from_server);
                        str =  reader.readLine();
                        out.println(str); // the name of the channel or the hashtag
                        message_from_server = in.readLine();
                        size = Integer.parseInt(message_from_server); //size
                        for(int i =0; i< size ;i++){
                            message_from_server = in.readLine();
                            System.out.println(""+message_from_server);
                        }
                        message_from_server = in.readLine();
                        if(!message_from_server.equals("Not found")){
                            System.out.println(""+message_from_server);
                            str =  reader.readLine();
                            out.println(str); // choice of video
                        }

                    }else{
                        System.out.println("Available hashtags: ");
                        message_from_server = in.readLine(); //broker size
                        int size = Integer.parseInt(message_from_server);
                        for (int i=0;i<size;i++){
                            message_from_server = in.readLine(); //hashtags list size
                            int size2 = Integer.parseInt(message_from_server);
                            for (int j=0;j<size2;j++){
                                message_from_server = in.readLine(); //hashtags
                                System.out.println(""+message_from_server);
                            }
                        }

                        message_from_server = in.readLine(); //give hashtag
                        System.out.println(""+message_from_server);
                        str =  reader.readLine(); //choice
                        out.println(str); // the name of the hashtag
                        message_from_server = in.readLine(); //found or not found

                        if(!message_from_server.equals("Not found")){
                            System.out.println(""+message_from_server);
                            System.out.println("Available videos: ");
                            message_from_server = in.readLine(); //videos hash size
                            size = Integer.parseInt(message_from_server);
                            for (int j=0;j<size;j++){
                                message_from_server = in.readLine(); 
                                System.out.println(""+message_from_server);
                            }
                            message_from_server = in.readLine(); //choose video
                            System.out.println(""+message_from_server);
                            str =  reader.readLine(); //choice of video
                            out.println(str); 
                        }

                    }
                    /*message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                    str =  reader.readLine();
                    out.println(str);
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                    str =  reader.readLine();
                    out.println(str);
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);*/
                }
                else if(str.equals("set channel name")){
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                    str =  reader.readLine();
                    out.println(str);
                }
                else if(str.equals("push")){
                    // get name 
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                    str =  reader.readLine();
                    out.println(str);
                    // get path
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                    str =  reader.readLine();
                    out.println(str);
                    // give the hashes
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                    while(true){
                        str =  reader.readLine();
                        out.println(str);
                        if(str.equals("exit")) {
                            break;
                        }
                    }
                    // message : Start to push video
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                }
                else{
                    if(str.equals("..")){
                        System.exit(0);
                    }
                    message_from_server = in.readLine();
                    System.out.println("Broker "+port1+" said : "+message_from_server);
                }
            }
        }
        catch(Exception e){
            System.out.println("Exception in messages");
        }
    }
    
    public void download(){
        String str;
        String message_from_server;
        String k = "end";
        byte[] b = k.getBytes();

        Byte w1 = new Byte(b[0]);
        Byte w2 = new Byte(b[1]);
        Byte w3 = new Byte(b[2]);
        int part = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;       
        Socket clientSocket2;     
            try{
                int file_size = 100003;
                int bytesRead;
                int current = 0;
                clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), port+4000 );
                InputStream is = clientSocket.getInputStream();
                byte [] mybytearray  = new byte [file_size];
                //mpakale test
                byte [] to_mp4_full  = new byte [10000000];
                int pointer = 0;
                String video_file  = "publisher-source-downloaded-"+port+"-"+id+"-"+video_name_temp+".mp4";
                fos = new FileOutputStream(video_file);
                bos = new BufferedOutputStream(fos);
                while (true){

                    mybytearray  = new byte [file_size];
                    
                    try{
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        
                        current = 0;
                        
                        do {
                            if(w1.equals(new Byte(mybytearray[current]))){
                                //System.out.println("mpika"+ current);
                                if(w2.equals(new Byte(mybytearray[current+1]))){
                                    //System.out.println("mpika2"+ current);
                                    if(w3.equals(new Byte(mybytearray[current+2]))){
                                        //System.out.println("mpika3"+ current);
                                        break;
                                    }
                                    else{
                                        current++;
                                    }
                                }
                                else{
                                    current++;
                                }
                            }
                            else{
                                current++;
                            }
                        } while(true);

                        for(int i = pointer; i<pointer+current;i++){
                            to_mp4_full[i] = mybytearray[i-pointer]; // [0-current]
                        }
                        pointer+=current;
                        
                    }
                    catch(FileNotFoundException e){
                        System.out.println("Exception bro.");
                    }    
                       
                    if (current < 100000 ) {
                        break;
                    }
                }
                bos.write(to_mp4_full, 0 , pointer);
                bos.flush();
                System.out.println("File " + video_file
                    + " downloaded (" + pointer + " bytes read)");
                
                is.close();
                fos.close();
                bos.close();

                System.out.println("Closing readers");
            }
            catch (Exception e){
                System.out.println("Exception in download in publisher.");
            }
            
    }

    public void download2(String filename){
        String str;
        String message_from_server;
        String k = "end";
        byte[] b = k.getBytes();

        Byte w1 = new Byte(b[0]);
        Byte w2 = new Byte(b[1]);
        Byte w3 = new Byte(b[2]);
        int part = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;       
        Socket clientSocket2;     
            try{
                int file_size = 100003;
                int bytesRead;
                int current = 0;
                clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), port+4000 );
                InputStream is = clientSocket.getInputStream();
                byte [] mybytearray  = new byte [file_size];
                //mpakale test
                byte [] to_mp4_full  = new byte [10000000];
                int pointer = 0;
                String video_file  = "publisher-source-downloaded-"+port+"-"+id+"-"+filename+".mp4";
                fos = new FileOutputStream(video_file);
                bos = new BufferedOutputStream(fos);
                while (true){

                    mybytearray  = new byte [file_size];
                    
                    try{
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        
                        current = 0;
                        
                        do {
                            if(w1.equals(new Byte(mybytearray[current]))){
                                //System.out.println("mpika"+ current);
                                if(w2.equals(new Byte(mybytearray[current+1]))){
                                    //System.out.println("mpika2"+ current);
                                    if(w3.equals(new Byte(mybytearray[current+2]))){
                                        //System.out.println("mpika3"+ current);
                                        break;
                                    }
                                    else{
                                        current++;
                                    }
                                }
                                else{
                                    current++;
                                }
                            }
                            else{
                                current++;
                            }
                        } while(true);

                        for(int i = pointer; i<pointer+current;i++){
                            to_mp4_full[i] = mybytearray[i-pointer]; // [0-current]
                        }
                        pointer+=current;
                        
                    }
                    catch(FileNotFoundException e){
                        System.out.println("Exception bro.");
                    }    
                       
                    if (current < 100000 ) {
                        break;
                    }
                }
                bos.write(to_mp4_full, 0 , pointer);
                bos.flush();
                System.out.println("Publisher file " + video_file
                    + " downloaded (" + pointer + " bytes read)");
                
                is.close();
                fos.close();
                bos.close();

                System.out.println("Closing readers");
            }
            catch (Exception e){
                System.out.println("Exception in download in publisher.");
            }
    }
    

    public void disconnect(int y){

    }
    public void updateNodes(){

    }
    public List<Broker> getBrokers(){
        return null;
    }

    public void run(){
        try{
            this.init(port);
            //this.download();
            Thread.sleep(2000);
            this.connect(port + 3000);
            
        }
        catch(Exception e){

        }
    }

    public static void main(String args[]) {
        
        Publisher t1 = new Publisher(5667,1);
        
        t1.start();
        
        
        /*
        try{
            Thread.sleep(1000);
        }
        catch(Exception e){
            System.out.println("Something went wrong with waiting.");
        }  
    
        
        
        Publisher t11 = new Publisher(5666,2);
        
        t11.start();
        */
        
        
    }
}