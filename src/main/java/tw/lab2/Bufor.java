package tw.lab2;

public class Bufor {
    private int x = 0;
    public Bufor(int x){
        this.x = x;
    }
    public synchronized void increment(){
        while (x == 0){
            try {
                wait();
            }
            catch (Exception ignored){ }
        }
        x++;
        notify();
    }
    public synchronized int getX(){
        return x;
    }
    public synchronized void decrement(){
        while (x != 0){
            try {
                wait();
            }
            catch (Exception ignored){}
        }
        x--;
        notify();
    }
}
