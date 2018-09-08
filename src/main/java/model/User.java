package model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class User {

    @QuerySqlField
    private int id;

    @QuerySqlField
    private String name;

    @QuerySqlField
    private double stake;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.stake = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "{ " +
                "\"user\":{" +
                "\"id\": " + id  +
                ", \"name\":" + "\"" +name + "\"" +
                ", \"stake\": " + stake +
                "}" +
                "}";
    }

    public void incrementTotalStake(double stakeToAdd) {
        this.stake += stakeToAdd;
    }
}
