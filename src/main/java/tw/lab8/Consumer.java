package tw.lab8;

import org.jcsp.lang.*;
import java.util.Random;

public class Consumer implements CSProcess{
    private final One2OneChannelInt req;
    private final One2OneChannelInt in;
    private final int max_consume;
    boolean running = true;
    private Random random = new Random();
    private static int idCounter = 1;
    int id = idCounter++;
    int action_count = 0;
    public Consumer(One2OneChannelInt req, One2OneChannelInt in, int max_consume) {
        this.req = req;
        this.in = in;
        this.max_consume = max_consume;
    }
    @Override
    public void run() {
        int item;
        while (running) {
            int count = random.nextInt(max_consume) + 1;
            do {
                req.out().write(count); // Request data - blocks until data is available
                item = in.in().read();
            } while (item == -1);
//            System.out.println("Consumer " + id + " consumed " + count + " items.");
            action_count++;
        }
    }
}
