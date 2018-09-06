package dao;

import data.model.Market;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

public class MarketDao {

    private static final String MARKET_CACHE = "MarketCache";
    private final IgniteCache<Integer, Market> marketCache;

    public MarketDao(Ignite ignite){
        this.marketCache = ignite.cache(MARKET_CACHE);
    }

    public Market getMarketById(int marketId) {
        return marketCache.get(marketId);
    }
}
