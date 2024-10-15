package tw.lab2_c;


import java.util.Random;

public class Professor extends Thread {
    Table table;
    static int id = 0;
    int my_ID = id++;

    public Professor(Table table) {
        this.table = table;
    }

    @Override
    public void run() {
        while (true) {
            table.takeForks(my_ID);
            try {
                sleep(new Random().nextInt(10) * 1000);
            } catch (InterruptedException ignored) { }
            table.putForks(my_ID);

            try {
                sleep(new Random().nextInt(10) * 1000);
            } catch (InterruptedException ignored) { }
        }
    }
}