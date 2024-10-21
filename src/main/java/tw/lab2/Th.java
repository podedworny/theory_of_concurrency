package tw.lab2;
import java.util.Random;

public class Th extends Thread{
    boolean status; // true - producer, false - consumer
    Bufor bufor;
    int count;
    Random random = new Random();
    public Th (boolean status, Bufor bufor, int count){
        this.status = status;
        this.bufor = bufor;
        this.count = count;
    }

    @Override
    public void run() {
        if (status) {
            for (;;) {
                bufor.produce(random.nextInt(count)+1);

            }
        } else {
            for (;;) {
                bufor.consume(random.nextInt(count)+1);
            }
        }
    }
}
