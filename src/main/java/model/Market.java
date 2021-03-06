package model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

public class Market implements Serializable {

    @QuerySqlField
    private String id;

    @QuerySqlField
    private String market;

    @QuerySqlField
    private double odds;

    @QuerySqlField
    private int totalCount;

    public Market(String id, String market, double odds) {
        this.id = id;
        this.market = market;
        this.odds = odds;
        this.totalCount = 0;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        return "Market{" +
                "id='" + id + '\'' +
                ", market='" + market + '\'' +
                ", odds=" + odds +
                ", totalCount=" + totalCount +
                '}';
    }
}
