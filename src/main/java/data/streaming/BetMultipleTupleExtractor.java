package data.streaming;

import data.model.Bet;
import kafka.message.MessageAndMetadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ignite.stream.StreamMultipleTupleExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BetMultipleTupleExtractor implements StreamMultipleTupleExtractor {

    private final static Log log = LogFactory.getLog(BetMultipleTupleExtractor.class);

    public BetMultipleTupleExtractor(){
    }

    @Override
    public Map<String,Bet> extract(Object input) {
        Map<String,Bet> entries = new HashMap<>();
        try {

            MessageAndMetadata<byte[], byte[]> msg = (MessageAndMetadata<byte[], byte[]>) input;
            String val = new String(msg.message());
            Bet bet = extractBetTransaction(val);
            System.out.println(bet);
            entries.put(bet.getId(), bet);

        }catch (Exception ex) {
            log.error(ex);
        }
        return entries;
    }

    private Bet extractBetTransaction(String message) {
        Bet outcome = null;
        try {
            String[] res = message.split(",", -1);
            StringBuilder marketSelections = new StringBuilder();
            for (int i = 3; i < res.length; i++) {
                marketSelections.append(res[i]);
                if (i < res.length - 1) marketSelections.append(",");
            }
            String marketIdsString = marketSelections.toString().substring(11, marketSelections.toString().length() - 2);
            outcome = new Bet(
                    /** id **/ res[0].substring(7, res[0].length()-1),
                    /** stake **/ Double.parseDouble(res[1].substring(8)),
                    /** userId **/ Integer.parseInt(res[2].substring(9)),
                    /** markets **/ extractMarketIds(marketIdsString)
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return outcome;
    }

    private ArrayList<Integer> extractMarketIds(String marketIds){
        ArrayList<Integer> marketSelections = new ArrayList<>();
        String[] res = marketIds.split(",", -1);
        for (String string : res){
            marketSelections.add(Integer.parseInt(string));
        }
        return marketSelections;
    }
}