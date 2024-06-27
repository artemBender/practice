package com.example.practice_MongoDB.Repository;
import com.example.practice_MongoDB.Entity.Revision;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionRepository extends MongoRepository<Revision, String> {


}