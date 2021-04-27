import java.util.*;
import java.net.*;
import java.io.*;

public class Broker extends Thread implements Broker_interface,Node{

    public List<Consumer> registeredUsers;
    public List<Publisher> registeredPublishers;
    public ServerSocket serverSocket;
    public Socket clientSocket; // mallon oxi
    public PrintWriter out;
    public BufferedReader in;
    private int number_of_thread = 1;

    public Broker(Broker br){
        this.registeredUsers = br.registeredUsers;
        this.registeredPublishers = br.registeredPublishers;
        this.serverSocket = br.serverSocket;
        this.clientSocket = br.clientSocket;
        this.out = br.out;
        this.in = br.in;
        this.number_of_thread = br.number_of_thread;
    }

    public Broker(){

    }

    //--------------------------------------------------------------------

    public void calculateKeys(){

    }
    public Publisher acceptConnection(Publisher p){
        return null;
    }
    public void acceptConnection(){ //parametros : Consumer c - type : Consumer
        while (true){
            try{
                Socket clSocket = serverSocket.accept();
                registeredUsers.add(new Consumer(clSocket, this));
                new Consumer_handlers(clSocket,number_of_thread++).start();
                number_of_clients.set(0, number_of_clients.get(0)+1);
            }
            catch(Exception e){
            }
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
        registeredPublishers = new ArrayList<Publisher>();
    }
    public void connect(){

    }
    public void disconnect(){

    }
    public void updateNodes(){

    }
    public List<Broker> getBrokers(){
        return brokers;
    }

    public void run(){
        try{
            for (int j = 0; j < 3; j++) {
                Broker server = new Broker();
                server.init(6666+j); //ports: 6666, 6667, 6668.
                brokers.add(new Broker(server));
                brokers.get(j).acceptConnection();
            }
            System.out.println("run thread Broker");
            
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

        public Consumer_handlers(Socket socket,int num) {
            this.clientSocket = socket;
            id = num;
        }

        public void run() {            
            String str;
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

            try{

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Starting to talk with consumer : "+id);
                //registeredUsers.add(id);
                String greeting = in.readLine();
                while (!greeting.equals(".")) {
                    System.out.println("Client "+id+" said : "+greeting);
                    str =  reader.readLine();
                    out.println(str + " message to "+id);
                    greeting = in.readLine();
                }
                in.close();
                out.close();
                clientSocket.close();
            }
            catch(Exception e){
    
            }            
        }
    }


    public static void main(String args[]) {
        Broker t2 = new Broker();
        brokers.add(t2);
        for (int u=0; u<brokers.size(); u++){
            System.out.println(brokers.get(u));
        }
        t2.start();
    }
}