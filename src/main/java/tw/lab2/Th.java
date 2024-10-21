package tw.lab2;

public class Th extends Thread{
    boolean status; // true - producer, false - consumer
    Bufor bufor;
    int count;
    public Th (boolean status, Bufor bufor, int count){
        this.status = status;
        this.bufor = bufor;
        this.count = count;
    }

    @Override
    public void run() {
        if (status) {
            for (int i = 0; i < count; i++) {
                bufor.produce(i);

            }
        } else {
            for (int i = 0; i < count; i++) {
                bufor.consume();
            }
        }
    }
}