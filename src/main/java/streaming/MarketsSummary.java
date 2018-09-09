package streaming;

import com.google.gson.Gson;
import model.Bet;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import java.util.ArrayList;
import java.util.Properties;

public class MarketsSummary {

    public MarketsSummary(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "market-summary");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.PRODUCER_PREFIX + ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
                "io.confluent.monitoring.clients.interceptor.MonitoringProducerInterceptor");

        props.put(StreamsConfig.CONSUMER_PREFIX + ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,
                "io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor");


        final StreamsBuilder builder = new StreamsBuilder();
        KStream<Long, String> betStream = builder.stream("BetTopic");
        Produced<Long, String> produced = Produced.with(Serdes.Long(), Serdes.String());

        KStream<Long, String> marketStream = betStream.flatMap((KeyValueMapper<Long, String, Iterable<KeyValue<Long, String>>>) (key, value) -> {
            ArrayList<KeyValue<Long, String>> marketList = new ArrayList<>();
            try {
                Bet bet = new Gson().fromJson(value, Bet.class);
                System.out.println(bet);
                ArrayList<Integer> markets = bet.getMarkets();
                for (int i = 0; i < markets.size(); i++) {
                    marketList.add(new KeyValue<>(new Long(markets.get(i)), new Long(markets.get(i)).toString()));
                }
                return marketList;
            } catch (Exception e) {
                marketList.add(new KeyValue<>(0l, "0"));
                return marketList;
            }
        });

        marketStream.to("MarketsSummary", produced);

        KStream<Long, Long> marketCountStream = marketStream.groupByKey().count().toStream();
        marketCountStream.to("MarketsCount", Produced.with(Serdes.Long(), Serdes.Long()));

        final Topology topology = builder.build();
        System.out.println(topology.describe());

        // start the stream
        new KafkaStreams(builder.build(), props).start();
    }

    public static void main(String[] args) {
        MarketsSummary marketsSummary = new MarketsSummary();

    }

}
