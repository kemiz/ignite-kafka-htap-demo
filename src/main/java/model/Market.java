package model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Market {

    @QuerySqlField
    private int id;

    @QuerySqlField
    private String market;

    @QuerySqlField
    private double odds;

    public Market(int id, String market, double odds) {
        this.id = id;
        this.market = market;
        this.odds = odds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
                "id:" + id +
                ", market:'" + market + '\'' +
                ", odds:" + odds +
                '}';
    }
}