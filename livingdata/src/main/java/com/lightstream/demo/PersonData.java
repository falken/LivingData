package com.lightstream.demo;

import java.util.UUID;

public class PersonData {
    private UUID personId;
    private String firstName;
    private String lastName;
    private String eyeColor;
    private String hairColor;
    private Integer height;
    private Integer weight;
    private Integer age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setValue(String name, Object value) {
        if("first_name".equals(name)){
            setFirstName(value.toString());
        } else if("last_name".equals(name)){
            setLastName(value.toString());
        } else if("person_id".equals(name)){
            setPersonId((UUID) value);
        } else if("eye_color".equals(name)){
            setEyeColor(value.toString());
        } else if("hair_color".equals(name)){
            setHairColor(value.toString());
        } else if("weight".equals(name)){
            setWeight(Integer.parseInt(value.toString()));
        } else if("height".equals(name)){
            setHeight(Integer.parseInt(value.toString()));
        } else if("age".equals(name)){
            setAge(Integer.parseInt(value.toString()));
        }
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }
}
