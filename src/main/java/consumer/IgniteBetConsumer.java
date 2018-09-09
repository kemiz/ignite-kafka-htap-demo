package consumer;

import com.google.gson.Gson;
import dao.BetDao;
import dao.UserDao;
import model.Bet;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import utils.IgniteConfigHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Simple hipster consumer that starts a transaction in ignite to commit the
 * incoming bet and increment the total user stake.
 * This is a cross cache/ partition transaction.
 */
public class IgniteBetConsumer {

    private final IgniteTransactions transactions;
    private final UserDao userDao;
    private final BetDao betDao;
    private boolean on = false;
    private KafkaConsumer<String, String> consumer;

    public IgniteBetConsumer() throws UnknownHostException {

        IgniteConfiguration cfg = IgniteConfigHelper.getIgniteClientConfig();
        Ignite ignite = Ignition.start(cfg);
        betDao = new BetDao(ignite);
        userDao = new UserDao(ignite);
        transactions = ignite.transactions();

        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", "localhost:9092");
        config.put("group.id", "demo-group");
        config.put("zookeeper.connect", "127.0.0.1");
        config.put("acks", "all");
        config.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("enable.auto.commit", "true");
        config.put("auto.commit.interval.ms", "1000");
        config.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,
                "io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor");

        consumer = new KafkaConsumer<>(config);
        consumer.subscribe(Arrays.asList("BetTopic"));
        return;
    }

    public void start() {
        Gson g = new Gson();
        this.on = true;
        while (on) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                // Start an ignite transaction to update the bet cache
                // and increment the total stake to date of the user
                try (Transaction tx = transactions.txStart()) {
                    String jsonBet = record.value();
                    Bet bet = g.fromJson(jsonBet, Bet.class);
                    // add the new bet to the bet cache
                    betDao.addBet(bet);
                    User user = userDao.getUserById(bet.getUserId());
                    user.incrementTotalStake(bet.getStake());
                    // increment the total stake of the user
                    userDao.addUser(user);
                    tx.commit();
                }
            }
        }
    }

    public void stop(){
        this.on = false;
    }
}
