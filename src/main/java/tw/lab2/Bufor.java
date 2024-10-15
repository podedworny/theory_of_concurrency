package tw.lab2;

import java.util.LinkedList;
import java.util.Queue;

public class Bufor {
    private final Queue<Integer> queue;
    private final int buffer_size;
    public Bufor(int buffer_size){
        this.queue = new LinkedList<>();
        this.buffer_size = buffer_size;
    }
    public synchronized void produce(int x){
        while (queue.size() == buffer_size){
            try {
                wait();
            }
            catch (Exception ignored){ }
        }
        queue.add(x);
        notifyAll();
    }
    public synchronized int getX(){
        return queue.size();
    }
    public synchronized void consume(){
        while (queue.isEmpty()){
            try {
                wait();
            }
            catch (Exception ignored){}
        }
        queue.poll();
        notifyAll();
    }
}
