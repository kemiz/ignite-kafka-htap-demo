import org.apache.ignite.Ignition;

import static utils.IgniteConfigHelper.getIgniteServerConfig;

public class StartIgniteServerNode {

    public static void main (String... args){
        /** Start Ignite **/
        Ignition.start(getIgniteServerConfig());
    }


}
