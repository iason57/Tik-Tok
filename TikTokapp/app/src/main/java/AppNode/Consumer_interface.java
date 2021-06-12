package AppNode;

interface Consumer_interface{
    public void register(Broker b,String str);
    public void disconnect(Broker b,String str);
}