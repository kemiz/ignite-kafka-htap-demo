package load;

import dao.MarketDao;
import dao.UserDao;
import model.Market;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import utils.MockData;
import utils.IgniteConfigHelper;

public class IgniteInitialDataLoader {

    private Ignite ignite;
    private final MarketDao marketDao;
    private final UserDao userDao;

    public IgniteInitialDataLoader(){
        /** Start Ignite **/
        IgniteConfiguration cfg = IgniteConfigHelper.getIgniteClientConfig();
        ignite = Ignition.start(cfg);
        this.marketDao = new MarketDao(ignite);
        this.userDao = new UserDao(ignite);
    }

    public void load() {
        this.loadInitialMarketData();
        this.loadInitialUserData();
        ignite.close();
    }

    private void loadInitialMarketData(){
        for (Market market : MockData.getBetMarketData()) {
            marketDao.addMarket(market);
            System.out.println(marketDao.getMarketById(market.getId()));
        }
    }

    private void loadInitialUserData(){
        for (User user : MockData.getUserData()) {
            userDao.addUser(user);
            System.out.println(userDao.getUserById(user.getId()));
        }
    }

}
