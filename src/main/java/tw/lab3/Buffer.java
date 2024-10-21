package tw.lab3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Queue<Integer> queue;
    private final int buffer_size;
    private final Lock lock = new ReentrantLock();
    private final Condition prod = lock.newCondition();
    private final Condition cons = lock.newCondition();

    public Buffer(int buffer_size) {
        this.queue = new LinkedList<>();
        this.buffer_size = buffer_size;
    }

    public void produce(int x, int thread_id){
        lock.lock();
        try {
            while (queue.size() + x > buffer_size){
                prod.await();
            }
            System.out.println("Producer " + thread_id + " produced " + x + " products");
            for (int i=0; i<x; i++) queue.add(x);
            prod.signal();
            cons.signal();

        }
        catch (Exception ignored) {}
        finally {
            lock.unlock();
        }
    }

}
