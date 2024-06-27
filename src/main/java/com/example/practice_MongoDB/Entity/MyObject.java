package com.example.practice_MongoDB.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Set;

public class MyObject {

    @Id
    private String id;

    private boolean isRoot;
    @DBRef
    private Set<Revision> parentRevision;
    @DBRef
    private Set<Revision> revisions;

    public MyObject(boolean isRoot, Set<Revision> parentRevision, Set<Revision> revisions) {
        this.id = java.util.UUID.randomUUID().toString();
        this.revisions = revisions;
        this.parentRevision = parentRevision;
        this.isRoot = isRoot;
    }

    public Set<Revision> getParentRevision() {
        return parentRevision;
    }

    public void setParentRevision(Set<Revision> parentRevision) {
        this.parentRevision = parentRevision;
    }
    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
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