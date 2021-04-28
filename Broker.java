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
    public int port;

    public Broker(Broker br){
        this.registeredUsers = br.registeredUsers;
        this.registeredPublishers = br.registeredPublishers;
        this.serverSocket = br.serverSocket;
        this.clientSocket = br.clientSocket;
        this.out = br.out;
        this.in = br.in;
        this.number_of_thread = br.number_of_thread;
        this.port = port;
    }

    public Broker(){

    }

    public Broker(int p){
        port = p;
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
                //int thesi = number_of_clients.get(0) % brokers.size();
                //System.out.println("Server that accepted the connection : "+thesi);
                //brokers.get(thesi).registeredUsers.add(new Consumer(clSocket, this)); // kai alles metavlites
                //brokers.get(thesi).clientSocket = clSocket;
                
                System.out.println(port);
                number_of_clients.set(0, number_of_clients.get(0)+1);
                new Consumer_handlers(clSocket,number_of_thread++,port).start();
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
            //for (int j = 0; j < 3; j++) {
                //Broker server = new Broker(port);
                //server.init(port);
                //server.acceptConnection();
                this.init(port);
                this.acceptConnection();
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

        public Consumer_handlers(Socket socket,int num,int p) {
            this.clientSocket = socket;
            id = num;
            pport = p;
        }

        public void run() {            
            String str;
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

            //new ---------------------------------------------------------------------------
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            OutputStream os = null;
            try{
               
                String video_file_to_send = "C://Users//iason//OneDrive//Desktop//Tik-Tok//source.mp4";
                // send file
                File myFile = new File (video_file_to_send);
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
                System.exit(0);
                
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


    public static void main(String args[]) {
        
        number_of_clients.add(0);

        Broker b1 = new Broker(6666);
        b1.start();
        /*
        Broker b2 = new Broker(6667);
        b2.start();

        Broker b3 = new Broker(6668);
        b3.start();
        */
    }
}