package load;

import dao.MarketDao;
import dao.UserDao;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
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
    private final KafkaProducer<Integer, User> userProducer;
    private final Ignite ignite;

    public KafkaInitialDataLoader() throws UnknownHostException {
        /** Start Ignite **/
        ignite = Ignition.start(getIgniteClientConfig());

        /** Set DAOs **/
        this.marketDao = new MarketDao(ignite);
        this.userDao = new UserDao(ignite);

        /** Setup Kafka Bet data producer **/
        Properties config = new Properties();
        config.put(ProducerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put("group.id", "demo-group");
        config.put("zookeeper.connect", "127.0.0.1");
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "serialization.UserSerializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        config.put("value.deserializer", "serialization.UserDeserializer");

        userProducer = new KafkaProducer<>(config);
    }

    public void load() {
        for (int i = 0; i < userDao.getUserCount(); i++) {
            User user = userDao.getUserById(i);
            userProducer.send(new ProducerRecord<>(USER_TOPIC, user.getId(), user));
        }
        this.ignite.close();
    }
}
