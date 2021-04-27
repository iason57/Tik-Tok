interface Node{

    public List<Broker> brokers; // statikh domh, dld den tha allazei to hashing

    //-----------------------------------

    public void init(int i);
    public void connect();
    public void disconnect();
    public void updateNodes();
    public List<Broker> getBrokers();
}