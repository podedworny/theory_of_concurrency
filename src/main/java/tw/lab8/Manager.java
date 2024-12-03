package tw.lab8;

import java.util.Arrays;

public class Manager {
    final boolean[] freeBuffer;

    public Manager(boolean[] freeBuffer) {
        this.freeBuffer = freeBuffer;
        Arrays.fill(freeBuffer, true);
    }

    public synchronized int getFreeBuffer(int index) {
        int new_index = index;
        while (!freeBuffer[new_index]) {
            new_index = (new_index + 1) % freeBuffer.length;
            if (new_index == index) {
                return -1;
            }
        }
        freeBuffer[new_index] = false;
        return new_index;
    }

    public synchronized void toggleFreeBuffer(int index) {
        freeBuffer[index] = true;
    }
}
