package tw.lab2;


public class Main{
    public static void main(String[] args) {
        int x = 2; // ilosc watkow
        int y = 1000; // ilosc petli
        Bufor v = new Bufor(0);
        Th[] threads = new Th[x];
        for (int i = 0; i < x; i++) {
            Th t1 = new Th(i % 2 == 0, v, y);
            threads[i] = t1;
            t1.start();
        }
        for (int i = 0; i < x; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }
        System.out.println(v.getX());
    }
}