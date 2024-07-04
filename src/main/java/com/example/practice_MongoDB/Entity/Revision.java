package com.example.practice_MongoDB.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Document(collection = "revision")
public class Revision {

    @Id
    private String id;

    @DBRef
    private Set<MyObject> myObject;

    @DBRef
    private Set<MyObject> childObjects;

    private LocalDate startDate;
    private LocalDate endDate;
    private int amount;
    private String color;

    public Revision(Set<MyObject> myObject, Set<MyObject> childObjects, LocalDate startDate, LocalDate endDate, int amount, String color) {
        this.id = java.util.UUID.randomUUID().toString();
        this.myObject = myObject;
        this.childObjects = childObjects;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.color = color;
    }

    public Revision() {
        this.id = java.util.UUID.randomUUID().toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Set<MyObject> getMyObject() {
        return myObject;
    }

    public void setMyObject(Set<MyObject> myObject) {
        this.myObject = myObject;
    }

    public Set<MyObject> getChildObjects() {
        return childObjects;
    }

    public void setChildObjects(Set<MyObject> childObjects) {
        this.childObjects = childObjects;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}