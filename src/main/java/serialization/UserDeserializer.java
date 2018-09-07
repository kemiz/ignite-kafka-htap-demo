package serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Bet;
import model.User;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class UserDeserializer implements Deserializer<User> {

    public void close() {
    }

    public void configure(Map<String, ?> arg0, boolean arg1) {
    }

    public User deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = mapper.readValue(arg1, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
