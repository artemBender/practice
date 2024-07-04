package com.example.practice_MongoDB.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "myObject")
public class MyObject {

    @Id
    private String id;

    @DBRef
    private Set<Revision> parentRevision;
    @DBRef
    private Set<Revision> revisions;

    public MyObject(Set<Revision> parentRevision, Set<Revision> revisions) {
        this.id = java.util.UUID.randomUUID().toString();
        this.revisions = revisions;
        this.parentRevision = parentRevision;
    }

    public Set<Revision> getParentRevision() {
        return parentRevision;
    }

    public void setParentRevision(Set<Revision> parentRevision) {
        this.parentRevision = parentRevision;
    }


    public void setId(String id) {
        this.id = id;
    }

    public MyObject() {
        this.id = java.util.UUID.randomUUID().toString();
    }

    public Set<Revision> getRevisions() {
        return revisions;
    }

    public void setRevisions(Set<Revision> revisions) {
        this.revisions = revisions;
    }

    public String getId() {
        return id;
    }
}