package tw.lab3;


import java.util.Random;

public class Th extends Thread{
    boolean status; // true - producer, false - consumer
    Buffer buffer;
    int count;
    Random random = new Random();
    public Th (boolean status, Buffer buffer, int count){
        this.status = status;
        this.buffer = buffer;
        this.count = count;
    }

    @Override
    public void run() {
        if (status) {
            for (;;) {
                buffer.produce(random.nextInt(count)+1);

            }
        } else {
            for (;;) {
                buffer.consume(random.nextInt(count)+1);
            }
        }
    }
}
