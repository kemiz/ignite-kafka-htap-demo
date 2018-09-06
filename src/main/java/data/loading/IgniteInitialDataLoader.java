package data.loading;

import data.model.Market;
import data.model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import tool.IgniteConfigHelper;

import java.util.ArrayList;
import java.util.Arrays;

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
        for (Market market :this.getBetMarketData()) {
            ignite.cache(MARKET_CACHE).put(market.getId(), market);
            System.out.println(ignite.cache(MARKET_CACHE).get(market.getId()));
        }
    }

    public void loadInitialUserData(){
        for (User user :this.getUserData()) {
            ignite.cache(USER_CACHE).put(user.getId(), user);
            System.out.println(ignite.cache(USER_CACHE).get(user.getId()));
        }
    }

    public static ArrayList<Market> getBetMarketData() {
        ArrayList<Market> marketData = new ArrayList<Market>();
        marketData.add(new Market(0, "West Ham to Draw", 4.3));
        marketData.add(new Market(1, "Liverpool to Win", 2.5));
        marketData.add(new Market(2, "Leicester to Win", 4.9));
        marketData.add(new Market(3, "Arsenal to Win", 3.6));
        marketData.add(new Market(4, "Manchester United to Win", 1.4));
        marketData.add(new Market(5, "Brighton to Win", 13));
        return marketData;
    }

    public static ArrayList<User> getUserData() {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(0, "Harry Jones"));
        users.add(new User(1, "George Sunday"));
        users.add(new User(2, "Anthony Johnson"));
        users.add(new User(3, "Peter Davis"));
        users.add(new User(4, "Mike Dully"));
        users.add(new User(5, "John Conway"));
        return users;
    }

    public void stopIgnite() {
        ignite.close();
    }
}
