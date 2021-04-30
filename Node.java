import java.util.*;

interface Node{

    public List<Broker> brokers = new ArrayList<Broker>(); // statikh domh, dld den tha allazei to hashing
    public List<Integer> number_of_clients = new ArrayList<Integer>();
    public List<Integer> number_of_publishers = new ArrayList<Integer>();
    public ArrayList<String> delimiter_of_Brokers = new ArrayList<String>();
    
    //-----------------------------------

    public void init(int i);
    public void connect();
    public void disconnect(int u);
    public void updateNodes();
    public List<Broker> getBrokers();
}