package dao;

import model.Market;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import static utils.IgniteConfigHelper.MARKET_CACHE;

public class MarketDao {

    private final IgniteCache<Long, Market> marketCache;

    public MarketDao(Ignite ignite){
        this.marketCache = ignite.cache(MARKET_CACHE);
    }

    public Market getMarketById(long marketId) {
        return marketCache.get(marketId);
    }

    public void addMarket(Market market) {
        this.marketCache.put(market.getId(), market);
    }

    public int getMarketCount() {
        return this.marketCache.size();
    }
}
