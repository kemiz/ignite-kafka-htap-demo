package query;

import model.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import utils.IgniteConfigHelper;

import java.util.List;

public class Top3Markets {
    public static void main(String[] args) {
        IgniteConfiguration cfg = IgniteConfigHelper.getIgniteClientConfig();
        Ignite ignite = Ignition.start(cfg);

        IgniteCache<Long, User> userCache = ignite.cache(IgniteConfigHelper.MARKET_CACHE);

        SqlFieldsQuery sql = new SqlFieldsQuery(
                "select top 3 market, totalcount from Market order by totalcount desc");

        while (true) {
            try {
                // Iterate over the result set.
                try (QueryCursor<List<?>> cursor = userCache.query(sql)) {
                    for (List<?> row : cursor)
                        System.out.println("Market: " + row.get(0) + ", Total Count: " + row.get(1));
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
