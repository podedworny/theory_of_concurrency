package tw.lab1;

public class Variab {
    private int x = 0;
    public Variab (int x){
        this.x = x;
    }
    public synchronized void increment(){
        x++;
    }
    public int getX(){
        return x;
    }
    public synchronized void decrement(){
        x--;
    }
}
