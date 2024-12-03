package tw.lab8;

import org.jcsp.lang.*;

import java.util.Random;

public class Producer implements CSProcess{
    private final One2OneChannelInt channel;
    Random random;
    boolean running = true;
    private static int idCounter = 1;
    int id = idCounter++;
    int action_count = 0;

    public Producer(One2OneChannelInt out, Random random) {
        this.channel = out;
        this.random = random;
    }

    @Override
    public void run() {
        while (running) {
//            System.out.println("Producer " + id + " producing");
            channel.out().write(random.nextInt(100000));
            action_count++;
            try {
                Thread.sleep(random.nextInt(200)+200); // Sleep to simulate work
            } catch (InterruptedException ignored) { }
        }
        channel.out().write(-1);
        System.out.println("Producer ended.");
    }
}
