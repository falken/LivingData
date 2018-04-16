package com.lightstream.demo;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.UUID;


public class Person {

    private UUID personId;
    private String personName;

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Person(UUID personId, String personName) {
        this.personId = personId;
        this.personName = personName;
    }
}
