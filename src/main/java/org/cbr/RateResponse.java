package org.cbr;

public class RateResponse {
    private String code;
    private String rate;
    private String date;

    public RateResponse(String code, String rate, String date) {
        this.code = code;
        this.rate = rate;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
