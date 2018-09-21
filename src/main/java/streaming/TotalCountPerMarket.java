package streaming;

import com.google.gson.Gson;
import model.Bet;
import model.Market;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.util.ArrayList;
import java.util.Properties;

public class TotalCountPerMarket {

    final StreamsBuilder builder;

    public TotalCountPerMarket(){


        builder = new StreamsBuilder();

        KStream<String, String> betStream = builder.stream("Bets");
        KTable<String, String> marketTable = builder.table("Markets");

        son g = new Gson();

        KStream<String, String> marketStream = betStream.flatMap((KeyValueMapper<String, String, Iterable<KeyValue<String, String>>>) (key, value) -> {
            ArrayList<KeyValue<String, String>> marketList = new ArrayList<>();
            try {
                ArrayList<String> markets = g.fromJson(value, Bet.class).getMarkets();
                for (int i = 0; i < markets.size(); i++) marketList.add(new KeyValue<>(markets.get(i), markets.get(i)));
                return marketList;
            } catch (Exception e) {
                // In case we have bad data
                marketList.add(new KeyValue<>("-1", "null"));
                return marketList;
            }
        });

        marketStream
                /** group by key, count and create a new stream **/
                .groupByKey()
                .count()
                /** join the market ids to the markets table and publish to new topic **/
                .join(marketTable, (value1, value2) -> {
                    Market market = g.fromJson(value2, Market.class);
                    market.setTotalCount(StrictMath.toIntExact(value1));
                    return g.toJson(market);
                })
                .toStream()
                .through("Market-Count")
                .peek((key, value) -> System.out.println(value));

        final Topology topology = builder.build();
        System.out.println(topology.describe());


    }

    public static void main(String[] args) {
        new TotalCountPerMarket().start();
    }

    public Properties getStreamsConfig() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "market-summary");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.PRODUCER_PREFIX + ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
                "io.confluent.monitoring.clients.interceptor.MonitoringProducerInterceptor");
        props.put(StreamsConfig.CONSUMER_PREFIX + ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,
                "io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor");
        return props;
    }

    private void start() {
        // start the stream
        new KafkaStreams(builder.build(), this.getStreamsConfig()).start();
    }

}
