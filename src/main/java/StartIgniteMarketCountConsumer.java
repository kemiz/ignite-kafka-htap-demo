import consumer.IgniteMarketCountConsumer;

public class StartIgniteMarketCountConsumer {

    public static void main (String... args){
        try {
            new IgniteMarketCountConsumer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
