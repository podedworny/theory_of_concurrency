package tw.lab2;

public class Th extends Thread{
    boolean status;
    Bufor v;
    int count;
    public Th (boolean status, Bufor v, int count){
        this.status = status;
        this.v = v;
        this.count = count;
    }

    @Override
    public void run() {
        if (status) {
            for (int i = 0; i < count; i++) {
                v.increment();

            }
        } else {
            for (int i = 0; i < count; i++) {
                v.decrement();
            }
        }
    }
}
