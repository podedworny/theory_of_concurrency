package tw.incorrect_4cond;

import java.util.concurrent.TimeUnit;

public class Main{
    public static void main(String[] args) {
        int consumer_count = 3;
        int producer_count = 1;
        int sum = consumer_count + producer_count;
        int M = 5;

        Buffer buffer = new Buffer(2 * M);
        Th[] threads = new Th[consumer_count + producer_count];

        for (int i = 0; i < sum; i++) {
            Th thread = new Th(i < producer_count, buffer, M);
            threads[i] = thread;
            thread.start();
        }

        try {
            TimeUnit.SECONDS.sleep(20);
        }
        catch (Exception ignored) {}

        int[] arr = new int[sum];

        for (int i=0; i<sum;i++){
            threads[i].running = false;
            threads[i].interrupt();
            arr[i] = threads[i].count;
        }

        for (int i = 0; i < sum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }

        for (int i=0; i<sum;i++){
            if (i < producer_count){
                System.out.println("Producer " + i + " produced " + arr[i]);
            }
            else {
                System.out.println("Consumer " + i + " consumed " + arr[i]);

            }
        }
    }
}