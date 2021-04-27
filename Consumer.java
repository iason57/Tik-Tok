import java.util.*;
import java.net.*;
import java.io.*;

public class Consumer extends Thread implements Consumer_interface,Node {

    public Socket clientSocket;
    public PrintWriter out;
    public BufferedReader in;
    public String message = "Hello"; 

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
            clientSocket = new Socket("192.168.1.14", port);
        }
        catch(Exception e){

        }
        
    }

    public void connect(){
        try{
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
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
            
            Consumer client = new Consumer();
            client.init(6666);
            client.connect();
            System.out.println("run thread 1");
            
        }
        catch(Exception e){

        }
    }
    public static void main(String args[]) {
        
        Consumer t1 = new Consumer();
        
        t1.start();
        
    }
}