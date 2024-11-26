package tw.lab8;

import org.jcsp.lang.*;
import java.util.LinkedList;

enum Status {
    FIRST, LAST, MIDDLE
}

public class Buffer implements CSProcess{
    private  One2OneChannelInt[] req;
    private  One2OneChannelInt[] in;
    private  One2OneChannelInt[] out;
    private One2OneChannelInt in_buffer;
    private One2OneChannelInt out_buffer;
    private One2OneChannelInt in_f_buffer;
    private One2OneChannelInt out_f_buffer;
    Status status;
    int size;
    boolean running = true;
    LinkedList<Integer> buffer_data = new LinkedList<Integer>();

    public Buffer(One2OneChannelInt[] f1, One2OneChannelInt[] f2, One2OneChannelInt io_buffer, One2OneChannelInt io_f_buffer, Status status, int size){ // first buffer
        this.size = size;
        this.status = status;
        if (status == Status.FIRST) {
            this.in = f1;
            this.out = f2;
            this.out_buffer = io_buffer;
            this.out_f_buffer = io_f_buffer;
        } else if (status == Status.LAST) {
            this.req = f2;
            this.out = f1;
            this.in_buffer = io_buffer;
            this.in_f_buffer = io_f_buffer;
        }
    }

    public Buffer(One2OneChannelInt in_buffer, One2OneChannelInt out_buffer, One2OneChannelInt in_f_buffer, One2OneChannelInt out_f_buffer, Status status, int size){ // middle buffer
        this.in_buffer = in_buffer;
        this.out_buffer = out_buffer;
        this.in_f_buffer = in_f_buffer;
        this.out_f_buffer = out_f_buffer;
        this.status = status;
        this.size = size;
    }


    @Override
    public void run() {
        final Guard[] guards;
        switch (status) {
            case FIRST:
                guards = new Guard[in.length];
                for (int i = 0; i < in.length; i++) {
                    guards[i] = in[i].in();
                }
                break;
            case MIDDLE:
                guards = new Guard[]{in_buffer.in()};
                break;
            case LAST:
                guards = new Guard[req.length + 1];
                for (int i = 0; i < req.length; i++) {
                    guards[i] = req[i].in();
                }
                guards[req.length] = in_buffer.in();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
        final Alternative alt = new Alternative(guards);

        while (running) {
            int index = alt.select();
            switch (status) {
                case FIRST:
//                    System.out.println("first: " + buffer_data.size());
                    int item = in[index].in().read();
                    if (item + buffer_data.size() <= size) {
                        out[index].out().write(1);
                        for (int i = 0; i < item; i++) {
                            buffer_data.add(item);
                        }
                    } else {
                        out[index].out().write(0);
                        out_buffer.out().write(-1);
                        int c = out_f_buffer.in().read();
                        if (c != 0) {
                            c = Math.min(c, buffer_data.size());
                            out_buffer.out().write(c);
                            for (int i = 0; i < c; i++) {
                                buffer_data.removeFirst();
                            }
                        }
                    }
                    break;
                case MIDDLE:
//                    System.out.println("mid: " + buffer_data.size());
                    int s = in_buffer.in().read();
                    if (s == -1)
                        in_f_buffer.out().write(size - buffer_data.size());
                    else {
                        for (int i = 0; i < s; i++) {
                            buffer_data.add(s);
                        }
                    }
                    if (!buffer_data.isEmpty()) {
                        out_buffer.out().write(-1);
                        int c = out_f_buffer.in().read();
                        if (c != 0) {
                            c = Math.min(c, buffer_data.size());
                            out_buffer.out().write(c);
                            for (int i = 0; i < c; i++) {
                                buffer_data.removeFirst();
                            }
                        }
                    }
                    break;
                case LAST:
//                    System.out.println("last: " + buffer_data.size());
                    if (index == req.length) {
                        int c = in_buffer.in().read();
                        if (c == -1) {
                            in_f_buffer.out().write(size - buffer_data.size());
                        } else {
                            for (int i = 0; i < c; i++) {
                                buffer_data.add(c);
                            }
                        }
                    } else {
                        int c = req[index].in().read();
                        if (c > buffer_data.size()) {
                            out[index].out().write(-1);
                        } else {
                            out[index].out().write(c);
                            for (int i = 0; i < c; i++) {
                                buffer_data.removeFirst();
                            }
                        }
                    }

                    break;
            }
        }
    }
}
