package org.crypto.cryptotrading.dto;

import java.util.List;

public class HuobiResponse {
    private String status;
    private long ts;
    private List<HuobiPrice> data;

    public HuobiResponse(String status, long ts, List<HuobiPrice> data) {
        this.status = status;
        this.ts = ts;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public List<HuobiPrice> getData() {
        return data;
    }

    public void setData(List<HuobiPrice> data) {
        this.data = data;
    }
}
