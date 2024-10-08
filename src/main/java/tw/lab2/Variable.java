package tw.lab2;

public class Variable {
    private int x;
    public Variable(){
        x = 0;
    }
    public void increment(){
        x++;
    }
    public void decrement(){
        x--;
    }
    public int getValue(){
        return x;
    }
}
