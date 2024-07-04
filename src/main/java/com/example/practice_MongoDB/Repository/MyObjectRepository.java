package com.example.practice_MongoDB.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.practice_MongoDB.Entity.MyObject;

import java.util.List;

@Repository
public interface MyObjectRepository extends MongoRepository<MyObject, String> {
    @Query("{ 'parentRevision': null }")
    List<MyObject> findRootObjects();
}