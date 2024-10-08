package tw.lab1;

public class Main{
    public static void main(String[] args) {
        int x = 2; // ilosc watkow
        int y = 500000; // ilosc petli
        Variab v = new Variab(0);
        ThreadExample[] threads = new ThreadExample[x];
        for (int i = 0; i < x; i++) {
            ThreadExample t1 = new ThreadExample(i % 2 == 0, v, y);
            t1.start();
            threads[i] = t1;
        }
        for (int i = 0; i < x; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(v.getX());
    }
}