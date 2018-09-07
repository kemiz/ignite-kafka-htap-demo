import streaming.BetDataIgniteStreamer;

import java.net.UnknownHostException;

/**
 * Starts the Ignite-Kafka streamer that subscribes to the "bet" topic in kafka.
 * The data received is received as a string and is converted to an "Bet" object
 * and inserted into Ignite.
 */
public class StartIgniteBetDataStreamer {

    public static void main (String... args){
        try {
            BetDataIgniteStreamer igniteStreamer = new BetDataIgniteStreamer();
            igniteStreamer.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
