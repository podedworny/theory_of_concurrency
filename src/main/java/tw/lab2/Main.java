package tw.lab2;


public class Main{
    public static void main(String[] args) {
        int consumer_count = 1;
        int producer_count = 2;
        int sum = consumer_count + producer_count;
        int buffer_size = 100;
        int products_count = 100;

        Bufor bufor = new Bufor(2*buffer_size);
        Th[] threads = new Th[consumer_count + producer_count];

        for (int i = 0; i < sum; i++) {
            Th thread = new Th(i < producer_count, bufor, buffer_size);
            threads[i] = thread;
            thread.start();
        }

        for (int i = 0; i < sum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }
        System.out.println(bufor.getX());
    }
}