package com.lightstream.demo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class PersonAccount {
    private UUID personId;
    private BigDecimal currentBalance;
    private UUID lastUpdate;

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public UUID getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(UUID lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
