package dao;

import model.Bet;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import static utils.IgniteConfigHelper.BET_CACHE;
import static utils.IgniteConfigHelper.USER_CACHE;

public class BetDao {
    private IgniteCache<String, Bet> betCache;

    public BetDao(Ignite ignite){
        this.betCache = ignite.cache(BET_CACHE);
    }

    public Bet getBetById(String betId) {
        return betCache.get(betId);
    }

    public int getBetCount() {
        return betCache.size();
    }

    public void addBet(Bet bet) {
        betCache.put(bet.getId(), bet);
    }
}
