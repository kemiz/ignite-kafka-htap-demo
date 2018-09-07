package load;

import model.Market;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import utils.DataGenerator;
import utils.IgniteConfigHelper;

import java.util.ArrayList;

public class IgniteInitialDataLoader {

    private Ignite ignite;
    private String MARKET_CACHE = "MarketCache";
    private String USER_CACHE = "UserCache";

    public IgniteInitialDataLoader(){

    }

    public void startIgnite() {
        IgniteConfiguration cfg = IgniteConfigHelper.getIgniteClientConfig();

        /** Start Ignite **/
        ignite = Ignition.start(cfg);
    }

    public void loadInitialMarketData(){
        for (Market market : DataGenerator.getBetMarketData()) {
            ignite.cache(MARKET_CACHE).put(market.getId(), market);
            System.out.println(ignite.cache(MARKET_CACHE).get(market.getId()));
        }
    }

    public void loadInitialUserData(){
        for (User user : DataGenerator.getUserData()) {
            ignite.cache(USER_CACHE).put(user.getId(), user);
            System.out.println(ignite.cache(USER_CACHE).get(user.getId()));
        }
    }

    public void stopIgnite() {
        ignite.close();
    }
}
