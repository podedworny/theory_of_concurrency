package tw.lab5;


import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int consumer_count = 5;
        int producer_count = 1;
        int sum = consumer_count + producer_count;
        int max_product = 50;
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
            TimeUnit.SECONDS.sleep(60);
        }
        catch (Exception ignored) {}

        int[] arr = new int[sum];
        int[] arr2 = new int[sum];
        long[] arr3 = new long[sum];

        for (int i=0; i<sum;i++){
            threads[i].running = false;
            threads[i].interrupt();
            arr[i] = threads[i].count;
            arr2[i] = threads[i].sum;
            arr3[i] = threads[i].time / threads[i].count;
        }

        for (int i = 0; i < sum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }

        int prod_min_time = Arrays.stream(arr3).limit(producer_count).mapToInt(x -> (int) x).min().orElse(-1);
        int prod_max_time = Arrays.stream(arr3).limit(producer_count).mapToInt(x -> (int) x).max().orElse(-1);
        int cons_min_time = Arrays.stream(arr3).skip(producer_count).mapToInt(x -> (int) x).min().orElse(-1);
        int cons_max_time = Arrays.stream(arr3).skip(producer_count).mapToInt(x -> (int) x).max().orElse(-1);
        int prod_min_count = Arrays.stream(arr).limit(producer_count).min().orElse(-1);
        int prod_max_count = Arrays.stream(arr).limit(producer_count).max().orElse(-1);
        int cons_min_count = Arrays.stream(arr).skip(producer_count).min().orElse(-1);
        int cons_max_count = Arrays.stream(arr).skip(producer_count).max().orElse(-1);
        int mix_min_time = Math.min(prod_min_time, cons_min_time);
        int mix_max_time = Math.max(prod_max_time, cons_max_time);
        int mix_min_count = Math.min(prod_min_count, cons_min_count);
        int mix_max_count = Math.max(prod_max_count, cons_max_count);

        for (int i=0; i<sum;i++){
            if (i < producer_count){
                System.out.println("Producer " + i + " produced " + arr[i] + " times. Sum: " + arr2[i] + ". Average time: " + arr3[i] + " ns");

            }
            else {
                System.out.println("Consumer " + (i - producer_count) + " consumed " + arr[i] + " times. Sum: " + arr2[i] + ". Average time: " + arr3[i] + " ns");

            }
        }

        long sum1 = 0;
        long sum2 = 0;
        for (int i=0; i<sum;i++){
            sum1 += threads[i].count;
            sum2 += threads[i].time / threads[i].count;
        }
        System.out.println("Average count of entering monitor: " + sum1/sum);
        System.out.println("Average time of waiting: " + sum2/sum + " ns");
        System.out.println("Producer min time: " + prod_min_time + " ns");
        System.out.println("Producer max time: " + prod_max_time + " ns");
        System.out.println("Producer difference: " + (prod_max_time - prod_min_time) + " ns");
        System.out.println("Consumer min time: " + cons_min_time + " ns");
        System.out.println("Consumer max time: " + cons_max_time + " ns");
        System.out.println("Consumer difference: " + (cons_max_time - cons_min_time) + " ns");
        System.out.println("Producer min count: " + prod_min_count);
        System.out.println("Producer max count: " + prod_max_count);
        System.out.println("Producer difference: " + (prod_max_count - prod_min_count));
        System.out.println("Consumer min count: " + cons_min_count);
        System.out.println("Consumer max count: " + cons_max_count);
        System.out.println("Consumer difference: " + (cons_max_count - cons_min_count));
        System.out.println("Min time: " + mix_min_time + " ns");
        System.out.println("Max time: " + mix_max_time + " ns");
        System.out.println("Difference: " + (mix_max_time - mix_min_time) + " ns");
        System.out.println("Min count: " + mix_min_count);
        System.out.println("Max count: " + mix_max_count);
        System.out.println("Difference: " + (mix_max_count - mix_min_count));
    }
}