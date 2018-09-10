package load;

import com.google.gson.Gson;
import dao.MarketDao;
import dao.UserDao;
import model.Market;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import scala.Int;
import utils.IgniteConfigHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import static utils.IgniteConfigHelper.getIgniteClientConfig;

public class KafkaInitialDataLoader {

    private static final String USER_TOPIC = "Users";
    private static final String MARKET_TOPIC = "Markets";
    private final MarketDao marketDao;
    private final UserDao userDao;
    private final KafkaProducer<Long, String> userProducer;
    private final KafkaProducer<Long, String> marketProducer;
    private final Ignite ignite;

    public KafkaInitialDataLoader() throws UnknownHostException {
        /** Start Ignite **/
        ignite = Ignition.start(getIgniteClientConfig());

        /** Set DAOs **/
        this.marketDao = new MarketDao(ignite);
        this.userDao = new UserDao(ignite);
        userProducer = new KafkaProducer<>(getProperties());
        marketProducer = new KafkaProducer<>(getProperties());
    }

    @NotNull
    public Properties getProperties() throws UnknownHostException {
        Properties config = new Properties();
        config.put(ProducerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put("group.id", "demo-group");
        config.put("zookeeper.connect", "127.0.0.1");
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return config;
    }

    public void load() {
        Gson g = new Gson();
        for (int i = 0; i < userDao.getUserCount(); i++) {
            User user = userDao.getUserById(i);
            userProducer.send(new ProducerRecord<>(USER_TOPIC, user.getId(), g.toJson(user)));
        }
        for (int i = 0; i < marketDao.getMarketCount(); i++) {
            Market market = marketDao.getMarketById(i);
            marketProducer.send(new ProducerRecord<>(MARKET_TOPIC, market.getId(), g.toJson(market)));
        }
        userProducer.flush();
        marketProducer.flush();
        this.ignite.close();
    }
}
