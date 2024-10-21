package tw.lab2_d;

public class Main{
    public static void main(String[] args) {
        int consumer_count = 3;
        int producer_count = 3;
        int sum = consumer_count + producer_count;
        int M = 10;

        Buffer buffer = new Buffer(2 * M);
        Th[] threads = new Th[consumer_count + producer_count];

        for (int i = 0; i < sum; i++) {
            Th thread = new Th(i < producer_count, buffer, M);
            threads[i] = thread;
            thread.start();
        }

        for (int i = 0; i < sum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }
    }
}