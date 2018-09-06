import data.streaming.BetDataIgniteStreamer;

import java.net.UnknownHostException;

public class StartIgniteBetDataStreamer {

    public static void main (String... args){
        try {
            BetDataIgniteStreamer igniteStreamer = new BetDataIgniteStreamer();
            igniteStreamer.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
