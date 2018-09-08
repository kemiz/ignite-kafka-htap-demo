package streaming.kafka;

import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public class UserStakeAggregator {

    public UserStakeAggregator(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());

//        final StreamsBuilder builder = new StreamsBuilder();
//        KStream<String, Bet> betStream = builder.stream("BetTopic");

//        betStream.mapValues(bet -> bet.getUserId()).





//        final Topology topology = builder.build();
//        System.out.println(topology.describe());
    }

}
