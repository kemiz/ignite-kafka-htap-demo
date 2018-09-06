import data.model.Bet;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import javax.cache.Cache;
import java.util.Arrays;

public class QueryIgniteCache {

    private static final String BET_CACHE = "BetCache";

    public static void main (String... args){
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);

        /** Ignite discovery config**/
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
        spi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(spi);

        /** Start Ignite **/
        Ignite ignite = Ignition.start(cfg);

        QueryCursor<Cache.Entry<String, Bet>> res = ignite.cache(BET_CACHE).query(new SqlQuery<String, Bet>(Bet.class, "select *"));

        for (Cache.Entry<String, Bet> cacheEntry: res) {
            System.out.println(cacheEntry.getValue());
        }
    }
}
