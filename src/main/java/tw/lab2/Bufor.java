package tw.lab2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bufor {
    private final Queue<Integer> queue;
    private final int buffer_size;
    private final Lock lock = new ReentrantLock();
    private final Condition cons = lock.newCondition();
    private final Condition prod = lock.newCondition();
    public Bufor(int buffer_size){
        this.queue = new LinkedList<>();
        this.buffer_size = buffer_size;
    }
    public void produce(int x){
        lock.lock();
        try {
            while (queue.size() + x > buffer_size) {
                try {
                    prod.await();
                } catch (Exception ignored) {
                }
            }
            for (int i=0; i<x;i++) queue.add(x);
            System.out.println(queue.size());
            cons.signal();
        }
        catch (Exception ignored) {}
        finally {
            lock.unlock();
        }
    }
    public int getX(){
        int rer = 0;
        lock.lock();
        try {
            rer = queue.size();
        }
        catch (Exception ignored) {}
        finally {
            lock.unlock();
        }
        return rer;
    }
    public void consume(int x){
        lock.lock();
        try {
            while (queue.size() < x) {
                try {
                    cons.await();
                } catch (Exception ignored) {
                }
            }
            for (int i=0; i<x; i++)  queue.poll();
            System.out.println(queue.size());
            prod.signal();
        }
        catch (Exception ignored) {}
        finally {
            lock.unlock();
        }
    }
}
