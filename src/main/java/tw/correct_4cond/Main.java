package tw.correct_4cond;


import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main{
    public static void main(String[] args) {
        int consumer_count = 5;
        int producer_count = 6;
        int sum = consumer_count + producer_count;
        int max_product = 100;
        Random random = new Random(42);

        Buffer buffer = new Buffer(2*max_product);
        Person[] threads = new Person[consumer_count + producer_count+1];

        for (int i = 0; i < sum; i++) {
            Person thread = new Person(i < producer_count, buffer, max_product, 1, random);
            threads[i] = thread;
            thread.start();
        }
        threads[sum] = new Person(false, buffer, max_product, 10, random);
        threads[sum].start();
        sum++;

        try {
            TimeUnit.SECONDS.sleep(120);
        }
        catch (Exception ignored) {}

        int[] arr = new int[sum];
        int[] arr2 = new int[sum];
        long[] arr3 = new long[sum];
        int s=0;
        for (int i=0; i<sum;i++){
            threads[i].running = false;
            threads[i].interrupt();
        }

        for (int i = 0; i < sum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }
        for (int i=0; i<sum;i++){
            arr[i] = threads[i].count;
            arr2[i] = threads[i].sum;
            arr3[i] = threads[i].time / threads[i].count;
            if(i!=0) s+=threads[i].sum;
        }
        for (int i=0; i<sum;i++){
            if (i < producer_count){
                System.out.println("Producer " + i + " produced " + arr[i] + " times. Sum: " + arr2[i] + ". Average time: " + arr3[i] + " ns");
            }
            else {
                System.out.println("Consumer " + (i - producer_count) + " consumed " + arr[i] + " times. Sum: " + arr2[i] + ". Average time: " + arr3[i] + " ns");

            }
        }

        //calculate avarage times of entering monitor and average times of waiting
        long sum1 = 0;
        long sum2 = 0;
        for (int i=0; i<sum;i++){
            sum1 += threads[i].count;
            sum2 += threads[i].time / threads[i].count;
        }
        System.out.println("Average count of entering monitor: " + sum1/sum);
        System.out.println("Average time of waiting: " + sum2/sum + " ns");
    }
}