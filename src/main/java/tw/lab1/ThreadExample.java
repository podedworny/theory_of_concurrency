package tw.lab1;

public class ThreadExample extends Thread{
    boolean status;
    Variab v;
    int count;
    public ThreadExample (boolean status, Variab v, int count){
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
