import dao.MarketDao;
import dao.UserDao;
import data.loading.IgniteInitialDataLoader;
import data.streaming.BetDataStreamGenerator;

import java.net.UnknownHostException;

public class LoadInitialData {

    public static void main (String... args){
        IgniteInitialDataLoader igniteInitialDataLoader = new IgniteInitialDataLoader();
        igniteInitialDataLoader.startIgnite();
        igniteInitialDataLoader.loadInitialMarketData();
        igniteInitialDataLoader.loadInitialUserData();
        igniteInitialDataLoader.stopIgnite();
    }
}
