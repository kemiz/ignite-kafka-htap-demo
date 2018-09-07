package streaming;

import model.Market;
import utils.DataGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MarketOddsStreamer {

    ArrayList<Market> markets = DataGenerator.getBetMarketData();

    private ArrayList<Market> getTop5Markets(){
        Collections.shuffle(markets, new Random(System.nanoTime()));
        ArrayList<Market> top5 = new ArrayList<>(markets.subList(0, 2));
        return top5;
    }

    public void adjustMarketOdds(){
        for (Market market : markets) {
            System.out.println(market);
        }
        int i = 0;
        for (Market market: getTop5Markets()) {
            // lower the odds if applicable
            double odds = market.getOdds();
            if (odds > 1.1) {
                odds = odds - 0.5;
            }
            market.setOdds(odds);
            markets.set(i, market);
            i++;
        }
    }

    public static void main(String[] args) {
        MarketOddsStreamer marketOddsStreamer = new MarketOddsStreamer();
        for (int i = 0; i < 5; i++) {
            marketOddsStreamer.adjustMarketOdds();
            System.out.println("------------------");
        }

    }
}
