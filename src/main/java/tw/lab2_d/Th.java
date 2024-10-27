package tw.lab2_d;


import java.util.Random;

public class Th extends Thread{
    boolean status; // true - producer, false - consumer
    Buffer buffer;
    int max_prod;
    Random random = new Random();
    static int id = 0;
    public int count = 0;
    private final int thread_id = id++;
    boolean running = true;
    public Th (boolean status, Buffer buffer, int max_prod){
        this.status = status;
        this.buffer = buffer;
        this.max_prod = max_prod;
    }

    @Override
    public void run() {
        if (status) {
            while(running) {
                buffer.produce(random.nextInt(max_prod - 1) + 1, thread_id);
                count++;
            }
        } else {
            while(running) {
                buffer.consume(random.nextInt(max_prod - 1) + 1, thread_id);
                count++;
            }
        }
    }
}
