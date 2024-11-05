package tw.lab5;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Queue<Integer> queue;
    private final int buffer_size;

    private final ReentrantLock lock_prod = new ReentrantLock();
    private final ReentrantLock lock_cons = new ReentrantLock();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();

    public Buffer(int buffer_size){
        this.queue = new LinkedList<>();
        this.buffer_size = buffer_size;
    }

    public void produce(int x, int thread_id){
        lock_prod.lock();
        try {
            lock.lock();
                try {
                    while (queue.size() + x > buffer_size){
                        cond.await();
                    }
                    for (int i=0; i<x; i++) queue.add(x);
                    cond.signalAll();
                }
                catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                finally {
                    lock.unlock();
            }
        }
        catch (Exception e){
            Thread.currentThread().interrupt();
        }
        finally {
            lock_prod.unlock();
        }
    }

    public void consume(int x, int thread_id){
        lock_cons.lock();
        try {
            lock.lock();
                try {
                    while (queue.size() < x){
                        cond.await();
                    }
                    for (int i=0; i<x; i++) queue.poll();
                    cond.signalAll();
                }
                catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                finally {
                    lock.unlock();
                }
        }
        catch (Exception e){
            Thread.currentThread().interrupt();
        }
        finally {
            lock_cons.unlock();
        }

    }
}