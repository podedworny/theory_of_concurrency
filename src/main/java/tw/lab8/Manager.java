package tw.lab8;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

import java.util.Arrays;

public class Manager implements CSProcess {
    final boolean[] freeBuffer;
    final One2OneChannelInt[] managerChannels;
    final One2OneChannelInt[] managerRequestChannels;

    public Manager(boolean[] freeBuffer, One2OneChannelInt[] managerChannels, One2OneChannelInt[] managerRequestChannels) {
        this.freeBuffer = freeBuffer;
        Arrays.fill(freeBuffer, true);
        this.managerChannels = managerChannels;
        this.managerRequestChannels = managerRequestChannels;
    }

    public int getFreeBuffer(int index) {
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

    public void toggleFreeBuffer(int index) {
        freeBuffer[index] = true;
    }


    @Override
    public void run() {
        Guard[] guards = new Guard[managerChannels.length];
        for (int i = 0; i < managerChannels.length; i++) {
            guards[i] = managerChannels[i].in();
        }
        Alternative alt = new Alternative(guards);
        while (true) {
            int index = alt.select();
            int i = managerChannels[index].in().read();
            if (i >= 0) {
                i = getFreeBuffer(i);
                managerRequestChannels[index].out().write(i);
            }
            else {
                i = i *(-1) -1;
                toggleFreeBuffer(i);
            }
        }
    }
}
