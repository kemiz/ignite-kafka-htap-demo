package model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    @QuerySqlField
    private long id;

    @QuerySqlField
    private String name;

    @QuerySqlField
    private double stake;

    private ArrayList<String> betIds;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.stake = 0.0;
        this.betIds = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public ArrayList<String> getBetIds() {
        return betIds;
    }

    public void setBetIds(ArrayList<String> betIds) {
        this.betIds = betIds;
    }

    public void incrementTotalStake(double stakeToAdd) {
        this.stake += stakeToAdd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stake=" + stake +
                ", betIds=" + betIds +
                '}';
    }

    public void addBet(String betId) {
        this.betIds.add(betId);
    }

}
