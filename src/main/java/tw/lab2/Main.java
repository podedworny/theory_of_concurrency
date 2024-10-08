package tw.lab2;

public class Main {
    public static void main(String[] args) {
        Variable v = new Variable();
        Th th = new Th(true, v);
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(th);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(v.getValue());
    }
}
