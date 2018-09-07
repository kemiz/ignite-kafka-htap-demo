package streaming;

import model.Bet;
import kafka.consumer.ConsumerConfig;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.stream.kafka.KafkaStreamer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import static utils.IgniteConfigHelper.BET_CACHE;
import static utils.IgniteConfigHelper.getIgniteClientConfig;

public class BetDataIgniteStreamer {

    private final KafkaStreamer<String, Bet> kafkaStreamer;

    public BetDataIgniteStreamer() throws UnknownHostException {

        /** Start Ignite **/
        Ignite ignite = Ignition.start(getIgniteClientConfig());

        /** Setup Kafka consumer **/
        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", "localhost:9092");
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        config.put("value.serializer", "serialization.BetSerializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        config.put("value.deserializer", "serialization.BetDeserializer");
        config.put("zookeeper.connect", "127.0.0.1");
        config.put("group.id", "demo-group");
        config.put("enable.auto.commit", "true");
        config.put("auto.commit.interval.ms", "1000");
        config.put("session.timeout.ms", "30000");
        config.put("rebalance.max.retries","6");
        config.put("rebalance.backoff.ms","3000");
        ConsumerConfig consumerConfig = new ConsumerConfig(config);

        /** Setup Kafka-Ignite Streamer **/
        kafkaStreamer = new KafkaStreamer<>();
        IgniteDataStreamer<String, Bet> stmr = ignite.dataStreamer(BET_CACHE);
        stmr.allowOverwrite(true);
        // Set the Ignite instance that we weill be using
        kafkaStreamer.setIgnite(ignite);
        kafkaStreamer.setStreamer(stmr);
        kafkaStreamer.setTopic("BetTopic");
        kafkaStreamer.setThreads(4);
        kafkaStreamer.setConsumerConfig(consumerConfig);
        kafkaStreamer.setMultipleTupleExtractor(new BetMultipleTupleExtractor());
    }

    public void start() {
        kafkaStreamer.start();
    }
}
