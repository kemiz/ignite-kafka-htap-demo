package query;

import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import utils.IgniteConfigHelper;

public class QueryIgnite {
    public static void main(String[] args) throws InterruptedException {
        IgniteConfiguration cfg = IgniteConfigHelper.getIgniteClientConfig();
        Ignite ignite = Ignition.start(cfg);
        IgniteCache<Integer, User> userCache = ignite.cache(IgniteConfigHelper.USER_CACHE);

        while (true) {
            for (int i = 0; i < userCache.size(); i++) {
                User user = userCache.get(i);
                System.out.println("name: " + user.getName() + ", total stake: " + user.getStake());
            }
            System.out.println("\n\n\n\n\n");
            Thread.sleep(1000);
        }

    }
}
