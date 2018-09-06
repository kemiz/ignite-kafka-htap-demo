package data.model;

import java.util.ArrayList;


public class Bet {

    private String id;
    private ArrayList<Integer> marketSelectionsIds;
    private double stake;
    private int userId;

    public Bet(String id, double stake, int userId, ArrayList<Integer> marketSelectionsIds) {
        this.id = id;
        this.marketSelectionsIds = marketSelectionsIds;
        this.stake = stake;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "userId='" + userId + '\'' +
                ", id='" + id + '\'' +
                ", marketSelectionsIds=" + marketSelectionsIds +
                ", stake=" + stake +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Integer> getMarkets() {
        return marketSelectionsIds;
    }

    public double getStake() {
        return stake;
    }

}
