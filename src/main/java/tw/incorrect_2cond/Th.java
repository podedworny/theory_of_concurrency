package tw.incorrect_2cond;


import java.util.Random;

public class Th extends Thread{
    boolean status; // true - producer, false - consumer
    Buffer buffer;
    int max_size;
    Random random;
    boolean running = true;
    int count = 0;
    int prod;
    int sum=0;
    boolean test = true;
    long time = 0;

    public Th (boolean status, Buffer buffer, int max_size, int prod, Random random){
        this.status = status;
        this.buffer = buffer;
        this.max_size = max_size;
        this.prod = prod;
        this.random = random;
    }

    @Override
    public void run() {
        int c;
        long startTime;
        if (status) {
            while (running) {
                c = test ? prod : random.nextInt(max_size) + 1;
                startTime = System.nanoTime();
                buffer.produce(c);
                time += (System.nanoTime() - startTime);
                count++;
                sum+=c;
            }
        } else {
            while (running) {
                c = test ? prod : random.nextInt(max_size) + 1;
                startTime = System.nanoTime();
                buffer.consume(c);
                time += (System.nanoTime() - startTime);
                count++;
                sum+=c;
            }
        }
    }
}
