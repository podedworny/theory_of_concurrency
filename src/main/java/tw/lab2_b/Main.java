package tw.lab2_b;

public class Main {
    public static void main(String[] args) {
        int reader_count = 3;
        int writer_count = 2;
        int sum = reader_count + writer_count;
        int products_count = 10;

        Library library = new Library();
        Th[] threads = new Th[reader_count + writer_count];

        for (int i = 0; i < sum; i++) {
            Th thread = new Th(i < writer_count, library, products_count);
            threads[i] = thread;
            thread.start();
        }

        for (int i = 0; i < sum; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) { }
        }
    }
}
