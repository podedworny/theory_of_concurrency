package tw.incorrect_4cond;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Queue<Integer> queue;
    private final int buffer_size;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition first_prod = lock.newCondition();
    private final Condition first_cons = lock.newCondition();
    private final Condition next_prod = lock.newCondition();
    private final Condition next_cons = lock.newCondition();

    public Buffer(int buffer_size){
        this.queue = new LinkedList<>();
        this.buffer_size = buffer_size;
    }

    public void produce(int x, int thread_id){
        lock.lock();
        try {
            while (lock.hasWaiters(first_prod)) next_prod.await();

            while (queue.size() + x > buffer_size) first_prod.await();

//            System.out.println("Producer " + thread_id + " produced " + x + " products");

            for (int i=0; i<x; i++) queue.add(x);

            next_prod.signal();
            first_cons.signal();
        }
        catch (Exception ignored){ }
        finally {
            lock.unlock();
        }
    }
    public void consume(int x, int thread_id){
        lock.lock();
        try {
            while (lock.hasWaiters(first_cons)) next_cons.await();

            while (queue.size() < x) first_cons.await();

//            System.out.println("Consumer " + thread_id + " consumed " + x + " products");

            for (int i=0; i<x; i++) queue.remove();

            next_cons.signal();
            first_prod.signal();
        }
        catch (Exception ignored){ }
        finally {
            lock.unlock();
        }
    }
}
