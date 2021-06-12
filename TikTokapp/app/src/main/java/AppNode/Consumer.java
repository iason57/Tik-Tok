package AppNode;

import java.util.*;
import java.net.*;
import java.io.*;

public class Consumer extends Thread implements Consumer_interface, Node {

    public Socket clientSocket;
    public Socket clientSocket2;
    public PrintWriter out;
    public BufferedReader in;
    public Broker broker;
    public int port;
    public int id;
    public String video_name_temp;

    /*
    public Consumer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Consumer(Socket clientSocket, Broker br) {
        this.clientSocket = clientSocket;
        this.broker = br;
    }
    */

    public Consumer(Consumer x) {
        this.clientSocket = x.clientSocket;
        this.out = x.out;
        this.in = x.in;
        this.broker = x.broker;
        this.port = x.port;
        this.id = x.id;
    }
    
    public Consumer(Socket clSocket,int number_of_thread,int port){
        clientSocket = clSocket;
        id = number_of_thread;
        this.port = port;
    }

    public Consumer(int p, int di){
        port = p;
        id =di;
    }

    public Consumer(){
        
    }

    public void setBroker(Broker br){
        this.broker = br;
    }

    //--------------------------------------------------------------
    
    public void register(Broker b,String str){
        //have no idea what to do with the str input...
        for(int i =0; i<b.channels_serviced.size();i++){
            if(str.equals(b.channels_serviced.get(i).getChannelName())){
                b.subscribers.get(i).add(this);
            }
        }
    }
    public void disconnect(Broker b,String str){

    }

    //Node methods
    
