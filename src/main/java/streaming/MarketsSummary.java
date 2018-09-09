package streaming;

import com.google.gson.Gson;
import model.Bet;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import scala.Int;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MarketsSummary {

    public MarketsSummary(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> betStream = builder.stream("BetTopic");

        betStream.flatMap((KeyValueMapper<String, String, Iterable<KeyValue<String, String>>>) (key, value) -> {
            // un-marshall json string to bet object
            Bet bet = new Gson().fromJson("{\"name\": \"John\"}", Bet.class);
            System.out.println(bet);
            ArrayList<Integer> markets = bet.getMarkets();
            // extract market data
            // convert to json string
            ArrayList<KeyValue<String, String>> marketStream = new ArrayList<>();

            for (int i = 0; i < markets.size(); i++) {
                marketStream.add(new KeyValue<>(markets.get(i).toString(), markets.get(i).toString()));
            }
            return marketStream;
        })
                .groupByKey()
                .windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(5)).advanceBy(TimeUnit.MINUTES.toMillis(1)))
                .count();
        betStream.to("");





        final Topology topology = builder.build();
        System.out.println(topology.describe());
    }

}
