package tw.lab2_c;

public class Table {
    private boolean[] forks = new boolean[5];
    private int x = 0;
    public synchronized void takeForks(int id) {
        while (x == 4) {
            try {
                wait();
            } catch (InterruptedException ignored) { }
        }
        x++;
        while(forks[id]) {
            try {
                wait();
            } catch (InterruptedException ignored) { }
        }
        forks[id] = true;
        while(forks[(id + 1) % 5]) {
            try {
                wait();
            } catch (InterruptedException ignored) { }
        }
        System.out.println("Professor " + id + " is eating");
        forks[(id + 1) % 5] = true;
    }

    public synchronized void putForks(int id) {
        System.out.println("Professor " + id + " is thinking");
        forks[id] = false;
        notifyAll();
        forks[(id + 1) % 5] = false;
        notifyAll();
        x--;
        notifyAll();
    }
}
