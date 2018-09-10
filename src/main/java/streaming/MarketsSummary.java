package streaming;

import com.google.gson.Gson;
import dao.MarketDao;
import model.Bet;
import org.apache.ignite.Ignition;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import utils.IgniteConfigHelper;

import java.util.ArrayList;
import java.util.Properties;

public class MarketsSummary {

    private MarketDao marketDao;

    public MarketsSummary(){

        marketDao = new MarketDao(Ignition.start(IgniteConfigHelper.getIgniteClientConfig()));
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

        KStream<String, String> marketStream = betStream.flatMap((KeyValueMapper<String, String, Iterable<KeyValue<String, String>>>) (key, value) -> {
            ArrayList<KeyValue<String, String>> marketList = new ArrayList<>();
            try {
                Bet bet = new Gson().fromJson(value, Bet.class);
                System.out.println("received: " + bet);
                ArrayList<Long> markets = bet.getMarkets();
                System.out.println("  -- extracting market selection data");
                for (int i = 0; i < markets.size(); i++) {
                    marketList.add(new KeyValue<>(markets.get(i).toString(), marketDao.getMarketById(i).getMarket()));
                }
                return marketList;
            } catch (Exception e) {
                marketList.add(new KeyValue<>("0", "0"));
                return marketList;
            }
        });

        marketStream.to("MarketsSummary", Produced.with(Serdes.String(), Serdes.String()));

        KStream<String, Long> marketCountStream = marketStream.groupByKey().count().toStream();
        marketCountStream.to("MarketsCount", Produced.with(Serdes.String(), Serdes.Long()));

        final Topology topology = builder.build();
        System.out.println(topology.describe());

        // start the stream
        new KafkaStreams(builder.build(), props).start();
    }

    public static void main(String[] args) {
        MarketsSummary marketsSummary = new MarketsSummary();

    }

}
