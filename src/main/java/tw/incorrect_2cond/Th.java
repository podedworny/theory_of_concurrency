package tw.incorrect_2cond;


import java.util.Random;

public class Th extends Thread{
    boolean status; // true - producer, false - consumer
    Buffer buffer;
    int max_size;
    Random random = new Random();
    boolean running = true;
    int count = 0;
    int prod;
    int sum=0;
    boolean test = false;

    public Th (boolean status, Buffer buffer, int max_size, int prod){
        this.status = status;
        this.buffer = buffer;
        this.max_size = max_size;
        this.prod = prod;
    }

    @Override
    public void run() {
        int c;
        if (status) {
            while (running) {
                c = test ? prod : random.nextInt(max_size) + 1;
                buffer.produce(c);
                count++;
                sum+=c;
            }
        } else {
            while (running) {
                c = test ? prod : random.nextInt(max_size) + 1;
                buffer.consume(c);
                count++;
                sum+=c;
            }
        }
    }
}
