package tw.lab2;

public class Th implements Runnable{
    boolean status = true;
    Variable v;
    public Th (boolean status, Variable v){
        this.status = status;
        this.v = v;
    }
    public void run(){
        v.decrement();
    }
}
