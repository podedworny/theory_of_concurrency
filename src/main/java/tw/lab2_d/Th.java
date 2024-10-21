package tw.lab2_d;


import java.util.Random;

public class Th extends Thread{
    boolean status; // true - producer, false - consumer
    Buffer buffer;
    int max_prod;
    Random random = new Random();
    static int id = 0;
    private final int thread_id = id++;
    public Th (boolean status, Buffer buffer, int max_prod){
        this.status = status;
        this.buffer = buffer;
        this.max_prod = max_prod;
    }

    @Override
    public void run() {
        if (status) {
            for (;;) {
                buffer.produce(random.nextInt(max_prod - 1) + 1, thread_id);
            }
        } else {
            for (;;) {
                buffer.consume(random.nextInt(max_prod - 1) + 1, thread_id);
            }
        }
    }
}
