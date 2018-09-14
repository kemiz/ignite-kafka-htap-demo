package query;

import model.Market;
import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import utils.IgniteConfigHelper;

import java.util.List;

public class AdjustOddsOfTop3Markets {
    public static void main(String[] args) {
        IgniteConfiguration cfg = IgniteConfigHelper.getIgniteClientConfig();
        Ignite ignite = Ignition.start(cfg);

        IgniteCache<String, Market> marketCache = ignite.cache(IgniteConfigHelper.MARKET_CACHE);

        // Execute query to get names of all employees.
        SqlFieldsQuery sql = new SqlFieldsQuery(
                "select top 3 id, market, odds, totalcount from Market order by totalcount desc");

        while (true) {
            try {
                // Iterate over the result set.
                try (QueryCursor<List<?>> cursor = marketCache.query(sql)) {
                    for (List<?> row : cursor) {
                        Market m = marketCache.get((String) row.get(0));
                        m.setOdds(m.getOdds() * 0.8);
                        marketCache.put(m.getId(), m);
                        System.out.println("Market: " + row.get(1) + ", New Odds: " + row.get(2));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("\n\n\n\n\n");
                Thread.sleep(5000);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
