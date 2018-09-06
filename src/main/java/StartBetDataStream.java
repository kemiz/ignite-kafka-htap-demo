import data.streaming.BetDataStreamGenerator;

import java.net.UnknownHostException;

public class StartBetDataStream {

    public static void main (String... args){
        try {
            BetDataStreamGenerator streamGenerator = new BetDataStreamGenerator();
            streamGenerator.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
