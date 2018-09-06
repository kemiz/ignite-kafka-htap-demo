package tool;

import data.model.Bet;
import data.model.User;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import java.util.Arrays;

public class IgniteConfigHelper {

    public static String BET_CACHE = "BetCache";
    public static String MARKET_CACHE = "MarketCache";
    public static String USER_CACHE = "UserCache";

    public static IgniteConfiguration getIgniteClientConfig() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);

        /** Ignite discovery config**/
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
        spi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(spi);
        return cfg;
    }

    public static IgniteConfiguration getIgniteServerConfig() {
        IgniteConfiguration cfg = new IgniteConfiguration();

        /** Ignite discovery config**/
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
        spi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(spi);

        /** Ignite cache config**/
        CacheConfiguration<String, Bet> marketCacheCfg = new CacheConfiguration<>();
        marketCacheCfg.setName(MARKET_CACHE);
        marketCacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        marketCacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        marketCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        cfg.setCacheConfiguration(marketCacheCfg);

        CacheConfiguration<Integer, User> userCacheCfg = new CacheConfiguration<>();
        userCacheCfg.setName(USER_CACHE);
        userCacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        userCacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        userCacheCfg.setCacheMode(CacheMode.PARTITIONED);

        CacheConfiguration<Integer, Bet> betCacheCfg = new CacheConfiguration<>();
        betCacheCfg.setName(BET_CACHE);
        betCacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        betCacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        betCacheCfg.setCacheMode(CacheMode.PARTITIONED);

        cfg.setCacheConfiguration(new CacheConfiguration[]{marketCacheCfg, userCacheCfg, betCacheCfg});
        return cfg;
    }
}
