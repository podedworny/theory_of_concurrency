package tw.incorrect_2cond;


import java.util.concurrent.TimeUnit;

public class Main{
    public static void main(String[] args) {
        int consumer_count = 9;
        int producer_count = 1;
        int sum = consumer_count + producer_count;
        int max_product = 100;

        Buffer buffer = new Buffer(2*max_product);
        Th[] threads = new Th[consumer_count + producer_count+1];

        for (int i = 0; i < sum; i++) {
            Th thread = new Th(i < producer_count, buffer, max_product, 1);
            threads[i] = thread;
            thread.start();
        }

        threads[sum] = new Th(false, buffer, max_product, 1);
        threads[sum].start();
        sum++;

        try {
            TimeUnit.SECONDS.sleep(12);
        }
        catch (Exception ignored) {}

        int[] arr = new int[sum];
        int[] arr2 = new int[sum];

        for (int i=0; i<sum;i++){
            threads[i].running = false;
            threads[i].interrupt();
            arr[i] = threads[i].count;
            arr2[i] = threads[i].sum;
        }

        for (int i = 0; i < sum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }

        for (int i=0; i<sum;i++){
            if (i < producer_count){
                System.out.println("Producer " + i + " produced " + arr[i] + " times. Sum: " + arr2[i]);
            }
            else {
                System.out.println("Consumer " + (i - producer_count) + " consumed " + arr[i] + " times. Sum: " + arr2[i]);

            }
        }
    }
}