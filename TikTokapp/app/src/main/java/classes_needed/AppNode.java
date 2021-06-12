package classes_needed;

public class AppNode {
    public static void main(String[] args) {
        Consumer t1 = new Consumer(6666,1);
        
        t1.start();

        Publisher t2 = new Publisher(5666,1);
        
        t2.start();
    }
}