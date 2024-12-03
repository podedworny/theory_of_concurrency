package tw.lab8;

import org.jcsp.lang.*;
import java.util.Random;

public class Consumer implements CSProcess{
    private final One2OneChannelInt[] req;
    private final One2OneChannelInt[] in;
    boolean running = true;
    private Random random;
    private static int idCounter = 1;
    int id = idCounter++;
    int action_count = 0;
    int rejected_count = 0;

    public Consumer(One2OneChannelInt[] req, One2OneChannelInt[] in, Random random) {
        this.req = req;
        this.in = in;
        this.random = random;
    }

    @Override
    public void run() {
        int item;
        int length = in.length;
        while (running) {
            int random_buffer = random.nextInt(length);
            System.out.println("Consumer " + id + " requesting from buffer " + (random_buffer+1));
            req[random_buffer].out().write(0); // Request data - blocks until data is available
            System.out.println("Consumer " + id + " requested from buffer " + (random_buffer+1));
            item = in[random_buffer].in().read(); // Read data
            if (item == -1){
                System.out.println("Consumer " + id + " rejected from buffer " + (random_buffer+1));
                rejected_count++;
            }
            else {
                System.out.println("Consumer " + id + " received " + item + " from buffer " + (random_buffer+1));
                action_count++;
            }
            try {
                Thread.sleep(random.nextInt(200)+200);
            } catch (InterruptedException ignored) {}
        }
    }
}
