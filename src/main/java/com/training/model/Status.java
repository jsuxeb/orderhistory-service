package com.training.model;

import java.time.Instant;

public class Status {
    private String status;
    private Instant statusDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Instant statusDate) {
        this.statusDate = statusDate;
    }
}
