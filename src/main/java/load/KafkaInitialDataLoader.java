package load;

import dao.MarketDao;
import dao.UserDao;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import scala.Int;
import utils.IgniteConfigHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import static utils.IgniteConfigHelper.getIgniteClientConfig;

public class KafkaInitialDataLoader {

    private static final String USER_TOPIC = "UserTopic";
    private final MarketDao marketDao;
    private final UserDao userDao;

    public KafkaInitialDataLoader() throws UnknownHostException {

        /** Start Ignite **/
        Ignite ignite = Ignition.start(getIgniteClientConfig());

        /** Set DAOs **/
        this.marketDao = new MarketDao(ignite);
        this.userDao = new UserDao(ignite);

        /** Setup Kafka Bet data producer **/
        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", "localhost:9092");
        config.put("group.id", "demo-group");
        config.put("zookeeper.connect", "127.0.0.1");
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        config.put("value.serializer", "serialization.UserSerializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        config.put("value.deserializer", "serialization.UserDeserializer");

        IgniteCache<Integer, User> userCache = ignite.cache(IgniteConfigHelper.USER_CACHE);
        KafkaProducer<Integer, User> userProducer = new KafkaProducer<>(config);

        for (int i = 0; i < userCache.size(); i++) {
            User user = userCache.get(i);
            userProducer.send(new ProducerRecord<>(USER_TOPIC, user.getId(), user));
        }
    }

    public static void main(String[] args) {
        try {
            KafkaInitialDataLoader initialDataLoader = new KafkaInitialDataLoader();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
