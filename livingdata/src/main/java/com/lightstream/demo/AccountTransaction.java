package com.lightstream.demo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class AccountTransaction {
    private UUID timeStamp;
    private BigDecimal amount;

    public UUID getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(UUID timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
