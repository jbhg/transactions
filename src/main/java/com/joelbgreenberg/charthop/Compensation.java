package com.joelbgreenberg.charthop;

public class Compensation {
    private String currency;
    private long base;
    private long grantShares;
    private String grantType;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getBase() {
        return base;
    }

    public void setBase(long base) {
        this.base = base;
    }

    public long getGrantShares() {
        return grantShares;
    }

    public void setGrantShares(long grantShares) {
        this.grantShares = grantShares;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
