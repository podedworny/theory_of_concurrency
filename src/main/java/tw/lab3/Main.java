package tw.lab3;


import java.util.concurrent.TimeUnit;

public class Main{
    public static void main(String[] args) {
        int consumer_count = 1;
        int producer_count = 2;
        int sum = consumer_count + producer_count;
        int buffer_size = 100;
        int products_count = 100;

        Buffer buffer = new Buffer(2*buffer_size);
        Th[] threads = new Th[consumer_count + producer_count];

        for (int i = 0; i < sum; i++) {
            Th thread = new Th(i < producer_count, buffer, buffer_size);
            threads[i] = thread;
            thread.start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (Exception ignored) {}
        int[] arr = new int[sum];
        for (int i=0; i<sum;i++){
            threads[i].running = false;
            arr[i] = threads[i].count;
        }
        for (int i = 0; i < sum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }
        for (int i=0; i<sum;i++){
            if (i < producer_count){
                System.out.println("Producer " + i + " produced " + threads[i].count);
            }
            else {
                System.out.println("Consumer " + i + " consumed " + threads[i].count);

            }
        }
    }
}