package tw.lab2_c;

public class Main {
    public static void main(String[] args) {
        int professor_count = 5;
        Table table = new Table();
        Professor[] professors = new Professor[professor_count];

        for (int i = 0; i < professor_count; i++) {
            Professor professor = new Professor(table);
            professors[i] = professor;
            professor.start();
        }

        for (int i = 0; i < professor_count; i++) {
            try {
                professors[i].join();
            } catch (InterruptedException ignored) { }
        }
    }
}