    public void init(int port){
        try{
            clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), port);
            //clientSocket2 = new Socket(Inet4Address.getLocalHost().getHostAddress(), port+1000);
        }
        catch(Exception e){

        }
        
    }

    public void messages(int port1){
        System.out.println("Message section : ");
        String str;
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String message_from_server;
        System.out.println("Reader ok ");
        System.out.println("port is  "+ port1);
        try{
            clientSocket2 = new Socket(Inet4Address.getLocalHost().getHostAddress(), port1);
            System.out.println("edw0");
            out = new PrintWriter(clientSocket2.getOutputStream(), true); //<----------------------------
            System.out.println("edw1");
            in = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
            message_from_server = in.readLine();
            System.out.println("edw2");
            System.out.println("Connection established, server said : "+message_from_server);
            while(true){
                System.out.println("Give your message : ");
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

    public void connect(int xyz){
        String str;
        //BufferedReader reader = new BufferedReader(
        //    new InputStreamReader(System.in));
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
                //new ----------------------------------------------------------------------------
                int file_size = 100003;
                int bytesRead;
                int current = 0;
                System.out.println("Starting socket");
                System.out.println("port is : "+port);
                clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), port+3000 );
                //clientSocket2 = new Socket(Inet4Address.getLocalHost().getHostAddress(), port+1000);
                InputStream is = clientSocket.getInputStream();
                byte [] mybytearray  = new byte [file_size];
                //mpakale test
                byte [] to_mp4_full  = new byte [10000000];
                int pointer = 0;
                String video_file  = "source-downloaded-"+port+"-"+id+"-"+video_name_temp+".mp4";
                fos = new FileOutputStream(video_file);
                bos = new BufferedOutputStream(fos);
                while (true){
                    //String video_file  = "C://Users//iason//Desktop//Ergasia-TikTok//Tik-Tok//source-downloaded-"+port+"-"+id+"-part-"+part+".txt";
        
                    // receive file

                    mybytearray  = new byte [file_size];
                    
                    //System.out.println("Step1");
                    try{
                        //System.out.println("new");
                        //fos = new FileOutputStream(video_file);
                        //bos = new BufferedOutputStream(fos);
                        
                        //System.out.println(mybytearray.length);
                        bytesRead = is.read(mybytearray,0,mybytearray.length);
                        
                        //current = bytesRead;
                        current = 0;
                        //System.out.println("Step2");
                        
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
                                //if (current==100000){
                                    //System.out.println(mybytearray[current]);
                                    //System.out.println(mybytearray[current +1]);
                                    //System.out.println(mybytearray[current +2]);
                                //}
                                current++;
                            }
                        } while(true);

                        //System.out.println("vgika");
                        //System.out.println(mybytearray[current]);
                        //System.out.println(mybytearray[current +1]);
                        //System.out.println(mybytearray[current +2]);
                        
                        
                        //System.out.println("File size is : "+current);
                        //bytesRead = is.read(mybytearray,0,current);
                        //bos.write(mybytearray, 0 , file_size);

                        for(int i = pointer; i<pointer+current;i++){
                            to_mp4_full[i] = mybytearray[i-pointer]; // [0-current]
                        }
                        pointer+=current;//proti fora 100.000 , deyteri fora 109.481
                        //System.out.println("Pointer is : "+pointer);
                        //bos.write(mybytearray, 0 , current-1);
                        //bos.flush();
                        
                    }
                    catch(FileNotFoundException e){
                        System.out.println("Exception bro.");
                    }
                    
                    //System.out.println("File " + video_file
                    //    + " downloaded (" + current + " bytes read)");
    
                    
    
                    if (current < 100000 ) {
                        //System.out.println("Breaking");
                        break;
                    }
                    //fos.close();
                    //bos.close();
                    
                    //end new ----------------------------------------------------------------------------
                    /*
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    while(true){
                        
                        str =  reader.readLine();
                        out.println(str);
                        if(str.equals("..")){
                            System.exit(0);
                        }
                        message_from_server = in.readLine();
                        System.out.println("Broker "+port+" said : "+message_from_server);
                    }
                    */
                }
                bos.write(to_mp4_full, 0 , pointer);
                bos.flush();
                System.out.println("File " + video_file
                    + " downloaded (" + pointer + " bytes read)");
                
                is.close();
                fos.close();
                bos.close();

                /*
                is=null;
                fos=null;
                bos=null;
                */

                System.out.println("Closing readers");
                //return;
                /*
                Thread.sleep(5000);
                InputStream receive = clientSocket.getInputStream();
                System.out.println("Closing 1");
                OutputStream send = clientSocket.getOutputStream();
                System.out.println("Closing 2");
                out = new PrintWriter(send, true);
                System.out.println("Closing 3");
                in = new BufferedReader(new InputStreamReader(receive));
                System.out.println("Closing 4");
                System.out.println("Starting to talk with consumer : "+id+" and broker : "+ port);
                //registeredUsers.add(id);
                String greeting = in.readLine();
                while (!greeting.equals(".")) {
                    System.out.println("Client "+id+" said to broker "+ port +" : "+greeting);
                    str =  reader.readLine();
                    out.println("Response to "+greeting+", "+str + " message to "+id);
                    greeting = in.readLine();
                }
                
                in.close();
                out.close();
                */

                
                
                //Thread.sleep(10000 * (id-1));
                //this.playData(video_file);
            }
            catch (Exception e){
                System.out.println("Exception in connect in consumer.");
            }
            
    }
    //?-?

    public void connect2(String video_name){
        String str;
        //BufferedReader reader = new BufferedReader(
        //    new InputStreamReader(System.in));
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
                //new ----------------------------------------------------------------------------
                int file_size = 100003;
                int bytesRead;
                int current = 0;
                System.out.println("Starting socket");
                System.out.println("port is : "+port);
                clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), port+3000 );
                //clientSocket2 = new Socket(Inet4Address.getLocalHost().getHostAddress(), port+1000);
                InputStream is = clientSocket.getInputStream();
                byte [] mybytearray  = new byte [file_size];
                //mpakale test
                byte [] to_mp4_full  = new byte [10000000];
                int pointer = 0;
                String video_file  = "source-downloaded-"+port+"-"+id+"-"+video_name+".mp4";
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
                        pointer+=current;//proti fora 100.000 , deyteri fora 109.481
                                                
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
                System.out.println("Exception in connect in consumer.");
            }
            
    }

    public void disconnect(int d){
        try{
            in.close();
            out.close();
            clientSocket.close();
        }
        catch(Exception e){

        }
        
    }

    // den exoyme idea ti kanei
    public void updateNodes(){

    }
    public List<Broker> getBrokers(){
        return brokers;
    }

    public void run(){
        try{
            this.init(port);
            this.messages(port+1000);
            System.out.println("Initializing port : "+port);
            System.out.println("Sending port : "+(port+2000));
            System.out.println("Messages port : "+(port+1000));
            //Consumer client = new Consumer(port);
            //client.init(port);
            //client.connect();
            //System.out.println("run thread Consumer");
            
        }
        catch(Exception e){

        }
    }
    public static void main(String args[]) {
        Consumer t1 = new Consumer(6668,1);
        
        t1.start();
        
        /*
        
        try{
            Thread.sleep(1000);
        }
        catch(Exception e){
            System.out.println("Something went wrong with waiting.");
        }  
    
        
        
        Consumer t11 = new Consumer(6666,2);
        
        t11.start();

        

        try{
            Thread.sleep(1000);
        }
        catch(Exception e){
            System.out.println("Something went wrong with waiting.");
        }
        
        
        
        Consumer t8 = new Consumer(6666,2);
        
        t8.start();
        */
        
        /*

        Consumer t9 = new Consumer(6667,2);
        
        t9.start();
        */
        
        /*

        // Publishers
        
        Consumer t2 = new Consumer(5666,1);
        
        t2.start();

        Consumer t3 = new Consumer(5666,2);
        
        t3.start();

         
        Consumer t4 = new Consumer(5666,3);
        
        t4.start();

        Consumer t5 = new Consumer(5666,4);
        
        t5.start();
        */

        /*
        try{
            Thread.sleep(4000);
            //System.exit(0); // <----------------------------------------------------------------------------------------------------------
        }
        catch(Exception e){
            System.out.println("Something went wrong with closing the threads.");
        }  
        */

        /*
        Consumer t2 = new Consumer(6667,1);
        
        t2.start();

        

        
        Consumer t3 = new Consumer(6668,1);
        
        t3.start();

        

        Consumer t4 = new Consumer(6666,2);
        
        t4.start();
        */
        
        
    }
}