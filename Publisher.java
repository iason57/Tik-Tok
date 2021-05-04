import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.Desktop;

public class Publisher extends Thread implements Publisher_interface,Node{

    ArrayList<String> hash = new ArrayList<String>();
    public Socket clientSocket;
    public PrintWriter out;
    public BufferedReader in;
    public Broker broker;
    public int port;
    public int id;

    public Publisher(){

    }

    public Publisher(int p, int di){
        port = p;
        id =di;
    }

    public Publisher(Socket clSocket,int number_of_thread,int port){
        clientSocket = clSocket;
        id = number_of_thread;
        this.port = port;
    }

    public Publisher(Publisher x) {
        this.clientSocket = x.clientSocket;
        this.out = x.out;
        this.in = x.in;
        this.broker = x.broker;
        this.port = x.port;
        this.id = x.id;
    }

    private ChannelName channelName;

    //--------------------------------------------------------------

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
    public void push(String str,Value v){
        
    }
    public void notifyFailure(Broker b){
        
    }
    public void notifyBrokersForHashTags(String str){
        
    }
    public void generateChunks(String str){
        
    }

    //Node methods

    public void init(int i){
        try{
            clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), port);
        }
        catch(Exception e){

        }
    }

    public void connect(int port1){
        System.out.println("Message section : ");
        String str;
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String message_from_server;
        System.out.println("Reader ok ");
        System.out.println("port is  "+ port1);
        try{
            Socket clientSocket2;
            clientSocket2 = new Socket(Inet4Address.getLocalHost().getHostAddress(), port1);
            out = new PrintWriter(clientSocket2.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
            System.out.println("out kai in ok.");
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
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                    str =  reader.readLine();
                    out.println(str);
                    message_from_server = in.readLine();
                    System.out.println(""+message_from_server);
                    str =  reader.readLine();
                    out.println(str);
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
        try{
            int file_size = 100003;
            int bytesRead;
            int current = 0;
            InputStream is = clientSocket.getInputStream();
            byte [] mybytearray  = new byte [file_size];
            byte [] to_mp4_full  = new byte [10000000];
            int pointer = 0;
            String video_file  = "publisher-source-downloaded-"+port+"-"+id+".mp4";
            fos = new FileOutputStream(video_file);
            bos = new BufferedOutputStream(fos);
            System.out.println("prin to while");
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
            System.out.println("202");
            bos.write(to_mp4_full, 0 , pointer);
            bos.flush();
            System.out.println("File " + video_file
                + " downloaded (" + pointer + " bytes read)");
            
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
            System.out.println("Exception in connect in publisher.");
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
            this.download();
            Thread.sleep(2000);
            this.connect(port + 2000);
            
        }
        catch(Exception e){

        }
    }

    public static void main(String args[]) {
        
        Publisher t1 = new Publisher(5666,1);
        
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