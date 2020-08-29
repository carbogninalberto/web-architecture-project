package com.carbogninalberto.util.response;

import java.io.Serializable;

public class ResponseRealTime extends Response implements Serializable {

    long usage;
    long timestamp;

    public ResponseRealTime(String msg, long usage) {
        super(msg);
        this.usage = usage;
        this.timestamp = System.currentTimeMillis();
    }

    public long getUsage() {
        return usage;
    }

    public void setUsage(long usage) {
        this.usage = usage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
