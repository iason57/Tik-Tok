import java.util.*;
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
    public int port;
    public int publisher_port;
    public ServerSocket publisherServerSocket;
    public ArrayList<String> channels_serviced;
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
        this.channels_serviced = new ArrayList<String>(br.channels_serviced);
        this.hashtags_serviced = new ArrayList<String>(br.hashtags_serviced);
    }

    public Broker(){
        this.channels_serviced = new ArrayList<String>();
        this.hashtags_serviced = new ArrayList<String>();
    }

    public Broker(int p){
        port = p;
        this.channels_serviced = new ArrayList<String>();
        this.hashtags_serviced = new ArrayList<String>();
    }

    public Broker(int p,int port_p){
        port = p;
        publisher_port = port_p;
        this.channels_serviced = new ArrayList<String>();
        this.hashtags_serviced = new ArrayList<String>();
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
    public void publisherAcceptConnection(){
        //while(true){
            try{
                //Socket pubSocket = publisherServerSocket.accept();
                new Accept_Publisher_handlers(publisherServerSocket, port, number_of_thread, registeredPublishers).start();
            }
            catch(Exception e){
                System.out.println("exception ston gamwpublisher");
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
                Accept_Consumer_handlers con_handler = new Accept_Consumer_handlers(this, serverSocket, port, number_of_thread, registeredUsers);
                con_handler.start();
            }
            catch(Exception e){
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

    public void connect(){

    }
    public void disconnect(int id_client){
        for(int i=0;i<registeredUsers.size();i++){
            if(registeredUsers.get(i).id == id_client){
                registeredUsers.remove(i);
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

    public void run(){
        try{
            //for (int j = 0; j < 3; j++) {
                //Broker server = new Broker(port);
                //server.init(port);
                //server.acceptConnection();
                
                this.init(port);
                System.out.println("Broker's consumer port: "+port);
                this.init2(publisher_port);
                System.out.println("Broker's publisher port: "+publisher_port);
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

        public Consumer_handlers(Socket socket,int num,int p, Broker b) {
            this.clientSocket = socket;
            id = num;
            pport = p;
            broker = b;
        }

        public void run() {           
            String str;
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
            

            //new ---------------------------------------------------------------------------
            
            try{
                /*
                dokimastika to ftiaxnoume edw --> douleia toy publisher
                */
                
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                LocalDateTime now = LocalDateTime.now();  
                //System.out.println(dtf.format(now)); 
                //--------------------------------------------------------------------
                ChannelName ch = new ChannelName("iasonas");
                channels.add(ch);
                ChannelName ch2 = new ChannelName("kauta podia elenas");
                channels.add(ch2);
                ChannelName ch3 = new ChannelName("myrwdia");
                channels.add(ch3);
                ChannelName ch4 = new ChannelName("vana");
                channels.add(ch4);
                ChannelName ch5 = new ChannelName("e");
                channels.add(ch5);
                ChannelName ch6 = new ChannelName("kal");
                channels.add(ch6);
                ArrayList<String> hash = new ArrayList<String>();
                hash.add("#sky");
                hash.add("#music");
                hash.add("#amazing");
                hash.add("#nofilterneeded");
                hash.add("#instapic");
                hash.add("#instapic");
                hash.add("#follow");
                VideoFile new_video = new VideoFile("peace",ch,(String)dtf.format(now),"5min.mp4",hash);
                ch.getAllVideos().add(new_video);
                ch2.getAllVideos().add(new_video);
                //-----------------------------------------------------------------------------
                //Katanomh stouw brokers me bash ta CHANNEL NAMES.
                for (int i=0; i<channels.size(); i++){
                    String hashed_chName = (broker.calculateKeys(channels.get(i).getChannelName()));
                    boolean flag=false;
                    for (int j=0; j<delimiter_of_Brokers.size(); j++){
                        if (hashed_chName.compareTo(broker.delimiter_of_Brokers.get(j)) < 0){
                            brokers.get(j).channels_serviced.add(hashed_chName);
                            flag=true;
                            break;
                        }
                    }
                    if (flag==false) {
                        brokers.get(delimiter_of_Brokers.size()).channels_serviced.add(hashed_chName);
                    }
                }
                System.out.println("edw typwnw ta hashed channel name tou broker 0: " +brokers.get(0).channels_serviced);
                System.out.println("edw typwnw ta hashed channel name tou broker 1: " +brokers.get(1).channels_serviced);
                System.out.println("edw typwnw ta hashed channel name tou broker 2: " +brokers.get(2).channels_serviced);
                //--------------------------------------------------------------------------------------------------------
                //Katanomh stouw brokers me bash ta HASHTAGS.
                for (int i=0; i<hash.size(); i++){
                    String hashed_hashtag = (broker.calculateKeys(hash.get(i)));
                    boolean flag=false;
                    boolean flag2=false;
                    for (int j=0; j<delimiter_of_Brokers.size(); j++){
                        if (hashed_hashtag.compareTo(broker.delimiter_of_Brokers.get(j)) < 0 && flag2==false){
                            flag2=true;
                            if (!brokers.get(j).hashtags_serviced.contains(hashed_hashtag)) {
                                brokers.get(j).hashtags_serviced.add(hashed_hashtag);
                                flag=true;
                                break;
                            }
                        }
                    }
                    if (flag==false && flag2==false) {
                        if (!brokers.get(delimiter_of_Brokers.size()).hashtags_serviced.contains(hashed_hashtag)) {
                            brokers.get(delimiter_of_Brokers.size()).hashtags_serviced.add(hashed_hashtag);
                        }
                    }
                }
                System.out.println("edw typwnw ta hashed hashtags tou broker 0: " +brokers.get(0).hashtags_serviced);
                System.out.println("edw typwnw ta hashed hashtags tou broker 1: " +brokers.get(1).hashtags_serviced);
                System.out.println("edw typwnw ta hashed hashtags tou broker 2: " +brokers.get(2).hashtags_serviced);
                //-----------------------------------------------------------------------------

                //adding new video's hashtags in channel's hashtagsList--if not exist.
                boolean flag;
                for(int i=0; i<hash.size(); i++){
                    flag = true;
                    for(int j=0; j<ch.getHashtagsPublished().size();j++){
                        if( (hash.get(i)).equals( ch.getHashtagsPublished().get(j) ) ) flag = false;
                    }
                    if(flag) ch.getHashtagsPublished().add(hash.get(i));
                }

                System.out.println(ch.getHashtagsPublished());

                /*
                System.out.println(ch.getChannelName());
                System.out.println(ch.getHashtagsPublished());
                
                for(int i=0; i<ch.getAllVideos().size(); i++){
                    System.out.println(ch.getAllVideos().get(i).getName());
                }
                */

                String video_file_to_send = new_video.getPath();
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
                bis.close();
                fis.close();
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
                */
                in.close();
                out.close();
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


        public Accept_Consumer_handlers(Broker b, ServerSocket socket, int p, int nof, List<Consumer> registers) {
            this.broker = b;
            this.serverSocket = socket;
            this.port = p;
            this.number_of_thread = nof;
            this.registeredUsers = registers;
        }

        public void run(){
                try{
                    while (true){
                        Socket x=serverSocket.accept();
                        System.out.println("                                consumer accept");
                        //int thesi = number_of_clients.get(0) % brokers.size();
                        //System.out.println("Server that accepted the connection : "+thesi);
                        //brokers.get(thesi).registeredUsers.add(new Consumer(clSocket, this)); // kai alles metavlites
                        //brokers.get(thesi).clientSocket = clSocket;
                        //System.out.println(port);
                        number_of_clients.set(0, number_of_clients.get(0)+1);
                        Consumer temp = new Consumer(x,number_of_thread+1,port);
                        temp.setBroker(broker);
                        registeredUsers.add(new Consumer(temp));
                        System.out.println("number of consumers : "+registeredUsers.size());
                        new Consumer_handlers(x,number_of_thread++,port,broker).start();
                    }
                }
                catch(Exception e){
                }
            
        }
    }

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
                    System.out.println("                                publisher accept");
                    number_of_publishers.set(0, number_of_publishers.get(0)+1);
                    System.out.println("NUMBER OF PUBLISHERS :"+number_of_publishers.get(0));
                }
                
            }
            catch(Exception e){
                System.out.println("exception ston gamwpublisher");
            }
        }
    }


    public static void main(String args[]) {
        
        number_of_clients.add(0);
        number_of_publishers.add(0);
        int count =0;

        Broker b1 = new Broker(6666, 5666);
        b1.start();
        count++;
      
        brokers.add(b1);
        Broker b2 = new Broker(6667,5667);
        b2.start();
        count++;
        brokers.add(b2);

        Broker b3 = new Broker(6668,5668);
        b3.start();
        count++;
        brokers.add(b3);

        try{
            int x = Integer.parseInt(Inet4Address.getLocalHost().getHostAddress().replace(".", ""));  
            System.out.println(x);
            String str="";
            String str2="";
    
            
            for(int i=1; i<=count-1;i++){
                System.out.println("Inside.");
                str = Integer.toString(x+brokers.get(i-1).port);
                //System.out.println(x+brokers.get(i-1).port);
                delimiter_of_Brokers.add(brokers.get(i-1).calculateKeys(str));
                //delimiter_of_Brokers.add(b2.calculateKeys(str));
                System.out.println(brokers.get(i-1).calculateKeys(str));
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