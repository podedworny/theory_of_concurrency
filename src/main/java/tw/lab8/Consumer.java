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
    private final One2OneChannelInt managerChannels;
    private final One2OneChannelInt managerRequestChannels;

    public Consumer(One2OneChannelInt[] req, One2OneChannelInt[] in, Random random, One2OneChannelInt managerChannels, One2OneChannelInt managerRequestChannels) {
        this.req = req;
        this.in = in;
        this.random = random;
        this.managerChannels = managerChannels;
        this.managerRequestChannels = managerRequestChannels;
    }

    @Override
    public void run() {
        int item;
        int length = in.length;
        while (running) {
            int random_buffer = random.nextInt(length);
            managerChannels.out().write(random_buffer);
            random_buffer = managerRequestChannels.in().read();
            if (random_buffer == -1) {
                System.out.println("Consumer " + id + " could not find a free buffer");
                rejected_count++;
                try {
                    Thread.sleep(random.nextInt(200)+200);
                } catch (InterruptedException ignored) {}
                continue;
            }
//            System.out.println("Consumer " + id + " requesting from buffer " + (random_buffer+1));
            req[random_buffer].out().write(0); // Request data - blocks until data is available
            item = in[random_buffer].in().read(); // Read data
            managerChannels.out().write(-1*random_buffer-1);
//            System.out.println("Consumer " + id + " received " + item + " from buffer " + (random_buffer+1));
            action_count++;
            try {
                Thread.sleep(random.nextInt(200)+200);
            } catch (InterruptedException ignored) {}
        }
    }
}
