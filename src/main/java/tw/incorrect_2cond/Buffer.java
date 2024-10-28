package tw.incorrect_2cond;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Queue<Integer> queue;
    private final int buffer_size;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cons = lock.newCondition();
    private final Condition prod = lock.newCondition();

    public Buffer(int buffer_size){
        this.queue = new LinkedList<>();
        this.buffer_size = buffer_size;
    }

    public void produce(int x){
        lock.lock();
        try {
            while (queue.size() + x > buffer_size) prod.await();

            for (int i=0; i<x;i++) queue.add(x);

            cons.signal();
        }
        catch (Exception ignored) {}
        finally {
            lock.unlock();
        }
    }

    public void consume(int x){
        lock.lock();
        try {
            while (queue.size() < x) cons.await();

            for (int i=0; i<x; i++)  queue.poll();

            prod.signal();
        }
        catch (Exception ignored) {}
        finally {
            lock.unlock();
        }
    }
}
