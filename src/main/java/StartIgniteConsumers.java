import consumer.IgniteBetConsumer;

import java.net.UnknownHostException;

public class StartIgniteConsumers {

    public static void main (String... args){
        try {
            IgniteBetConsumer igniteBetConsumer = new IgniteBetConsumer();
            igniteBetConsumer.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
