import load.IgniteInitialDataLoader;

/**
 * Loads initial data into Ignite cache
 * - Users
 * - Bet Markets
 * -
 */
public class LoadInitialData {

    public static void main (String... args){
        IgniteInitialDataLoader igniteInitialDataLoader = new IgniteInitialDataLoader();
        igniteInitialDataLoader.startIgnite();
        igniteInitialDataLoader.loadInitialMarketData();
        igniteInitialDataLoader.loadInitialUserData();
        igniteInitialDataLoader.stopIgnite();
    }
}
