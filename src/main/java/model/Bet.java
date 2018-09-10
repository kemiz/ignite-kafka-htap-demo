package model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.ArrayList;


public class Bet implements Serializable {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField
    private ArrayList<Long> markets;

    @QuerySqlField
    private double stake;

    @QuerySqlField
    private long userId;

    public Bet(String id, double stake, long userId, ArrayList<Long> markets) {
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

    public ArrayList<Long> getMarkets() {
        return markets;
    }

    public void setMarkets(ArrayList<Long> markets) {
        this.markets = markets;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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
