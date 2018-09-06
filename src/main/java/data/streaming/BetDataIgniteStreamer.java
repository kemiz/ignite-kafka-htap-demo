package data.streaming;

import data.model.Bet;
import kafka.consumer.ConsumerConfig;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.stream.kafka.KafkaStreamer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

public class BetDataIgniteStreamer {

    private final KafkaStreamer<String, Bet> kafkaStreamer;

    public BetDataIgniteStreamer() throws UnknownHostException {

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);

        /** Ignite discovery config**/
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
        spi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(spi);

        /** Start Ignite **/
        Ignite ignite = Ignition.start(cfg);

        /** Setup Kafka consumer **/
        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", "localhost:9092");
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        config.put("value.serializer", "data.serialization.BetSerializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        config.put("value.deserializer", "data.serialization.BetDeserializer");
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
        IgniteDataStreamer<String, Bet> stmr = ignite.dataStreamer("BetCache");
        stmr.allowOverwrite(true);
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
