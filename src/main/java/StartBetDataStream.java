import producer.BetDataStreamGenerator;

import java.net.UnknownHostException;

/**
 * Starts generating a stream of bet data that is published to a kafka topic.
 */
public class StartBetDataStream {

    public static void main (String... args){
        try {
            BetDataStreamGenerator streamGenerator = new BetDataStreamGenerator();
            streamGenerator.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
