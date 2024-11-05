package tw.lab5;

import java.util.Random;

public class Person extends Thread {
    boolean status; // true - producer, false - consumer
    Buffer buffer;
    int max_prod;
    Random random = new Random();
    boolean running = true;
    static int id = 0;
    private final int thread_id = id++;
    int count = 0;
    int prod;
    int sum=0;
    boolean test = true;

    public Person (boolean status, Buffer buffer, int max_prod, int prod){
        this.status = status;
        this.buffer = buffer;
        this.max_prod = max_prod;
        this.prod = prod;
    }

    @Override
    public void run() {
        int c;
        if (status) {
            while (running) {
                c = test ? prod : random.nextInt(max_prod) + 1;
                buffer.produce(c, thread_id);
                count++;
                sum+=c;
            }
        } else {
            while (running) {
                c = test ? prod : random.nextInt(max_prod) + 1;
                buffer.consume(c, thread_id);
                count++;
                sum+=c;
            }
        }
    }
}
