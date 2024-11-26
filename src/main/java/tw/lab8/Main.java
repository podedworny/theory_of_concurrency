package tw.lab8;

import org.jcsp.lang.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int MAX_PRODUCE = 50;
    private static final int BUFFER_SIZE = MAX_PRODUCE * 2;
    private static final int PRODUCER_COUNT = 10;
    private static final int CONSUMER_COUNT = 10;
    private static final int BUFFER_COUNT = 6;

    public static void main(String[] args) {
        One2OneChannelInt[] producerChannels = new One2OneChannelInt[PRODUCER_COUNT];
        One2OneChannelInt[] producerFeedbackChannels = new One2OneChannelInt[PRODUCER_COUNT];
        One2OneChannelInt[] consumerChannels = new One2OneChannelInt[CONSUMER_COUNT];
        One2OneChannelInt[] consumerRequestChannels = new One2OneChannelInt[CONSUMER_COUNT];
        One2OneChannelInt[] bufferChannels = new One2OneChannelInt[BUFFER_COUNT-1];
        One2OneChannelInt[] bufferFeedbackChannels = new One2OneChannelInt[BUFFER_COUNT-1];

        ArrayList<CSProcess> processes = new ArrayList<CSProcess>();

        for (int i = 0; i < PRODUCER_COUNT; i++) {
            producerChannels[i] = Channel.one2oneInt();
            producerFeedbackChannels[i] = Channel.one2oneInt();
            processes.add(new Producer(producerChannels[i], producerFeedbackChannels[i], MAX_PRODUCE));
        }
        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumerChannels[i] = Channel.one2oneInt();
            consumerRequestChannels[i] = Channel.one2oneInt();
            processes.add(new Consumer(consumerRequestChannels[i], consumerChannels[i], MAX_PRODUCE));
        }

        for (int i = 0; i < BUFFER_COUNT-1; i++) {
            bufferChannels[i] = Channel.one2oneInt();
            bufferFeedbackChannels[i] = Channel.one2oneInt();
        }
        // create first buffer
        processes.add(new Buffer(producerChannels, producerFeedbackChannels, bufferChannels[0], bufferFeedbackChannels[0], Status.FIRST, BUFFER_SIZE));
        // create middle buffer
        for (int i = 1; i < BUFFER_COUNT-1; i++) {
            processes.add(new Buffer(bufferChannels[i-1], bufferChannels[i], bufferFeedbackChannels[i-1], bufferFeedbackChannels[i], Status.MIDDLE, BUFFER_SIZE));
        }
        // create last buffer
        processes.add(new Buffer(consumerChannels, consumerRequestChannels, bufferChannels[BUFFER_COUNT-2], bufferFeedbackChannels[BUFFER_COUNT-2], Status.LAST, BUFFER_SIZE));

        Parallel par = new Parallel(processes.toArray(new CSProcess[0]));
        Thread thread = new Thread(() -> new Main().ggg(par));
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(10);
        }
        catch (Exception ignored) {}
        par.destroy();
        System.out.println("Time's up!");
        for (CSProcess process : processes) {
            if (process instanceof Producer) {
                ((Producer) process).running = false;
                System.out.println("Producer ended.");
            }
            else if (process instanceof Consumer) {
                ((Consumer) process).running = false;
            }
        }
        for (CSProcess process : processes) {
            if (process instanceof Consumer) {
                System.out.println("Consumer " + ((Consumer) process).id + " consumed " + ((Consumer) process).action_count + " items.");
            }
        }
    }
    public void ggg(Parallel par) {
        par.run();
    }
}
