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
    public int publisher_port;
    public ServerSocket publisherServerSocket;

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
    }

    public Broker(){

    }

    public Broker(int p){
        port = p;
    }

    public Broker(int p,int port_p){
        port = p;
        publisher_port = port_p;
    }

    /*
    public int get_Broker_(){
        return number_of_thread;
    }
    */
    //--------------------------------------------------------------------

    public void calculateKeys(){

    }
    public void publisherAcceptConnection(){
        while(true){
            try{
                //Socket pubSocket = publisherServerSocket.accept();
                new Accept_Publisher_handlers(publisherServerSocket.accept(), port, number_of_thread, registeredPublishers).start();
            }
            catch(Exception e){
                System.out.println("exception ston gamwpublisher");
            }
        }
    }

    public void acceptConnection(){ //parametros : Consumer c - type : Consumer
        while (true){
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
            if(registeredUsers.get(i).port == id_client){
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

    public void run(){
        try{
            //for (int j = 0; j < 3; j++) {
                //Broker server = new Broker(port);
                //server.init(port);
                //server.acceptConnection();
                
                this.init(port);
                this.init2(publisher_port);
                /*Broker temp = new Broker(this);
                temp.publisherAcceptConnection();*/
                this.acceptConnection(); 
                this.publisherAcceptConnection();
                System.out.println(publisher_port);
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
            
            try{
               
                String video_file_to_send = "source.mp4";
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
                        System.out.println("The chunk is : "+chunk);
                        //if(chunk < 100000) break;
                    }
                    byte [] mybytearray  = new byte [chunk+3];
                    System.out.println("Length : "+mybytearray.length);
                    String k = "end";
                    byte[] b = k.getBytes();
                    //System.out.println(b.length);
                    mybytearray[mybytearray.length - 3] = b[0];
                    mybytearray[mybytearray.length  - 2] = b[1];
                    mybytearray[mybytearray.length  - 1] = b[2];

                    System.out.println("here"); 
                    
                    
                    //bis.read(mybytearray,pointer_in_file,pointer_in_file+chunk); //chunk + 3?? thelo to pointer in file?
                    System.out.println("here1"); 
                    for(int i=pointer_in_file;i < (pointer_in_file+chunk);i++){
                        mybytearray[i-pointer_in_file] = allfile[i];
                    }
                    System.out.println("here2"); 
                    
                    System.out.println("Sending " + video_file_to_send + "(" + (mybytearray.length -3) + "bytes) part :"+(pointer_in_file/100000 +1 ) );    
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    System.out.println("Done.");
                    pointer_in_file += 100000;
                    System.out.println(pointer_in_file);                    
                }
                bis.close();
                fis.close();
                os.close();

                System.out.println("File size is : "+(int)myFile.length());
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
            //while (true){
                try{
                    Socket x=serverSocket.accept();
                    //int thesi = number_of_clients.get(0) % brokers.size();
                    //System.out.println("Server that accepted the connection : "+thesi);
                    //brokers.get(thesi).registeredUsers.add(new Consumer(clSocket, this)); // kai alles metavlites
                    //brokers.get(thesi).clientSocket = clSocket;
                    System.out.println(port);
                    number_of_clients.set(0, number_of_clients.get(0)+1);
                    Consumer temp = new Consumer(x,number_of_thread+1,port);
                    temp.setBroker(broker);
                    registeredUsers.add(new Consumer(temp));
                    System.out.println(registeredUsers.size());
                    new Consumer_handlers(x,number_of_thread++,port).start();
                }
                catch(Exception e){
                }
            //}
        }
    }

    private static class Accept_Publisher_handlers extends Thread {
        private Socket publisherServerSocket;
        private int port;
        private int number_of_thread = 1;
        public List<Publisher> registeredPublishers;


        public Accept_Publisher_handlers(Socket socket, int p, int nof, List<Publisher> registers) {
            this.publisherServerSocket = socket;
            this.port = p;
            this.number_of_thread = nof;
            this.registeredPublishers = registers;
        }

        public void run(){
            System.out.println("edwwwwwwwwwwww");
        try{
            System.out.println("publisher accept");
            number_of_publishers.set(0, number_of_publishers.get(0)+1);
            System.out.println(number_of_publishers.get(0));

        }
        catch(Exception e){
            System.out.println("exception ston gamwpublisher");
        }
        }
    }


    public static void main(String args[]) {
        
        number_of_clients.add(0);
        number_of_publishers.add(0);

        Broker b1 = new Broker(6666, 5666);
        b1.start();

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