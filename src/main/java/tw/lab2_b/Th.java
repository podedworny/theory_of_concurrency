package tw.lab2_b;

import java.util.Random;

public class Th extends Thread{
    boolean status; // true - reader, false - writer
    Library library;
    int count;
    static int id = 1;
    int my_ID = id++;
    public Th (boolean status, Library library, int count){
        this.status = status;
        this.library = library;
        this.count = count;
    }

    @Override
    public void run() {
        if (status) {
            for (int i = 0; i < count; i++) {
                library.start_reading(my_ID);

                try {
                    sleep(new Random().nextInt(10) * 100);
                }
                catch (Exception ignored){ }

                library.stop_reading(my_ID);

                try {
                    sleep(new Random().nextInt(10) * 100);
                }
                catch (Exception ignored){ }
                }
        } else {
            for (int i = 0; i < count; i++) {
                library.start_writing(my_ID);

                try {
                    sleep(new Random().nextInt(10) * 100);
                }
                catch (Exception ignored){ }

                library.stop_writing(my_ID);

                try {
                    sleep(new Random().nextInt(10) * 100);
                }
                catch (Exception ignored){ }
            }
        }
    }
}