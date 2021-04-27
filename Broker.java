import java.util.*;
import java.net.*;
import java.io.*;

public class Broker extends Thread implements Broker_interface,Node{

    public List<Consumer> registeredUsers = new ArrayList<Consumer>();
    public List<Publisher> registeredPublishers;
    public ServerSocket serverSocket;
    public Socket clientSocket; // mallon oxi
    public PrintWriter out;
    public BufferedReader in;

    //--------------------------------------------------------------------

    public void calculateKeys(){

    }
    public Publisher acceptConnection(Publisher p){
        return null;
    }
    public void acceptConnection(){ //parametros : Consumer c - type : Consumer
        try{
            System.out.println("here3!");
            clientSocket = serverSocket.accept();
            System.out.println("here2!");
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);       
            System.out.println("here!");
            String greeting = in.readLine();
            
        }
        catch(Exception e){

        }
        // return c;
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
        /*
        for (int j = 0; j < 10; j++) {
            brokers.add(new Broker());
        }
        */
    }
    public void connect(){

    }
    public void disconnect(){

    }
    public void updateNodes(){

    }
    public List<Broker> getBrokers(){
        return null;
    }

    public void run(){
        try{
            
            
            System.out.println("run thread 1");
            Broker server = new Broker();
            server.init(6666);
            System.out.println("run thread 2");
            server.acceptConnection();
        }
        catch(Exception e)
        {

        }
    }
    public static void main(String args[]) {
        /*
        new Client(10, 5).start();
        new Client(20, 5).start();
        new Client(30, 5).start();
        new Client(40, 5).start();
        new Client(50, 5).start();
        new Client(60, 5).start();
        new Client(70, 5).start();
        new Client(80, 5).start();
        new Client(90, 5).start();
        new Client(100, 5).start();
        */
        
        Broker t2 = new Broker();
        
        t2.start();
        
    }
}