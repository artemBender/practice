package com.example.practice_MongoDB.Service;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Service
public class TreeTraversalService {

    @Autowired
    private MyObjectRepository myObjectRepository;

    public void traverseTree(MyObject root) {
        Deque<Revision> stack = new ArrayDeque<>(root.getRevisions());
        while (!stack.isEmpty()) {
            Revision current = stack.pop();
            if (current.getChildObjects() != null) {
                for (MyObject myObject : current.getChildObjects()) {
                    if (myObject.getRevisions() != null) {
                        for (Revision child : myObject.getRevisions()) {
                            stack.push(child);
                        }
                    }
                }
            }
        }
    }


    public List<MyObject> findRootObjects() {
        return myObjectRepository.findRootObjects();
    }


    public List<MyObject> Lookup() {
        MatchOperation matchParentRevision = match(Criteria.where("parentRevision").isNull());

        LookupOperation lookupRevisions = LookupOperation.newLookup()
                .from("revision")
                .localField("revisions.$id")
                .foreignField("_id")
                .as("revisions");


        LookupOperation lookupChildObjects = LookupOperation.newLookup()
                .from("myObject")
                .localField("revisions.childObjects.$id")
                .foreignField("_id")
                .as("childObjects");

        LookupOperation lookupChildRevisions = LookupOperation.newLookup()
                .from("revision")
                .localField("childObjects.revisions.$id")
                .foreignField("_id")
                .as("childRevisions");


        LookupOperation lookupGrandChildObjects = LookupOperation.newLookup()
                .from("myObject")
                .localField("childRevisions.childObjects.$id")
                .foreignField("_id")
                .as("grandChildObjects");

        LookupOperation lookupGrandChildRevisions = LookupOperation.newLookup()
                .from("revision")
                .localField("grandChildObjects.revisions.$id")
                .foreignField("_id")
                .as("grandChildRevisions");


        Aggregation aggregation = Aggregation.newAggregation(
                matchParentRevision,
                lookupRevisions,
                lookupChildObjects,
                lookupChildRevisions,
                lookupGrandChildObjects,
                lookupGrandChildRevisions
        );

        AggregationResults<MyObject> results = mongoTemplate.aggregate(
                aggregation, "myObject", MyObject.class);

        return results.getMappedResults();
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<MyObject> findRootObject() {
        Query query = new Query();
        query.addCriteria(Criteria.where("parentRevision").is(null));

        return mongoTemplate.find(query, MyObject.class);
    }
}
