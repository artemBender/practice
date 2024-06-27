package com.example.practice_MongoDB.Service;
import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class DeleteService {

    @Autowired
    private MyObjectRepository myObjectRepository;

    @Autowired
    private RevisionRepository revisionRepository;

//    Мы можем удалить ревизию, при условии, что она не последняя в объекте.
    public void deleteRevision (String revisionId)
    {
        if (revisionId == null) {
            throw new IllegalArgumentException("Revision ID must not be null");
        }

        Optional<Revision> revision = revisionRepository.findById(revisionId);
        if (revision.isPresent())
        {
            Set<MyObject> object = revision.get().getMyObject();
            for(MyObject myObject:object)
                if (myObject.getRevisions().size() == 1)
                {
                    System.out.println("Can't delete the last revision");
                    return;
                }
            for(MyObject myObject:object)
            {
                myObject.getRevisions().remove(revision.get());
                Set <MyObject> myObjects = revision.get().getChildObjects();
                for (MyObject myObj : myObjects)
                {
                    myObj.setParentRevision(new HashSet<>());
                    myObj.setRoot(true);
                }
                myObjectRepository.saveAll(myObjects);
                myObjectRepository.save(myObject);
                revisionRepository.delete(revision.get());
                System.out.println("Revision deleted");
            }
        }
        else System.out.println("Revision not found");
    }

//    Мы можем удалить объект при условии, что в нем только одна ревизия и внешние ревизии не ссылаются на объект.
//    Когда удаляем головной объект (у него только одна ревизия),
//    то все объекты на которые была ссылка с последнй ревизии объекта становятся головными.
    public void deleteObject (String objectId)
    {
        if (objectId == null) {
            throw new IllegalArgumentException("Object ID must not be null");
        }

        Optional<MyObject> myObject = myObjectRepository.findById(objectId);
        if(myObject.isPresent())
        {
            if(myObject.get().isRoot() && myObject.get().getRevisions().size() == 1)
            {
                Set<Revision> childRevision = myObject.get().getRevisions();
                for(Revision rev : childRevision) {
                    for (MyObject object : rev.getChildObjects())
                    {
                        object.setParentRevision(new HashSet<>());
                        object.setRoot(true);
                    }
                    myObjectRepository.saveAll(rev.getChildObjects());
                }
                myObjectRepository.delete(myObject.get());
                revisionRepository.deleteAll(childRevision);
                System.out.println("Object deleted");
            }
            else System.out.println("Can't delete object with more than 1 revision or/and not root object");

        }
        else System.out.println("Object not found");
    }

    //    Мы можем удалить ссылку на объект из ревизии.
    //    Если на объект никто не ссылается, он становится головным объектом.

    public void deleteObjectFromRevision (String revisionId, String objectId)
    {
        if (revisionId == null || objectId == null) {
            throw new IllegalArgumentException("Revision ID and Object ID must not be null");
        }

        Optional<MyObject> myObject = myObjectRepository.findById(objectId);
        Optional<Revision> revision = revisionRepository.findById(revisionId);
        if(myObject.isPresent() && revision.isPresent())
        {
            if (revision.get().getChildObjects().removeIf(dbRef -> dbRef.getId().equals(objectId))) {
                myObject.get().getParentRevision().removeIf(dbRef -> dbRef.getId().equals(revisionId));
                if (myObject.get().getParentRevision().isEmpty())
                {
                    myObject.get().setRoot(true);
                }
                revisionRepository.save(revision.get());
                myObjectRepository.save(myObject.get());
                System.out.println("Object deleted from revision");
            }
            else System.out.println("Delete error. Maybe object isn't child object for revision");

        }else System.out.println("Object or revision not found");
    }
    // Мы можем удалить ссылку на ревизию из объекта, при условии, что у него есть еще родитель-объект

    public  void deleteRevisionFromObject (String objectId, String revisionId)
    {
        if (revisionId == null || objectId == null) {
            throw new IllegalArgumentException("Revision ID and Object ID must not be null");
        }

        Optional<MyObject> myObject = myObjectRepository.findById(objectId);
        Optional<Revision> revision = revisionRepository.findById(revisionId);
        if(myObject.isPresent() && revision.isPresent())
        {
            if(revision.get().getMyObject().size() > 1)
            {
                if (myObject.get().getRevisions().removeIf(dbRef -> dbRef.getId().equals(revisionId))) {
                    revision.get().getMyObject().removeIf(dbRef -> dbRef.getId().equals(objectId));
                    revisionRepository.save(revision.get());
                    myObjectRepository.save(myObject.get());
                    System.out.println("Revision deleted from object");
                }
                else System.out.println("Delete error. Maybe object isn't child object for revision");
            }else System.out.println("Can't delete revision with 1 parent from object");
        }else System.out.println("Object or revision not found");
    }
}