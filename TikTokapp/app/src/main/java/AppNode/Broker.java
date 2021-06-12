package AppNode;

import java.util.*;
import java.util.concurrent.Flow.Subscriber;
import java.net.*;
import java.io.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Broker extends Thread implements Broker_interface, Node {

    public List<Consumer> registeredUsers;
    public List<Publisher> registeredPublishers;
    public ServerSocket serverSocket;
    public ServerSocket serverSocket_forvideos;
    public ServerSocket serverSocket_p_forvideos;
    public Socket clientSocket; // mallon oxi
    public PrintWriter out;
    public BufferedReader in;
    private int number_of_thread = 1;
    private int number_of_thread_p = 1;
    public int port;
    public int publisher_port;
    public ServerSocket publisherServerSocket;
    public ServerSocket messagesServerSocket;
    public ServerSocket messagesServerSocket_p;
    public ArrayList<ChannelName> channels_serviced; // ["iasonas"]
    public ArrayList< ArrayList<Consumer> > subscribers; // 1-1 antistoixia me ta channels_serviced - get(0)-->lista subs tou iasonas
    public ArrayList< ArrayList<Publisher> > subscribers_p; // 1-1 antistoixia me ta channels_serviced - get(0)-->lista subs tou iasonas
    public ArrayList<String> hashtags_serviced;
    public  ArrayList< ArrayList<VideoFile>> videos_hash;
    public String temp_name_of_channel,temporary;
    public VideoFile last_video;
    
    

    public Broker(Broker br){
        this.registeredUsers = br.registeredUsers;
        this.registeredPublishers = br.registeredPublishers;
        this.serverSocket = br.serverSocket;
        this.clientSocket = br.clientSocket;
        this.serverSocket_forvideos = br.serverSocket_forvideos;
        this.out = br.out;
        this.in = br.in;
        this.number_of_thread = br.number_of_thread;
        this.port = br.port;
        this.publisher_port = br.publisher_port;
        this.publisherServerSocket = br.publisherServerSocket;
        this.channels_serviced = new ArrayList<ChannelName>(br.channels_serviced);
        this.hashtags_serviced = new ArrayList<String>(br.hashtags_serviced);
        this.subscribers = new ArrayList< ArrayList<Consumer> >(br.subscribers);
        this.subscribers_p = new ArrayList< ArrayList<Publisher> >(br.subscribers_p);
        this.videos_hash = new ArrayList< ArrayList<VideoFile>> (br.videos_hash);
        this.last_video= br.last_video;
        this.temp_name_of_channel= br.temp_name_of_channel;
        this.temporary=br.temporary;
    }

    public Broker(){
        this.channels_serviced = new ArrayList<ChannelName>();
        this.hashtags_serviced = new ArrayList<String>();
        this.subscribers = new ArrayList< ArrayList<Consumer> >();
        this.subscribers_p = new ArrayList< ArrayList<Publisher> >();
        this.videos_hash = new ArrayList< ArrayList<VideoFile>> ();
    }

    public Broker(int p){
        port = p;
        this.channels_serviced = new ArrayList<ChannelName>();
        this.hashtags_serviced = new ArrayList<String>();
        this.subscribers = new ArrayList< ArrayList<Consumer> >();
        this.subscribers_p = new ArrayList< ArrayList<Publisher> >();
        this.videos_hash = new ArrayList< ArrayList<VideoFile>> ();
    }

    public Broker(int p,int port_p){
        port = p;
        publisher_port = port_p;
        this.channels_serviced = new ArrayList<ChannelName>();
        this.hashtags_serviced = new ArrayList<String>();
        this.subscribers = new ArrayList< ArrayList<Consumer> >();
        this.subscribers_p = new ArrayList< ArrayList<Publisher> >();
        this.videos_hash = new ArrayList< ArrayList<VideoFile>> ();
    }

    /*
    public int get_Broker_(){
        return number_of_thread;
    }
    */
    //--------------------------------------------------------------------

    // vazoume me thn seira 6666,6667,...
    // 

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

    public void share_to_subs(String path){
        try{

            //File myFile = new File (path);

            //if ((int)myFile.length() != 0) {

                int thesi_broker = this.thesi_broker_hash(last_channel_that_uploaded.get(0));



                int thesi =-1;

                for (int i=0;i<brokers.get(thesi_broker).channels_serviced.size();i++){

                    if(brokers.get(thesi_broker).channels_serviced.get(i).getChannelName().equals(last_channel_that_uploaded.get(0))){ // kanali me to teleytaio upload

                        thesi = i;

                    }

                }

                System.out.println("thesi : "+thesi);

                if(thesi != -1 ){

                    for(int i=0; i<brokers.get(thesi_broker).subscribers.get(thesi).size();i++){ // get(thesi) giati exoun 1-1 antistoixia me ta channel names 

                        brokers.get(thesi_broker).subscribers.get(thesi).get(i).video_name_temp = last_video_search.get(0).getName();

                        brokers.get(thesi_broker).subscribers.get(thesi).get(i).connect(000);

                    }

                    //Share with all publisher-Consumers that are subscribed also

    

                    for(int i=0; i<brokers.get(thesi_broker).subscribers_p.get(thesi).size();i++){ // get(thesi) giati exoun 1-1 antistoixia me ta channel names 

                        brokers.get(thesi_broker).subscribers_p.get(thesi).get(i).video_name_temp = last_video_search.get(0).getName();//last_video.getName();

                        brokers.get(thesi_broker).subscribers_p.get(thesi).get(i).download();

                    }

                }

        }
        catch(Exception e)
        {
            System.out.println("Problem in sharing to subs");
        }
    }

    public void publisherAcceptConnection(){
        //while(true){
            try{
                new Accept_Publisher_handlers(this, publisherServerSocket, messagesServerSocket_p, publisher_port, number_of_thread_p, registeredPublishers).start();
                new Accept_Publisher_handlers_videos(this, serverSocket_p_forvideos, (publisher_port-1000), number_of_thread_p, registeredPublishers).start();
            }
            catch(Exception e){
                System.out.println("exception ston publisher");
            }
        //}
    }

    public void acceptConnection(){ //parametros : Consumer c - type : Consumer
        try{
            //Socket clSocket = serverSocket.accept();
            /*
            //int thesi = number_of_clients.get(0) % brokers.size();
            //System.out.println("Server that accepted the connection : "+thesi);
            //brokers.get(thesi).registeredUsers.add(new Consumer(clSocket, this)); // kai alles metavlites
            //brokers.get(thesi).clientSocket = clSocket;
            System.out.println(port);
            number_of_clients.set(0, number_of_clients.get(0)+1);
            Consumer temp = new Consumer(clSocket,number_of_thread+1,port);
            temp.setBroker(this);
            registeredUsers.add(new Consumer(temp));
            System.out.println(registeredUsers.size());
            new Consumer_handlers(clSocket,number_of_thread++,port).start();*/
            Accept_Consumer_handlers con_handler = new Accept_Consumer_handlers(this, serverSocket,messagesServerSocket, port, number_of_thread, registeredUsers,registeredPublishers);
            con_handler.start();
            new Accept_Consumer_handlers_videos(this, serverSocket_forvideos, port+3000, number_of_thread, registeredUsers,registeredPublishers).start();
        }
        catch(Exception e){
            System.out.println("exception ston consumer");
        }
    }

    public void notifyPublisher(String str){

    }
    public void notifyBrokersOnChanges(){

    }
    public void pull(String str){

    }
    public void filterConsumers(String str){

    }
    
    //Node methods
    
    public void init(int port){
        try{
            serverSocket = new ServerSocket(port);
            serverSocket_forvideos = new ServerSocket(port+3000);//9666
        }
        catch(Exception e){

        }

        registeredUsers = new ArrayList<Consumer>();
    }

    public void init2(int port_pub){
        try{
            publisherServerSocket = new ServerSocket(port_pub);
            serverSocket_p_forvideos = new ServerSocket(port_pub-1000);//4666
        }
        catch(Exception e){

        }
        registeredPublishers = new ArrayList<Publisher>();
    }

    public void init3(int port_messages){
        try{
            messagesServerSocket = new ServerSocket(port_messages);
            messagesServerSocket_p = new ServerSocket(port_messages+1000);
        }
        catch(Exception e){

        }
        //registeredPublishers = new ArrayList<Publisher>();
    }

    public void connect(int xyz){

    }
    public void disconnect(int id_client){
        for(int i=0;i<registeredUsers.size();i++){
            if(registeredUsers.get(i).id == id_client){
                registeredUsers.remove(i);
                break;
            }
        }
    }

    public void disconnect_p(int id_client){
        for(int i=0;i<registeredPublishers.size();i++){
            if(registeredPublishers.get(i).id == id_client){
                registeredPublishers.remove(i);
                break;
            }
        }
    }

    public void updateNodes(){

    }
    public List<Broker> getBrokers(){
        return brokers;
    }

    public boolean isRegistered(int id_client) {
        for(int i=0;i<registeredUsers.size();i++){
            if(registeredUsers.get(i).id == id_client){
                return true;
            }
        }
        return false;
    }

    public boolean isRegistered_p(int id_client) {
        for(int i=0;i<registeredPublishers.size();i++){
            if(registeredPublishers.get(i).id == id_client){
                return true;
            }
        }
        return false;
    }

    public void run(){
        try{
            //for (int j = 0; j < 3; j++) {
                //Broker server = new Broker(port);
                //server.init(port);
                //server.acceptConnection();
                
                this.init(port);
                System.out.println("Broker's consumer port: "+port);
                System.out.println("Broker's consumer port for sending videos: "+(port+3000));
                this.init2(publisher_port);
                this.init3(port+1000);
                System.out.println("Broker's publisher port: "+publisher_port);
                System.out.println("Broker's publisher port for videos: "+(publisher_port-1000));
                System.out.println("Broker's message port for consumers: "+(port+1000));
                System.out.println("Broker's message port for publishers: "+(port+2000));
                /*Broker temp = new Broker(this);
                temp.publisherAcceptConnection();*/
                this.acceptConnection(); 
                this.publisherAcceptConnection();
                System.out.println("-----------------aaaaaaaaaaaaaaaaaaaaaaaaaaaa-----------------");
                //System.out.println("Number of clients : "+number_of_clients.get(0));
            //}

            //System.out.println(brokers);
            
        }
        catch(Exception e)
        {

        }
    }

    //handler

    private static class Consumer_handlers extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private int id;
        private int pport;
        private Broker broker;
        private ArrayList<ChannelName> channels = new ArrayList<ChannelName>();
        private List<Consumer> sub;
        private Consumer c;
        private List<Publisher> publisher_list;

        public Consumer_handlers(Socket socket, int num, int p, Broker b, List<Consumer> registers, List<Publisher> publisher) {//, Consumer temp
            this.clientSocket = socket;
            id = num;
            pport = p;
            broker = b;
            sub = registers;
            publisher_list = publisher;
            //c = temp;
        }



        public void run() {       
            
            //------------------------------------------------------------------------------------------------dokimastiko
            

            String str;
            //BufferedReader reader = new BufferedReader(
            //    new InputStreamReader(System.in));
            int hashed=0;
            int hashed_key =0;

            
            try{
                System.out.println("After 15 sec sleep");
                //VideoFile new_video = publisher_list.get(0).getChannel().get_last_video();
                //VideoFile new_video = broker.last_video;
                VideoFile new_video = last_video_search.get(0);
                System.out.println("Video downloading to clients : "+ new_video.getName());
                String video_file_to_send = new_video.getPath();
                System.out.println("path : "+video_file_to_send);
                //System.out.println(new_video.getHashtags());
                // send file
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
                os = clientSocket.getOutputStream();
                while((int)myFile.length() > pointer_in_file){
                    if( (pointer_in_file + 100000) > (int)myFile.length() ){
                        chunk = (int)myFile.length()%100000;
                        //System.out.println("The chunk is : "+chunk);
                        //if(chunk < 100000) break;
                    }
                    byte [] mybytearray  = new byte [chunk+3];
                    //System.out.println("Length : "+mybytearray.length);
                    String k = "end";
                    byte[] b = k.getBytes();
                    //System.out.println(b.length);
                    mybytearray[mybytearray.length - 3] = b[0];
                    mybytearray[mybytearray.length  - 2] = b[1];
                    mybytearray[mybytearray.length  - 1] = b[2];

                    
                    //bis.read(mybytearray,pointer_in_file,pointer_in_file+chunk); //chunk + 3?? thelo to pointer in file?
                    for(int i=pointer_in_file;i < (pointer_in_file+chunk);i++){
                        mybytearray[i-pointer_in_file] = allfile[i];
                    }
                    
                    System.out.println("Sending " + video_file_to_send + "(" + (mybytearray.length -3) + "bytes) part :"+(pointer_in_file/100000 +1 ) );    
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    System.out.println("Done sending.");
                    pointer_in_file += 100000;
                    //System.out.println(pointer_in_file);                    
                }
                fis.close();
                bis.close();
                os.close();

                System.out.println("File total size is : "+(int)myFile.length());

               
                /*
                byte [] mybytearray  = new byte [(int)myFile.length()+3];

                System.out.println("ok1.");
                String k = "end";
                byte[] b = k.getBytes();
                //System.out.println(b.length);
                mybytearray[mybytearray.length - 3] = b[0];
                mybytearray[mybytearray.length - 2] = b[1];
                mybytearray[mybytearray.length - 1] = b[2];


                System.out.println(mybytearray[mybytearray.length - 3]);
                System.out.println(mybytearray[mybytearray.length - 2]);
                System.out.println(mybytearray[mybytearray.length - 1]);

                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray,0,mybytearray.length);
                os = clientSocket.getOutputStream();
                System.out.println("Sending " + video_file_to_send + "(" + (mybytearray.length -3) + "bytes)");    
                os.write(mybytearray,0,mybytearray.length);
                os.flush();
                System.out.println("Done.");
                */

                
                //end new -----------------------------------------------------------------------

                /*
                try{
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    System.out.println("Starting to talk with consumer : "+id+" and broker : "+ pport);
                    //registeredUsers.add(id);
                    String greeting = in.readLine();
                    while (!greeting.equals(".")) {
                        System.out.println("Client "+id+" said to broker "+ pport +" : "+greeting);
                        str =  reader.readLine();
                        out.println("Response to "+greeting+", "+str + " message to "+id);
                        greeting = in.readLine();
                    }
                    
                    in.close();
                    out.close();
                    clientSocket.close();     
                }
                catch(Exception e){
    
                } 
                */  
                
            }
            catch(Exception e){
    
            }   
            
            //------------------------------------------------------------------------------------------------dokimastiko-telos                     
        }
    }

    //=================================================================================================
    private static class Publisher_handlers extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private int id;
        private int pport;
        private Broker broker;
        private ArrayList<ChannelName> channels = new ArrayList<ChannelName>();
        private List<Publisher> sub;
        private Publisher c;

        public Publisher_handlers(Socket socket,int num,int p, Broker b, List<Publisher> registers) {//, Consumer temp
            this.clientSocket = socket;
            id = num;
            pport = p;
            broker = b;
            sub = registers;
        }

        public void run() {           
            //System.out.println("Starting to receive video from publisher");
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
            try{
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                LocalDateTime now = LocalDateTime.now();
                String tempstr = (String)dtf.format(now).replace("/", "");
                tempstr = tempstr.replace(":", "");
                int file_size = 100003;
                int bytesRead;
                int current = 0;
                InputStream is = clientSocket.getInputStream();
                byte [] mybytearray  = new byte [file_size];
                byte [] to_mp4_full  = new byte [10000000];
                int pointer = 0;
                String video_file  = "temporary"+tempstr+".mp4"; // to onoma pou tha xrisimopoihsoyme gia na to grapsoume
                System.out.println("TO TEMP STR EINAI : "+ video_file);
                for(int i=0; i<broker.channels_serviced.size();i++){
                    if(broker.last_video.getChannelName().getChannelName().equals(broker.channels_serviced.get(i).getChannelName())){
                        broker.last_video.path_in_broker = video_file;
                    }
                }
                fos = new FileOutputStream(video_file);
                bos = new BufferedOutputStream(fos);
                bos.flush();
                //System.out.println("Before for");
                while (true){
                    mybytearray  = new byte [file_size];
                    
                    try{
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        
                        current = 0;
                        
                        do {
                            if(w1.equals(new Byte(mybytearray[current]))){
                                if(w2.equals(new Byte(mybytearray[current+1]))){
                                    if(w3.equals(new Byte(mybytearray[current+2]))){
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
                //System.out.println("after for");
                bos.write(to_mp4_full, 0 , pointer);
                bos.flush();
                System.out.println("File " + video_file
                    + " downloaded (" + pointer + " bytes read) --> temporary");
              
                is.close();
                fos.close();
                bos.close();

                is=null;
                fos=null;
                bos=null;

                System.out.println("Closing readers");
                System.out.println("TO TEMP STR EINAI2 : "+ video_file);
                broker.share_to_subs(video_file); //<--------------------------------------------------------------------------------------
                return;
            }
            catch (Exception e){
                System.out.println("Exception in connect in consumer.");
            }
        }
    }

    //==============================================================================================

    private static class Consumer_handlers_messages extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private int id;
        private int pport;
        private Broker broker;
        private ArrayList<ChannelName> channels = new ArrayList<ChannelName>();
        private List<Consumer> sub;
        private Consumer c;

        public Consumer_handlers_messages(Socket socket, int num, int p, Broker b, List<Consumer> registers, Consumer temp) {//, Consumer temp
            this.clientSocket = socket;
            id = num;
            pport = p;
            broker = b;
            sub = registers;
            c = temp;
        }

        public void run() {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
            String str,response_for_subscribe;
            int hashed,hashed_key;
            boolean theflag;
            try{
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Starting to talk with consumer : "+id+" and broker : "+ pport);
                //registeredUsers.add(id);
                out.println("Server's stream working");
                String greeting = in.readLine();
                while (!greeting.equals(".")) {
                    if(greeting.equals("subscribe")){
                        out.println("Available channels : "+allChannels.size());
                        for(ChannelName x : allChannels){
                            out.println(x.getChannelName());
                        }
                        response_for_subscribe = in.readLine();

                        // see where is the name with hash

                        //System.out.println("flag 1");

                        //------------------------------------------------------------------------------------------------------------------
                        hashed_key=0;
                        //hashed_key = broker.thesi_broker_hash(response_for_subscribe);
                        for(int k=0;k<brokers.size();k++){
                            for(int i=0;i<brokers.get(k).registeredPublishers.size();i++){
                                if(brokers.get(k).registeredPublishers.get(i).channelName.getChannelName().equals(response_for_subscribe)){
                                    hashed_key = k;
                                }
                            }
                        }

                        /*
                        String hashed_chName = (broker.calculateKeys(response_for_subscribe));
                        int len = hashed_chName.length();
                        String test2 = hashed_chName.substring(len-3);
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
                        hashed_key = hashed % brokers.size();    // h thesi ston pinaka brokers ston opoio einai to zitoymeno channel
                        */

                        //------------------------------------------------------------------------------------------------------------------
                        //System.out.println("flag 4");
                        //System.out.println(brokers.get(hashed_key).channels_serviced.size());
                        //System.out.println(brokers.get(hashed_key).subscribers.size());
                        // ara to kanali tha einai ston broker sthn thesi hashed_key in brokers table
                        boolean flag = false, flag_c = true;
                        for ( int i =0; i < brokers.get(hashed_key).channels_serviced.size() ; i++ ){
                            if ( brokers.get(hashed_key).channels_serviced.get(i).getChannelName().equals(response_for_subscribe) ){
                                
                                for(Consumer cons :  brokers.get(hashed_key).subscribers.get(i) ){
                                    if(cons.port == c.port && cons.id == c.id){
                                        flag_c = false;
                                        break;
                                    }
                                }
                                if (flag_c) {
                                    brokers.get(hashed_key).subscribers.get(i).add(c); 
                                    // adding consumer to subscribers that have 1-1 match with channels serviced
                                    // flag true means that it found the channel
                                    flag = true;
                                    System.out.println("Channel found");
    
                                    //System.out.println(brokers.get(hashed_key).subscribers.get(i).get( brokers.get(hashed_key).subscribers.get(i).size() -1 ).port );
                                    System.out.println("Subscriber list size for channel '"+brokers.get(hashed_key).channels_serviced.get(i).getChannelName()+"' is : "+brokers.get(hashed_key).subscribers.get(i).size());
                                    System.out.println("Added to broker : "+hashed_key);
                                }
                            }
                        }
                        if (!flag) {
                            if(!flag_c) out.println("Already subscribed!");
                            else out.println("Channel not found!");
                        }
                        else out.println("Subscription complete!");
                        //System.out.println("flag 5");
                    }
                    else if(greeting.equals("disconnect")){
                        broker.disconnect(c.id);
                        int thesi =0;
                        for(int l=0;l<brokers.size();l++){
                            if(broker.port == brokers.get(l).port) thesi = l;
                        }
                        System.out.println("Registered users left in broker "+thesi+" : "+broker.registeredUsers.size());
                    }
                    else if(greeting.equals("search")){
                        ArrayList<VideoFile> vids= new ArrayList<VideoFile>();
                        hashed_key = -1;
                        theflag = false;
                        if(broker.isRegistered(c.id)){
                            out.println("Want to search by Channel name or hashtag ?");
                            greeting = in.readLine();
                            if(greeting.contains("name") || greeting.contains("Name") || greeting.contains("channel") || greeting.contains("Channel")){
                                
                                out.println("Available channels : "+allChannels.size());
                                for(ChannelName x : allChannels){
                                    out.println(x.getChannelName());
                                }

                                // search by channel name
                                out.println("Give name : ");
                                greeting = in.readLine(); // den exoume kanei elegxo oti den yparxei to name i to hashtag
                                //hashed_key = broker.thesi_broker_hash(greeting);
                                for(int k=0;k<brokers.size();k++){
                                    for(int i=0;i<brokers.get(k).registeredPublishers.size();i++){
                                        if(brokers.get(k).registeredPublishers.get(i).channelName.getChannelName().equals(greeting)){
                                            hashed_key = k;
                                        }
                                    }
                                }
                                //System.out.println("hashed key for : "+greeting+" is : "+hashed_key);
                                for(ChannelName x : brokers.get(hashed_key).channels_serviced){
                                    if(x.getChannelName().equals(greeting)){
                                        // thelei na dei ola ta video apo ena channel
                                        // stelnoume to plithos twn video
                                        vids = new ArrayList<VideoFile>(x.getAllVideos());
                                        out.println(vids.size());
                                        for(int i =0; i< vids.size() ;i++){
                                            out.println(vids.get(i).getName());
                                        }
                                        theflag = true;
                                        break;
                                    }
                                }
                                
                                if(!theflag) {
                                    out.println("0"); // give size = 0
                                    out.println("Not found");
                                }
                                else{ 
                                    out.println("Make your choice :");
                                    greeting = in.readLine(); // get choice from consumer
                                    // to opoio einai to onoma tou video
                                    for(int i =0; i< vids.size() ;i++){
                                        if(vids.get(i).getName().equals(greeting)){
                                            //vids.get(i).path_in_broker
                                            //if(!vids.get(i).getName().equals(brokers.get(hashed_key).last_video.getName())){
                                                brokers.get(hashed_key).last_video = vids.get(i);
                                                last_video_search.set(0,vids.get(i));
                                            //}
                                            System.out.println("before");
                                            new Accept_Consumer_handlers_videos(brokers.get(hashed_key), brokers.get(hashed_key).serverSocket_forvideos, ((brokers.get(hashed_key).port)+3000), brokers.get(hashed_key).number_of_thread, brokers.get(hashed_key).registeredUsers, brokers.get(hashed_key).registeredPublishers).start();
                                            Thread.sleep(3000);
                                            System.out.println("after");
                                            c.connect2(greeting);
                                        }
                                    }
                                    
                                }
                                
                            }
                            else if(greeting.contains("hashtag") || greeting.contains("Hashtag")) {
                                // search by hashtag 

                                //give size
                                out.println(brokers.size());
                                for (int i=0;i<brokers.size();i++){
                                    out.println(brokers.get(i).hashtags_serviced.size());
                                    for (int j=0;j<brokers.get(i).hashtags_serviced.size(); j++){
                                        out.println(brokers.get(i).hashtags_serviced.get(j));  
                                    }
                                }
                                
                                out.println("Give hashtag : ( in form #name_of_hashtag )");
                                greeting = in.readLine();
                                hashed_key  = broker.thesi_broker_hash(greeting);
                                //System.out.println("hashed key for : "+greeting+" is : "+hashed_key);
                                for(String x : brokers.get(hashed_key).hashtags_serviced){
                                    if(x.equals(greeting)){
                                        out.println("found");
                                        theflag = true;
                                        break;
                                    }
                                }
                                if(!theflag) {
                                    out.println("Not found");
                                }else{
                                    System.out.println(brokers.get(hashed_key).hashtags_serviced.size());
                                    System.out.println(brokers.get(hashed_key).videos_hash.size());
                                    System.out.println(brokers.get(hashed_key).videos_hash.get(0).size());
                                    
                                    int thesi_hash=-1;
                                    for(int i=0;i<brokers.get(hashed_key).hashtags_serviced.size();i++){
                                        if(brokers.get(hashed_key).hashtags_serviced.get(i).equals(greeting)){
                                            thesi_hash=i;
                                        } 
                                    }
                                    if(thesi_hash!=-1){
                                        out.println(brokers.get(hashed_key).videos_hash.get(thesi_hash).size());
                                        for(int i=0;i<brokers.get(hashed_key).videos_hash.get(thesi_hash).size();i++){
                                            out.println(brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i).getName()); 
                                        }
                                        out.println("Choose video: ");
                                        greeting = in.readLine();
                                        
                                        System.out.println(brokers.get(hashed_key).videos_hash.get(thesi_hash).size());
                                        for(int i=0;i<brokers.get(hashed_key).videos_hash.get(thesi_hash).size();i++){
                                            System.out.println("compare : "+brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i).getName()+" with "+greeting);
                                            if(brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i).getName().equals(greeting)){
                                                
                                                //if(!brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i).getName().equals(broker.last_video.getName())){
                                                    brokers.get(hashed_key).last_video = brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i);
                                                    last_video_search.set(0,brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i));
                                                //}
                                                System.out.println("before");
                                                new Accept_Consumer_handlers_videos(brokers.get(hashed_key), brokers.get(hashed_key).serverSocket_forvideos, ((brokers.get(hashed_key).port)+3000), brokers.get(hashed_key).number_of_thread, brokers.get(hashed_key).registeredUsers, brokers.get(hashed_key).registeredPublishers).start();
                                                Thread.sleep(3000);
                                                System.out.println("after");
                                                c.connect2(greeting);
                                            }
                                        }

                                    }else{
                                        System.out.println("No video available");
                                    }
                                }
                            }
                            else {
                                out.println("Incorrect input!");
                            }
                        }
                        else{
                            // he is not registered : will see what to do
                        }
                    }
                    else{
                        System.out.println("Client with id : "+id+", said to broker "+ pport +" : "+greeting);
                        str =  reader.readLine();
                        out.println("Response to "+greeting+", "+str + " message to client with id : "+id);
                    }
                    greeting = in.readLine();
                }
                reader.close();
                out.close();
                in.close();
                clientSocket.close();    
            }
            catch(Exception e){
    
            }                         
        }
    }

    private static class Accept_Consumer_handlers extends Thread {
        private Broker broker;
        private ServerSocket serverSocket;
        private int port;
        private int number_of_thread = 1;
        private List<Consumer> registeredUsers;
        private List<Publisher> registeredPublishers;
        private ServerSocket s;


        public Accept_Consumer_handlers(Broker b, ServerSocket socket, ServerSocket s2, int p, int nof, List<Consumer> registers, List<Publisher> r) {
            this.broker = b;
            this.serverSocket = socket;
            this.port = p;
            this.s=s2;
            this.number_of_thread = nof;
            this.registeredUsers = registers;
            this.registeredPublishers =r;
        }

        public void run(){
                try{
                    while (true){
                        System.out.println("Waiting to accept connection");
                        Socket x=serverSocket.accept();
                        //System.out.println("                                consumer accept");
                        //int thesi = number_of_clients.get(0) % brokers.size();
                        //System.out.println("Server that accepted the connection : "+thesi);
                        //brokers.get(thesi).registeredUsers.add(new Consumer(clSocket, this)); // kai alles metavlites
                        //brokers.get(thesi).clientSocket = clSocket;
                        //System.out.println(port);
                        number_of_clients.set(0, number_of_clients.get(0)+1);
                        Consumer temp = new Consumer(x,number_of_thread,port);
                        temp.setBroker(broker);
                        registeredUsers.add(new Consumer(temp));
                        //System.out.println("number of consumers : "+registeredUsers.size());
                        //new Consumer_handlers(x,number_of_thread,port,broker,registeredUsers,registeredPublishers).start();//,registeredUsers.get(registeredUsers.size()-1) //<-------------------------------------- that
                        System.out.println("Before consumer messages");
                        Socket x2= s.accept();
                        System.out.println("Accept consumer messages socket");
                        new Consumer_handlers_messages(x2,number_of_thread,port+1000,broker,registeredUsers,registeredUsers.get(registeredUsers.size()-1)).start();
                        number_of_thread +=1;
                    }
                }
                catch(Exception e){
                }
            
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class Accept_Consumer_handlers_videos extends Thread {
        private Broker broker;
        private ServerSocket serverSocket;
        private int port;
        private int number_of_thread = 1;
        private List<Consumer> registeredUsers;
        private List<Publisher> registeredPublishers;


        public Accept_Consumer_handlers_videos(Broker b, ServerSocket socket, int p, int nof, List<Consumer> registers, List<Publisher> r) {
            this.broker = b;
            this.serverSocket = socket;
            this.port = p;
            this.number_of_thread = nof;
            this.registeredUsers = registers;
            this.registeredPublishers =r;
        }
        
        public void run(){
                try{
                    while (true){
                        Socket x=serverSocket.accept();
                        new Consumer_handlers(x,number_of_thread,port,broker,registeredUsers,registeredPublishers).start();//,registeredUsers.get(registeredUsers.size()-1) //<-------------------------------------- that
                        number_of_thread+=1;
                    }
                }
                catch(Exception e){
                }
            
        }
    }

    //============================================================================
    private static class Accept_Publisher_handlers extends Thread {
        private Broker broker;
        private ServerSocket serverSocket;
        private int port;
        private int number_of_thread = 1;
        private List<Publisher> registeredPublishers;
        private ServerSocket s;


        public Accept_Publisher_handlers(Broker b, ServerSocket socket,ServerSocket s2, int p, int nof, List<Publisher> registers) {
            this.broker = b;
            this.serverSocket = socket;
            this.port = p;
            this.s=s2;
            this.number_of_thread = nof;
            this.registeredPublishers = registers;
        }

        public void run(){
            //boolean initialize_messages = false;
                    
            try{
                while (true){
                    Socket x=serverSocket.accept();
                    System.out.println("Socket accepted: " + port);
                    number_of_publishers.set(0, number_of_publishers.get(0)+1);
                    String test = "default_name";
                    Publisher temp = new Publisher(x,number_of_thread,port,test);
                    temp.setBroker(broker);
                    //registeredPublishers.add(new Publisher(temp));
                    System.out.println("Now publishers size is -------------- "+registeredPublishers.size());
                    Thread.sleep(2000); // ypo synthiki genika milwntas alla problima gia meta
                    //System.out.println("Prin thn apothikeysh toy video ");
                    //new Publisher_handlers(x,number_of_thread,port,broker,registeredPublishers).start();//,registeredUsers.get(registeredUsers.size()-1) //<-------------------------------------- that
                    //Thread.sleep(2000);
                    //System.out.println("Prin ta minimata me ton publisher1");
                    Socket x2= s.accept();
                    //initialize_messages = true;
                    
                    //System.out.println("Prin ta minimata me ton publisher2");
                    new Publisher_handlers_messages(x2,number_of_thread,port+2000,broker,registeredPublishers,temp).start();
                    number_of_thread +=1;
                }
            }
            catch(Exception e){
                System.out.println("Exception in thread initialization publisher");
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static class Accept_Publisher_handlers_videos extends Thread {
        private Broker broker;
        private ServerSocket serverSocket;
        private int port;
        private int number_of_thread = 1;
        private List<Publisher> registeredPublishers;


        public Accept_Publisher_handlers_videos(Broker b, ServerSocket socket, int p, int nof, List<Publisher> registers) {
            this.broker = b;
            this.serverSocket = socket;
            this.port = p;
            this.number_of_thread = nof;
            this.registeredPublishers = registers;
        }

        public void run(){
            try{
                while (true){
                    Socket x=serverSocket.accept();
                    new Publisher_handlers(x,number_of_thread,port,broker,registeredPublishers).start();
                    number_of_thread+=1;
                }
            }
            catch(Exception e){
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static class Publisher_handlers_messages extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private int id;
        private int pport;
        private Broker broker;
        private ArrayList<ChannelName> channels = new ArrayList<ChannelName>();
        private List<Publisher> sub;
        private Publisher c;

        public Publisher_handlers_messages(Socket socket, int num, int p, Broker b, List<Publisher> registers, Publisher p_temp) {//, Consumer temp
            this.clientSocket = socket;
            id = num;
            pport = p;
            broker = b;
            sub = registers;
            c = p_temp;
        }

        public void run() {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
            String str,response_for_subscribe;
            int hashed,hashed_key;
            boolean theflag;
            try{
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Starting to talk with publisher : "+id+" and broker : "+ pport);
                out.println("Server's stream working");
                String greeting = in.readLine();
                while (!greeting.equals(".")) {
                    if(greeting.equals("subscribe")){
                        out.println("Available channels : "+allChannels.size());
                        for(ChannelName x : allChannels){
                            out.println(x.getChannelName());
                        }
                        response_for_subscribe = in.readLine();
                        hashed_key =0;
                        //hashed_key = broker.thesi_broker_hash(response_for_subscribe);
                        for(int k=0;k<brokers.size();k++){
                            for(int i=0;i<brokers.get(k).registeredPublishers.size();i++){
                                if(brokers.get(k).registeredPublishers.get(i).channelName.getChannelName().equals(response_for_subscribe)){
                                    hashed_key = k;
                                }
                            }
                        }
                        boolean flag = false, flag_c = true;
                        for ( int i =0; i < brokers.get(hashed_key).channels_serviced.size() ; i++ ){
                            if ( brokers.get(hashed_key).channels_serviced.get(i).getChannelName().equals(response_for_subscribe) ){
                                
                                for(Publisher cons :  brokers.get(hashed_key).subscribers_p.get(i) ){
                                    if(cons.port == c.port && cons.id == c.id){
                                        flag_c = false;
                                        break;
                                    }
                                }
                                if (flag_c) {
                                    brokers.get(hashed_key).subscribers_p.get(i).add(c);
                                    flag = true;
                                    System.out.println("Channel found");
                                    System.out.println("Subscriber list size for channel '"+brokers.get(hashed_key).channels_serviced.get(i).getChannelName()+"' is : "+brokers.get(hashed_key).subscribers_p.get(i).size());
                                    System.out.println("Added to broker : "+hashed_key);
                                }
                            }
                        }
                        if (!flag) {
                            if(!flag_c) out.println("Already subscribed!");
                            else out.println("Channel not found!");
                        }
                        else out.println("Subscription complete!");
                    }
                    else if(greeting.equals("disconnect")){
                        broker.disconnect_p(c.id);
                        int thesi =0;
                        for(int l=0;l<brokers.size();l++){
                            if(broker.port == brokers.get(l).port) thesi = l;
                        }
                        System.out.println("Registered publishers left in broker "+thesi+" : "+broker.registeredPublishers.size());
                    }
                    else if(greeting.equals("set channel name")){
                        out.println("Give channel name : ");
                        greeting = in.readLine();
                        c.channelName.setChannelName(greeting);
                        int thesi=broker.thesi_broker_hash(greeting);
                        brokers.get(thesi).registeredPublishers.add(c);
                    }
                    else if(greeting.equals("push")){
                        out.println("Give name : ");
                        greeting = in.readLine();
                        broker.temp_name_of_channel= c.getChannel().getChannelName();
                        last_channel_that_uploaded.set(0,c.getChannel().getChannelName());
                        out.println("Give the path for the video");
                        String path = in.readLine();
                        ArrayList<String> hash = new ArrayList<String>();
                        out.println("Give the hashes ('exit' to exit)");
                        while(true){
                            str =  in.readLine();
                            if(!str.equals("exit")) hash.add(str);
                            else break;
                        }
                        out.println("Start to push video ");
                        c.push(greeting,path,hash);
                    }
                    else if(greeting.equals("search")){
                        ArrayList<VideoFile> vids= new ArrayList<VideoFile>();
                        hashed_key = -1;
                        theflag = false;
                        boolean f = false;
                        if(broker.isRegistered_p(c.id)){
                            out.println("Want to search by Channel name or hashtag ?");
                            greeting = in.readLine();
                            if(greeting.contains("name") || greeting.contains("Name") || greeting.contains("channel") || greeting.contains("Channel")){
                                
                                out.println("Available channels : "+allChannels.size());
                                for(ChannelName x : allChannels){
                                    out.println(x.getChannelName());
                                }

                                // search by channel name
                                out.println("Give name : ");
                                greeting = in.readLine(); // den exoume kanei elegxo oti den yparxei to name i to hashtag
                                //hashed_key  = broker.thesi_broker_hash(greeting);
                                for(int k=0;k<brokers.size();k++){
                                    for(int i=0;i<brokers.get(k).registeredPublishers.size();i++){
                                        if(brokers.get(k).registeredPublishers.get(i).channelName.getChannelName().equals(greeting)){
                                            hashed_key = k;
                                        }
                                    }
                                }
                                //System.out.println("hashed key for : "+greeting+" is : "+hashed_key);
                                for(ChannelName x : brokers.get(hashed_key).channels_serviced){
                                    if(x.getChannelName().equals(greeting)){
                                        // thelei na dei ola ta video apo ena channel
                                        // stelnoume to plithos twn video
                                        vids = new ArrayList<VideoFile>(x.getAllVideos());
                                        out.println(vids.size());
                                        for(int i =0; i< vids.size() ;i++){
                                            out.println(vids.get(i).getName());
                                        }
                                        theflag = true;
                                        break;
                                    }
                                }
                                
                                if(!theflag) {
                                    out.println("0"); // give size = 0
                                    out.println("Not found");
                                }
                                else{ 
                                    out.println("Make your choice :");
                                    greeting = in.readLine(); // get choice from consumer
                                    // to opoio einai to onoma tou video
                                    for(int i =0; i< vids.size() ;i++){
                                        if(vids.get(i).getName().equals(greeting)){
                                            //vids.get(i).path_in_broker
                                            //if(!vids.get(i).getName().equals(broker.last_video.getName())){
                                                brokers.get(hashed_key).last_video = vids.get(i);
                                                last_video_search.set(0,vids.get(i));
                                            //}
                                            System.out.println("before");
                                            new Accept_Consumer_handlers_videos(brokers.get(hashed_key), brokers.get(hashed_key).serverSocket_forvideos, ((brokers.get(hashed_key).port)+3000), brokers.get(hashed_key).number_of_thread, brokers.get(hashed_key).registeredUsers, brokers.get(hashed_key).registeredPublishers).start();
                                            Thread.sleep(3000);
                                            System.out.println("after");
                                            c.download2(greeting);
                                        }
                                    }
                                    
                                }
                                
                            }
                            else if(greeting.contains("hashtag") || greeting.contains("Hashtag")) {
                                // search by hashtag 

                                //give size
                                out.println(brokers.size());
                                for (int i=0;i<brokers.size();i++){
                                    out.println(brokers.get(i).hashtags_serviced.size());
                                    for (int j=0;j<brokers.get(i).hashtags_serviced.size(); j++){
                                        out.println(brokers.get(i).hashtags_serviced.get(j));  
                                    }
                                }
                                
                                out.println("Give hashtag : ( in form #name_of_hashtag )");
                                greeting = in.readLine();
                                hashed_key  = broker.thesi_broker_hash(greeting);
                                //System.out.println("hashed key for : "+greeting+" is : "+hashed_key);
                                for(String x : brokers.get(hashed_key).hashtags_serviced){
                                    if(x.equals(greeting)){
                                        out.println("found");
                                        theflag = true;
                                        break;
                                    }
                                }
                                if(!theflag) {
                                    out.println("Not found");
                                }else{
                                    System.out.println(brokers.get(hashed_key).hashtags_serviced.size());
                                    System.out.println(brokers.get(hashed_key).videos_hash.size());
                                    System.out.println(brokers.get(hashed_key).videos_hash.get(0).size());
                                    
                                    int thesi_hash=-1;
                                    for(int i=0;i<brokers.get(hashed_key).hashtags_serviced.size();i++){
                                        if(brokers.get(hashed_key).hashtags_serviced.get(i).equals(greeting)){
                                            thesi_hash=i;
                                        } 
                                    }
                                    if(thesi_hash!=-1){
                                        vids = new ArrayList<VideoFile>(c.channelName.getAllVideos());
                                        out.println(brokers.get(hashed_key).videos_hash.get(thesi_hash).size());
                                        for(int i=0;i<brokers.get(hashed_key).videos_hash.get(thesi_hash).size();i++){
                                            f=false;
                                            for(int l=0; l<vids.size();l++){
                                                if(vids.get(l).getName().equals(brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i).getName())){
                                                    f=true; // yparxei se ayton to video
                                                    break;
                                                }
                                            }
                                            if(!f){
                                                out.println(brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i).getName()); 
                                            }
                                            else{
                                                out.println("Invalid video(cannot watch)"); 
                                            }
                                        }
                                        out.println("Choose video (press enter if you don't want to choose any): ");
                                        greeting = in.readLine();
                                        if(!greeting.equals("")){
                                            for(int i=0;i<brokers.get(hashed_key).videos_hash.get(thesi_hash).size();i++){
                                                if(brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i).getName().equals(greeting)){
                                                    
                                                    //if(!brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i).getName().equals(broker.last_video.getName())){
                                                        brokers.get(hashed_key).last_video = brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i);
                                                        last_video_search.set(0,brokers.get(hashed_key).videos_hash.get(thesi_hash).get(i));
                                                    //}
                                                    System.out.println("before");
                                                    new Accept_Consumer_handlers_videos(brokers.get(hashed_key), brokers.get(hashed_key).serverSocket_forvideos, ((brokers.get(hashed_key).port)+3000), brokers.get(hashed_key).number_of_thread, brokers.get(hashed_key).registeredUsers, brokers.get(hashed_key).registeredPublishers).start();
                                                    Thread.sleep(3000);
                                                    System.out.println("after");
                                                    c.download2(greeting);
                                                }
                                            }
                                        }

                                    }else{
                                        System.out.println("No video available");
                                    }
                                }
                            }
                            else {
                                out.println("Incorrect input!");
                            }
                        }
                        else{
                            // he is not registered : will see what to do
                        }
                    }
                    else{
                        System.out.println("Publisher with id : "+id+", said to broker "+ pport +" : "+greeting);
                        str =  reader.readLine();
                        out.println("Response to "+greeting+", "+str + " message to publisher with id : "+id);
                    }
                    greeting = in.readLine();
                }
                reader.close();
                out.close();
                in.close();
                clientSocket.close();    
            }
            catch(Exception e){
    
            }                         
        }
    }


    /*
    private static class Accept_Publisher_handlers extends Thread {
        private ServerSocket publisherServerSocket;
        private int port;
        private int number_of_thread = 1;
        public List<Publisher> registeredPublishers;


        public Accept_Publisher_handlers(ServerSocket socket, int p, int nof, List<Publisher> registers) {
            this.publisherServerSocket = socket;
            this.port = p;
            this.number_of_thread = nof;
            this.registeredPublishers = registers;
        }

        public void run(){
            try{
                while(true){
                    publisherServerSocket.accept();
                    //System.out.println("                                publisher accept");
                    number_of_publishers.set(0, number_of_publishers.get(0)+1);
                    //System.out.println("NUMBER OF PUBLISHERS :"+number_of_publishers.get(0));
                }
                
            }
            catch(Exception e){
                System.out.println("exception ston gamwpublisher");
            }
        }
    }
    */


    public static void main(String args[]) {
        
        number_of_clients.add(0);
        number_of_publishers.add(0);
        VideoFile temp = new VideoFile();
        last_video_search.add(temp);
        last_channel_that_uploaded.add("");

        Broker b1 = new Broker(6666, 5666);
        b1.start();
        brokers.add(b1);
        

        Broker b2 = new Broker(6667,5667);
        b2.start();
        brokers.add(b2);



        Broker b3 = new Broker(6668,5668);
        b3.start();
        brokers.add(b3);

        /*
        Broker b4 = new Broker(6669,5669);
        b4.start();
        brokers.add(b4);

        */

        try{
            int x = Integer.parseInt(Inet4Address.getLocalHost().getHostAddress().replace(".", ""));  
            //System.out.println(x);
            String str="";
    
            
            for(int i=1; i<=brokers.size()-1;i++){
                //System.out.println("Inside.");
                str = Integer.toString(x+brokers.get(i-1).port);
                //System.out.println(x+brokers.get(i-1).port);
                delimiter_of_Brokers.add(brokers.get(i-1).calculateKeys(str));
                //delimiter_of_Brokers.add(b2.calculateKeys(str));
                //System.out.println(brokers.get(i-1).calculateKeys(str));
                //System.out.println(b2.calculateKeys(str));
            }
            
        }
        catch(Exception e){
            System.out.println("Problem.");
        }
        
       
        


        // an o broker poy kanei handle ena consumer den exei to hashtag poy thelei o consumer tote 
        // allazei to port ston consumer kai e3hpeireteitai apo allon
        
        /*
        Broker b2 = new Broker(6667);
        b2.start();

        Broker b3 = new Broker(6668);
        b3.start();
        */
        
    }
}