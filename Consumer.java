import java.util.*;
import java.net.*;
import java.io.*;

public class Consumer extends Thread implements Consumer_interface,Node {

    public Socket clientSocket;
    public PrintWriter out;
    public BufferedReader in;
    public String message = "Hello";
    public Broker broker;
    public int port;

    public Consumer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Consumer(Socket clientSocket, Broker br) {
        this.clientSocket = clientSocket;
        this.broker = br;
    }

    public Consumer(int p){
        port = p;
    }

    public Consumer(){
        
    }

    public void setBroker(Broker br){
        this.broker = br;
    }

    //--------------------------------------------------------------

    public void register(Broker b,String str){

    }
    public void disconnect(Broker b,String str){

    }
    public void playData(String str,Value v){

    }
    
    //Node methods
    
    public void init(int port){
        try{
            clientSocket = new Socket(Inet4Address.getLocalHost().getHostAddress(), port);
        }
        catch(Exception e){

        }
        
    }

    public void connect(){
        String str;
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String message_from_server;
        
        try{
            //new ----------------------------------------------------------------------------
            int file_size = 150000;
            int bytesRead;
            int current = 0;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            String video_file = "C://Users//iason//OneDrive//Desktop//Tik-Tok//source-downloaded.mp4";

            // receive file

            byte [] mybytearray  = new byte [file_size];
            
            InputStream is = clientSocket.getInputStream();
            
            System.out.println("Step1");
            try{
                fos = new FileOutputStream(video_file);
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                
                //current = bytesRead;
                current = 0;
                System.out.println("Step2");
                
                String k = "end";
                byte[] b = k.getBytes();

                Byte w1 = new Byte(b[0]);
                Byte w2 = new Byte(b[1]);
                Byte w3 = new Byte(b[2]);

                

                /*
                 String l = "asdfasdfendasdf";
                byte[] h = l.getBytes();

                do {
                    if(w1.equals(new Byte(h[current]))){
                        if(w2.equals(new Byte(h[current+1]))){
                            if(w3.equals(new Byte(h[current+2]))){
                                break;
                            }
                        }
                    }
                    else{
                        current++;
                    }
                } while(true);

                System.out.println("ok, done test kai current : "+current);

                */
                
                System.out.println("Step3");
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
                        //if(current == 1700000)break;
                        current++;
                    }
                } while(true);
                System.out.println(mybytearray[current ]);
                System.out.println(mybytearray[current +1]);
                System.out.println(mybytearray[current +2]);
                
                
                System.out.println("File size is : "+current);
                //bytesRead = is.read(mybytearray,0,current);
                //bos.write(mybytearray, 0 , file_size);
                
                bos.write(mybytearray, 0 , current-1);
                bos.flush();
            }
            catch(FileNotFoundException e){
                
            }
            
            System.out.println("File " + video_file
                + " downloaded (" + current + " bytes read)");
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
        catch (Exception e){

        }
        
    }
    public void disconnect(){
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
            this.connect();
            //Consumer client = new Consumer(port);
            //client.init(port);
            //client.connect();
            //System.out.println("run thread Consumer");
            
        }
        catch(Exception e){

        }
    }
    public static void main(String args[]) {
        
        Consumer t1 = new Consumer(6666);
        
        t1.start();
        /*

        Consumer t2 = new Consumer(6667);
        
        t2.start();

        Consumer t3 = new Consumer(6668);
        
        t3.start();

        Consumer t4 = new Consumer(6666);
        
        t4.start();
        */
        
    }
}