package data.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.model.Bet;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class BetDeserializer implements Deserializer<Bet> {

    public void close() {
    }

    public void configure(Map<String, ?> arg0, boolean arg1) {
    }

    public Bet deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        Bet bet = null;
        try {
            bet = mapper.readValue(arg1, Bet.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bet;
    }
}
