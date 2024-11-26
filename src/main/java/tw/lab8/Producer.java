package tw.lab8;

import org.jcsp.lang.*;
// import random
import java.util.Random;

public class Producer implements CSProcess{
    private final One2OneChannelInt channel;
    private final One2OneChannelInt feedback;
    private final int max_produce;
    private final Random random = new Random();
    boolean running = true;

    public Producer(One2OneChannelInt out, One2OneChannelInt feedback, int max_produce) {
        this.channel = out;
        this.feedback = feedback;
        this.max_produce = max_produce;
    }
    @Override
    public void run() {
        int item, status;
        while (running) {
            item = random.nextInt(max_produce) + 1;
            do {
                channel.out().write(item);
                status = feedback.in().read();
            } while (status == 0);
        }
        channel.out().write(-1);
        System.out.println("Producer ended.");
    }
}
