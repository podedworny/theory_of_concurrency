package tw.lab8;

import org.jcsp.lang.*;
import java.util.LinkedList;


public class Buffer implements CSProcess{
    private  One2OneChannelInt req_consumer;
    private  One2OneChannelInt out_consumer;
    private  One2OneChannelInt in_producer;
    private  One2OneChannelInt in_buffer;
    private  One2OneChannelInt out_buffer;
    private One2OneChannelInt in_req_buffer;
    private One2OneChannelInt out_req_buffer;
    int size;
    boolean running = true;
    LinkedList<Integer> buffer_data = new LinkedList<Integer>();
    private static int idCounter = 1;
    int id = idCounter++;
    int action_count = 0;
    long start_time = 0;

    public Buffer(One2OneChannelInt req, One2OneChannelInt out, One2OneChannelInt in, One2OneChannelInt in_buffer,
                  One2OneChannelInt out_buffer, One2OneChannelInt in_req_buffer, One2OneChannelInt out_req_buffer, int size) {
        this.req_consumer = req;
        this.out_consumer = out;
        this.in_producer = in;
        this.in_buffer = in_buffer;
        this.out_buffer = out_buffer;
        this.size = size;
        this.in_req_buffer = in_req_buffer;
        this.out_req_buffer = out_req_buffer;
    }


    @Override
    public void run() {
        final Guard[] guards = {req_consumer.in(), in_producer.in(), in_buffer.in()};
        final Alternative alt = new Alternative(guards);

        while (running){
            int index = alt.fairSelect();
            if (index == 0) { // A Consumer is ready to read
                if (!buffer_data.isEmpty()) {
                    req_consumer.in().read();
                    int item = buffer_data.removeFirst();
                    out_consumer.out().write(item);
                }
            } else if (index == 1) { // A Producer is ready to send
                if (buffer_data.size() < size) {
                    int item = in_producer.in().read();
                    buffer_data.add(item);
                    action_count++;
                }
            } else if (index == 2) { // A buffer is ready to send
                int item = in_buffer.in().read();
                if (buffer_data.size() < size) {
                    buffer_data.add(item);
                    action_count++;
                    in_req_buffer.out().write(-1);
                }
                else {
                    in_req_buffer.out().write(0);
                }
            }
            if (buffer_data.size() >= (int) (size * 0.8)) {
                if (start_time == 0) {
                    start_time = System.currentTimeMillis();
                }
                else if (System.currentTimeMillis() - start_time >= 1000) {
                    out_buffer.out().write(buffer_data.peek());
                    int signal = out_req_buffer.in().read();
                    if (signal == -1) {
                        buffer_data.removeFirst();
                    }
                    start_time = 0;
                }

            }
            else{
                start_time = 0;
            }
        }
    }
}
