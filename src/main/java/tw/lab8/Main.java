package tw.lab8;

import org.jcsp.lang.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int BUFFER_SIZE = 10;
    private static final int PRODUCER_COUNT = 5;
    private static final int CONSUMER_COUNT = 5;
    private static final int BUFFER_COUNT = PRODUCER_COUNT;

    public static void main(String[] args) {
        Random random = new Random(42);
        One2OneChannelInt[] producerChannels = new One2OneChannelInt[PRODUCER_COUNT];
        One2OneChannelInt[] consumerChannels = new One2OneChannelInt[BUFFER_COUNT];
        One2OneChannelInt[] consumerRequestChannels = new One2OneChannelInt[BUFFER_COUNT];
        One2OneChannelInt[] bufferChannels = new One2OneChannelInt[BUFFER_COUNT];
        One2OneChannelInt[] bufferRequestChannels = new One2OneChannelInt[BUFFER_COUNT];
        boolean[] freeBuffer = new boolean[BUFFER_COUNT];
        ArrayList<CSProcess> processes = new ArrayList<CSProcess>();

        for (int i = 0; i < PRODUCER_COUNT; i++) {
            // Create producer channels
            producerChannels[i] = Channel.one2oneInt();
            // Create producer processes
            processes.add(new Producer(producerChannels[i], random));
            // Create buffer channels
            bufferChannels[i] = Channel.one2oneInt();
            bufferRequestChannels[i] = Channel.one2oneInt();
        }

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            processes.add(new Consumer(consumerRequestChannels, consumerChannels, random));
            // Create consumer channels
            consumerChannels[i] = Channel.one2oneInt();
            consumerRequestChannels[i] = Channel.one2oneInt();
        }

        for (int i = 1; i < BUFFER_COUNT; i++) {
            processes.add(new Buffer(consumerRequestChannels, consumerChannels, producerChannels[i], bufferChannels[i-1],
                    bufferChannels[i], bufferRequestChannels[i-1], bufferRequestChannels[i], BUFFER_SIZE));
        }
        processes.add(new Buffer(consumerRequestChannels, consumerChannels, producerChannels[0], bufferChannels[BUFFER_COUNT-1],
                bufferChannels[0], bufferRequestChannels[BUFFER_COUNT-1], bufferRequestChannels[0], BUFFER_SIZE));

        Parallel par = new Parallel(processes.toArray(new CSProcess[0]));
        Thread thread = new Thread(() -> new Main().ggg(par));
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (Exception ignored) {
        }

        System.out.println("Time's up!");

        for (CSProcess process : processes) {
            if (process instanceof Consumer) {
                System.out.println("Consumer " + ((Consumer) process).id + " consumed " + ((Consumer) process).action_count + " items.");
            }
            if (process instanceof Producer) {
                System.out.println("Producer " + ((Producer) process).id + " produced " + ((Producer) process).action_count + " items.");
            }
            if (process instanceof Buffer) {
                System.out.println("Buffer " + ((Buffer) process).id + " processed " + ((Buffer) process).action_count + " items.");
            }
        }
        for (CSProcess process : processes){
            if (process instanceof Consumer){
                System.out.println("Consumer " + ((Consumer) process).id + " rejected " + ((Consumer) process).rejected_count + " items.");
            }
        }



    }
    public void ggg(Parallel par) {
        par.run();
    }
}
