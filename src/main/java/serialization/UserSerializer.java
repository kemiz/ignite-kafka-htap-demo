package serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Bet;
import model.User;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserSerializer implements Serializer<User> {
    public void configure(Map<String, ?> map, boolean b) {
    }

    public byte[] serialize(String arg0, User arg1) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    public void close() {
    }
}
