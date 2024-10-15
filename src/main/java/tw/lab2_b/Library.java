package tw.lab2_b;


public class Library {
    private int reader_count = 0;
    private boolean writing = false;
    public synchronized void start_reading(int my_ID){
        while (writing){
            try {
                wait();
            }
            catch (Exception ignored){ }
        }
        System.out.println("Reader " + my_ID + " is reading");
        reader_count++;
    }
    public synchronized void stop_reading(int my_ID){
        System.out.println("Reader " + my_ID + " stopped reading");
        reader_count--;
        if (reader_count == 0){
            notifyAll();
        }
    }
    public synchronized void start_writing(int my_ID){
        while (writing || reader_count > 0){
            try {
                wait();
            }
            catch (Exception ignored){}
        }
        System.out.println("Writer " + my_ID + " is writing");
        writing = true;

    }

    public synchronized void stop_writing(int my_ID){
        System.out.println("Writer " + my_ID + " stopped writing");
        writing = false;
        notifyAll();
    }
}