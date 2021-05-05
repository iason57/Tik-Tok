import java.util.*;
import java.util.concurrent.Flow.Subscriber;
import java.net.*;
import java.io.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Broker extends Thread implements Broker_interface,Node{

    public List<Consumer> registeredUsers;
    public List<Publisher> registeredPublishers;
    public ServerSocket serverSocket;
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
    
    

    public Broker(Broker br){
        this.registeredUsers = br.registeredUsers;
        this.registeredPublishers = br.registeredPublishers;
        this.serverSocket = br.serverSocket;
        this.clientSocket = br.clientSocket;
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
    }

    public Broker(){
        this.channels_serviced = new ArrayList<ChannelName>();
        this.hashtags_serviced = new ArrayList<String>();
        this.subscribers = new ArrayList< ArrayList<Consumer> >();
        this.subscribers_p = new ArrayList< ArrayList<Publisher> >();
    }

    public Broker(int p){
        port = p;
        this.channels_serviced = new ArrayList<ChannelName>();
        this.hashtags_serviced = new ArrayList<String>();
        this.subscribers = new ArrayList< ArrayList<Consumer> >();
        this.subscribers_p = new ArrayList< ArrayList<Publisher> >();
    }

    public Broker(int p,int port_p){
        port = p;
        publisher_port = port_p;
        this.channels_serviced = new ArrayList<ChannelName>();
        this.hashtags_serviced = new ArrayList<String>();
        this.subscribers = new ArrayList< ArrayList<Consumer> >();
        this.subscribers_p = new ArrayList< ArrayList<Publisher> >();
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

    public void publisherAcceptConnection(){
        //while(true){
            try{
                new Accept_Publisher_handlers(this, publisherServerSocket, messagesServerSocket_p, publisher_port, number_of_thread_p, registeredPublishers).start();
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
        }
        catch(Exception e){

        }

        registeredUsers = new ArrayList<Consumer>();
    }

    public void init2(int port_pub){
        try{
            publisherServerSocket = new ServerSocket(port_pub);
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
                this.init2(publisher_port);
                this.init3(port+1000);
                System.out.println("Broker's publisher port: "+publisher_port);
                System.out.println("Broker's message port for consumers: "+(port+1000));
                System.out.println("Broker's message port for publishers: "+(port+2000));
                /*Broker temp = new Broker(this);
                temp.publisherAcceptConnection();*/
                this.acceptConnection(); 
                this.publisherAcceptConnection();
                //System.out.println(publisher_port);
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

        public Consumer_handlers(Socket socket,int num,int p, Broker b, List<Consumer> registers,List<Publisher> publisher) {//, Consumer temp
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
                VideoFile new_video = publisher_list.get(0).getChannel().get_last_video();
                System.out.println("here : all vid size : "+publisher_list.get(0).getChannel().getAllVideos().size());
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

        public Consumer_handlers_messages(Socket socket,int num,int p, Broker b, List<Consumer> registers, Consumer temp) {//, Consumer temp
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
                        
                        hashed_key = broker.thesi_broker_hash(response_for_subscribe);

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
                        hashed_key = -1;
                        theflag = false;
                        if(broker.isRegistered(c.id)){
                            out.println("Want to search by Channel name or hashtag ?");
                            greeting = in.readLine();
                            if(greeting.contains("name") || greeting.contains("Name") || greeting.contains("channel") || greeting.contains("Channel")){
                                // search by channel name
                                out.println("Give name : ");
                                greeting = in.readLine(); // den exoume kanei elegxo oti den yparxei to name i to hashtag
                                hashed_key  = broker.thesi_broker_hash(greeting);
                                //System.out.println("hashed key for : "+greeting+" is : "+hashed_key);
                                for(ChannelName x : brokers.get(hashed_key).channels_serviced){
                                    if(x.getChannelName().equals(greeting)){
                                        out.println("kati tha stelnoyme");
                                        theflag = true;
                                        break;
                                    }
                                }
                                if(!theflag) out.println("Not found");
                                
                            }
                            else if(greeting.contains("hashtag") || greeting.contains("Hashtag")) {
                                // search by hashtag 
                                out.println("Give hashtag : ( in form #name_of_hashtag )");
                                greeting = in.readLine();
                                hashed_key  = broker.thesi_broker_hash(greeting);
                                //System.out.println("hashed key for : "+greeting+" is : "+hashed_key);
                                for(String x : brokers.get(hashed_key).hashtags_serviced){
                                    if(x.equals(greeting)){
                                        out.println("kati tha stelnoyme2");
                                        theflag = true;
                                        break;
                                    }
                                }
                                if(!theflag) out.println("Not found");
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


        public Accept_Consumer_handlers(Broker b, ServerSocket socket,ServerSocket s2, int p, int nof, List<Consumer> registers,List<Publisher> r) {
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
                        Thread.sleep(2000);
                        new Consumer_handlers(x,number_of_thread,port,broker,registeredUsers,registeredPublishers).start();//,registeredUsers.get(registeredUsers.size()-1) //<-------------------------------------- that
                        Thread.sleep(2000);
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
                    String test="problem";
                    Publisher temp = new Publisher(x,number_of_thread,port,test);
                    temp.setBroker(broker);
                    registeredPublishers.add(new Publisher(temp));
                    System.out.println("Now publishers size is -------------- "+registeredPublishers.size());
                    Thread.sleep(2000); // ypo synthiki genika milwntas alla problima gia meta
                    //System.out.println("Prin thn apothikeysh toy video ");
                    new Publisher_handlers(x,number_of_thread,port,broker,registeredPublishers).start();//,registeredUsers.get(registeredUsers.size()-1) //<-------------------------------------- that
                    //Thread.sleep(2000);
                    //System.out.println("Prin ta minimata me ton publisher1");
                    Socket x2= s.accept();
                    //initialize_messages = true;
                    
                    //System.out.println("Prin ta minimata me ton publisher2");
                    new Publisher_handlers_messages(x2,number_of_thread,port+2000,broker,registeredPublishers,registeredPublishers.get(registeredPublishers.size()-1)).start();
                    number_of_thread +=1;
                }
            }
            catch(Exception e){
                System.out.println("Exception in thread initialization publisher");
            }
        }
    }

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

        public Publisher_handlers_messages(Socket socket,int num,int p, Broker b, List<Publisher> registers, Publisher p_temp) {//, Consumer temp
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
                        
                        hashed_key = broker.thesi_broker_hash(response_for_subscribe);
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
                    else if(greeting.equals("push")){
                        out.println("Give name : ");
                        greeting = in.readLine();
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
                        hashed_key = -1;
                        theflag = false;
                        if(broker.isRegistered_p(c.id)){
                            out.println("Want to search by Channel name or hashtag ?");
                            greeting = in.readLine();
                            if(greeting.contains("name") || greeting.contains("Name") || greeting.contains("channel") || greeting.contains("Channel")){
                                out.println("Give name : ");
                                greeting = in.readLine();
                                hashed_key  = broker.thesi_broker_hash(greeting);
                                for(ChannelName x : brokers.get(hashed_key).channels_serviced){
                                    if(x.getChannelName().equals(greeting)){
                                        out.println("kati tha stelnoyme");
                                        theflag = true;
                                        break;
                                    }
                                }
                                if(!theflag) out.println("Not found");
                                
                            }
                            else if(greeting.contains("hashtag") || greeting.contains("Hashtag")) {
                                out.println("Give hashtag : ( in form #name_of_hashtag )");
                                greeting = in.readLine();
                                hashed_key  = broker.thesi_broker_hash(greeting);
                                for(String x : brokers.get(hashed_key).hashtags_serviced){
                                    if(x.equals(greeting)){
                                        out.println("kati tha stelnoyme2");
                                        theflag = true;
                                        break;
                                    }
                                }
                                if(!theflag) out.println("Not found");
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

        Broker b1 = new Broker(6666, 5666);
        b1.start();
        brokers.add(b1);
        /*

        Broker b2 = new Broker(6667,5667);
        b2.start();
        brokers.add(b2);

        Broker b3 = new Broker(6668,5668);
        b3.start();
        brokers.add(b3);

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