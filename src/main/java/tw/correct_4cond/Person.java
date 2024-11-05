package tw.correct_4cond;


import java.util.Random;

public class Person extends Thread {
    boolean status; // true - producer, false - consumer
    Buffer buffer;
    int max_prod;
    Random random;
    boolean running = true;
    static int id = 0;
    private final int thread_id = id++;
    int count = 0;
    int prod;
    int sum=0;
    boolean test = false;
    long time;

    public Person (boolean status, Buffer buffer, int max_prod, int prod, Random random){
        this.status = status;
        this.buffer = buffer;
        this.max_prod = max_prod;
        this.prod = prod;
        this.random = random;
    }

    @Override
    public void run() {
        int c;
        long startTime;
        if (status) {
            while (running) {
                c = test ? prod : random.nextInt(max_prod) + 1;
                startTime = System.nanoTime();
                buffer.produce(c, thread_id);
                time += (System.nanoTime() - startTime);
                count++;
                sum+=c;
            }
        } else {
            while (running) {
                c = test ? prod : random.nextInt(max_prod) + 1;
                startTime = System.nanoTime();
                buffer.consume(c, thread_id);
                time += (System.nanoTime() - startTime);
                count++;
                sum+=c;
            }
        }
    }
}
