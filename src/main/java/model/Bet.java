package model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.ArrayList;


public class Bet implements Serializable {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField
    private ArrayList<String> markets;

    @QuerySqlField
    private double stake;

    @QuerySqlField
    private String userId;

    public Bet(String id, double stake, String userId, ArrayList<String> markets) {
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

    public ArrayList<String> getMarkets() {
        return markets;
    }

    public void setMarkets(ArrayList<String> markets) {
        this.markets = markets;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
