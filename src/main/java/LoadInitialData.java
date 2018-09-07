import load.IgniteInitialDataLoader;
import load.KafkaInitialDataLoader;

import java.net.UnknownHostException;

/**
 * Loads initial data into Ignite cache & Kafka topics
 * - User & Bet Market data
 */
public class LoadInitialData {

    public static void main (String... args) throws UnknownHostException {
        new IgniteInitialDataLoader().load();
        new KafkaInitialDataLoader().load();
    }
}
