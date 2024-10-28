package tw.correct_4cond;

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

    private boolean first_prod_flag = false;
    private boolean first_cons_flag = false;

    public Buffer(int buffer_size){
        this.queue = new LinkedList<>();
        this.buffer_size = buffer_size;
    }

    public void produce(int x, int thread_id){
        lock.lock();
        try {
            while (first_prod_flag) next_prod.await();

            while (queue.size() + x > buffer_size){
                first_prod_flag = true;
                first_prod.await();
            }

//            System.out.println("Producer " + thread_id + " produced " + x + " products");

            for (int i=0; i<x; i++) queue.add(x);
            first_prod_flag = false;
            next_prod.signal();
            first_cons.signal();
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        finally {
            lock.unlock();
        }
    }

    public void consume(int x, int thread_id){
        lock.lock();
        try {
            while (first_cons_flag) next_cons.await();

            while (queue.size() < x){
                first_cons_flag = true;
                first_cons.await();
            }

//            System.out.println("Consumer " + thread_id + " consumed " + x + " products");

            for (int i=0; i<x; i++) queue.remove();

            first_cons_flag = false;
            next_cons.signal();
            first_prod.signal();
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        finally {
            lock.unlock();
        }
    }
}