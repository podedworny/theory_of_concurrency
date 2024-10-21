package tw.lab3;

import tw.lab2_d.Buffer;
import tw.lab2_d.Th;

public class Main{
    public static void main(String[] args) {
        int consumer_count = 3;
        int producer_count = 3;
        int sum = consumer_count + producer_count;
        int products_count = 10;
        int M = 10;

        tw.lab2_d.Buffer buffer = new Buffer(2 * M);
        tw.lab2_d.Th[] threads = new tw.lab2_d.Th[consumer_count + producer_count];

        for (int i = 0; i < sum; i++) {
            tw.lab2_d.Th thread = new Th(i < producer_count, buffer, products_count, M);
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