package utils;

import model.Market;
import model.User;

import java.util.ArrayList;
import java.util.Random;

public class MockData {

    private static final Random random = new Random();

    public static ArrayList<Market> getBetMarketData() {
        ArrayList<Market> marketData = new ArrayList<Market>();
        marketData.add(new Market(0, "West Ham to Draw", 4.3));
        marketData.add(new Market(1, "Liverpool to Win", 2.5));
        marketData.add(new Market(2, "Leicester to Win", 4.9));
        marketData.add(new Market(3, "Arsenal to Win", 3.6));
        marketData.add(new Market(4, "Manchester United to Win", 1.4));
        marketData.add(new Market(5, "Brighton to Win", 13));
        return marketData;
    }

    public static ArrayList<User> getUserData() {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(0, "Harry Jones"));
        users.add(new User(1, "George Sunday"));
        users.add(new User(2, "Anthony Johnson"));
        users.add(new User(3, "Peter Davis"));
        users.add(new User(4, "Mike Dully"));
        users.add(new User(5, "John Conway"));
        return users;
    }

}
