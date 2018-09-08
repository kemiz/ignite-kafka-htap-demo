package model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.util.ArrayList;


public class Bet {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField
    private ArrayList<Integer> markets;

    @QuerySqlField
    private double stake;

    @QuerySqlField
    private int userId;

    public Bet(String id, double stake, int userId, ArrayList<Integer> markets) {
        this.id = id;
        this.markets = markets;
        this.stake = stake;
        this.userId = userId;
    }

    public Bet(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Integer> getMarkets() {
        return markets;
    }

    public void setMarkets(ArrayList<Integer> markets) {
        this.markets = markets;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "userId:'" + userId + '\'' +
                ", id:'" + id + '\'' +
                ", markets:" + markets +
                ", stake:" + stake +
                '}';
    }

}
