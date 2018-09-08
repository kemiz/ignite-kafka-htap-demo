package producer;

import dao.MarketDao;
import dao.UserDao;
import model.Bet;
import model.Market;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static utils.IgniteConfigHelper.getIgniteClientConfig;

/**
 * Generates stream of bet placement data and publishes to kafka topic
 */
public class BetDataStreamGenerator {

    private static final String TOPIC = "BetTopic";
    private KafkaProducer<String, Bet> betProducer;
    private boolean done = false;
    private long intervalBetweenBetPlacements = 1000;
    private Random random = new Random(12345);
    private MarketDao marketDao;
    private UserDao userDao;

    public BetDataStreamGenerator() throws UnknownHostException {

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
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "serialization.BetSerializer");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "serialization.BetDeserializer");
        this.betProducer = new KafkaProducer<>(config);
    }

    /**
     * Start generating random bets and publish them to kafka topic
     */
    public void start(){
        while (!done) {
            // Generate a random bet and publish it the kafka topic
            Bet bet = generateRandomBet();
            betProducer.send(new ProducerRecord<>(TOPIC, bet.getId(), bet));
            try {
                Thread.sleep(intervalBetweenBetPlacements);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates a arbitrary bet object
     * @return the bet object
     */
    private Bet generateRandomBet() {
        int number_of_lines = random.nextInt(5) + 1;
        ArrayList<Integer> marketSelections = new ArrayList<Integer>();
        // Generate some random market selections for the best slip
        for (int i = 0; i <= number_of_lines; i++){
            Market currentSelection = marketDao.getMarketById(random.nextInt(5));
            // Add the selection to the bet slip
            if (!marketSelectionExists(currentSelection, marketSelections)) marketSelections.add(currentSelection.getId());
        }
        // Create the bet, add the selections and set a random stake
        User user = userDao.getUserById(random.nextInt(5));
        Bet bet = new Bet(
                UUID.randomUUID().toString(),
                random.nextInt(20) + 1,
                user.getId(),
                marketSelections);
        System.out.println(bet);
        return bet;
    }

    private static boolean marketSelectionExists(Market currentSelection, ArrayList<Integer> marketSelections) {
        for (Integer marketId : marketSelections) {
            if (marketId == currentSelection.getId()) {
                return true;
            }
        }
        return false;
    }

}
