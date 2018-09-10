package model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

public class Market implements Serializable {

    @QuerySqlField
    private long id;

    @QuerySqlField
    private String market;

    @QuerySqlField
    private double odds;

    public Market(long id, String market, double odds) {
        this.id = id;
        this.market = market;
        this.odds = odds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public double getOdds() {
        return odds;
    }

    public void setOdds(double odds) {
        this.odds = odds;
    }

    @Override
    public String toString() {
        return "{ market {" +
                "id:" + id +
                ", market:'" + market + '\'' +
                ", odds:" + odds +
                '}';
    }
}
