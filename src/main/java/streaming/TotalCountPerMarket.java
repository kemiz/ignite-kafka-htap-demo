package streaming;

import com.google.gson.Gson;
import dao.MarketDao;
import model.Bet;
import model.Market;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import utils.IgniteConfigHelper;

import java.util.ArrayList;
import java.util.Properties;

public class TotalCountPerMarket {

    private MarketDao marketDao;

    public TotalCountPerMarket(){

//        marketDao = new MarketDao(Ignition.start(IgniteConfigHelper.getIgniteClientConfig()));

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "market-summary");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.PRODUCER_PREFIX + ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
                "io.confluent.monitoring.clients.interceptor.MonitoringProducerInterceptor");
        props.put(StreamsConfig.CONSUMER_PREFIX + ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,
                "io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor");

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> betStream = builder.stream("Bets");
        KTable<String, String> marketTable = builder.table("Markets");

        Gson g = new Gson();

        KStream<String, String> marketStream = betStream.flatMap((KeyValueMapper<String, String, Iterable<KeyValue<String, String>>>) (key, value) -> {
            ArrayList<KeyValue<String, String>> marketList = new ArrayList<>();
            try {
                ArrayList<String> markets = g.fromJson(value, Bet.class).getMarkets();
                for (int i = 0; i < markets.size(); i++) marketList.add(new KeyValue<>(markets.get(i), markets.get(i)));
                return marketList;
            } catch (Exception e) {
                marketList.add(new KeyValue<>("0", "null"));
                return marketList;
            }
        });

        marketStream
                /** group by key, count and create a new stream **/
                .groupByKey()
                .count()
                .toStream()
                /** join the market ids to the markets table and publish to new topic **/
                .join(marketTable, (value1, value2) -> "Market: " + g.fromJson(value2, Market.class).getMarket() + ", Count: " + value1)
                .through("Bet-Market-Selection-Feed")
                .peek((key, value) -> System.out.println(value));

        final Topology topology = builder.build();
        System.out.println(topology.describe());

        // start the stream
        new KafkaStreams(builder.build(), props).start();
    }

    public static void main(String[] args) {
        TotalCountPerMarket totalCountPerMarket = new TotalCountPerMarket();
    }

}
