package com.example.practice_MongoDB.Service;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UpdateService {

    @Autowired
    private MyObjectRepository myObjectRepository;

    @Autowired
    private RevisionRepository revisionRepository;

    //    Мы можем найти любой объект добавить в любую ревизию на него ссылку.
    //    НО нужно написать проверку, чтобы избежать зацикливания!
    public void addObjectToRevision(String revisionId, String objectId) {
        if (revisionId == null || objectId == null) {
            throw new IllegalArgumentException("Revision ID and Object ID must not be null");
        }
        Optional<MyObject> myObject = myObjectRepository.findById(objectId);
        Optional<Revision> revision = revisionRepository.findById(revisionId);

        if (myObject.isPresent() && revision.isPresent()) {
            for(MyObject myObj : revision.get().getMyObject())
                if (myObj.getId().equals(myObject.get().getId()) || myObject.get().getParentRevision() == null) {
                System.out.println("Adding error: cycle");
                return;
                }
            revision.get().getChildObjects().add(myObject.get());
            myObject.get().getParentRevision().add(revision.get());
            revisionRepository.save(revision.get());
            myObjectRepository.save(myObject.get());
            System.out.println("Object added");
        } else {
            System.out.println("Object or revision not found");
        }
    }
    // добавить любую ревизию к объекту
    public void addRevisionToObject(String objectId, String revisionId)
    {
        if (revisionId == null || objectId == null) {
            throw new IllegalArgumentException("Revision ID and Object ID must not be null");
        }
        Optional<MyObject> myObject = myObjectRepository.findById(objectId);
        Optional<Revision> revision = revisionRepository.findById(revisionId);

        if(myObject.isPresent() && revision.isPresent())
        {
            if(myObject.get().getParentRevision() == null) {
                System.out.println("Adding error: cycle");
                return;
            }
            for(Revision rev : myObject.get().getRevisions()) {
                    if (rev.getId().equals(revision.get().getId())) {
                        System.out.println("Adding error: cycle");
                        return;
                    }
                }
            myObject.get().getRevisions().add(revision.get());
            revision.get().getMyObject().add(myObject.get());
            revisionRepository.save(revision.get());
            myObjectRepository.save(myObject.get());
            System.out.println("Revision added");
        } else System.out.println("Object or revision not found");
    }

    public void updateRevision(String id, LocalDate startDate, LocalDate endDate, int amount, String color)
    {
        Optional<Revision> revisionOptional = revisionRepository.findById(id);
        if(revisionOptional.isPresent())
        {
            Revision revision = revisionOptional.get();
            if (!revision.getStartDate().equals(startDate) ||
                    !revision.getEndDate().equals(endDate) ||
                    !(revision.getAmount() == amount) ||
                    !revision.getColor().equals(color))
            {
                revision.setStartDate(startDate);
                revision.setEndDate(endDate);
                revision.setAmount(amount);
                revision.setColor(color);
                revisionRepository.save(revision);
                System.out.println("Revision updated");
            } else {
                System.out.println("No changes detected, revision not updated");
            }
        } else {
            System.out.println("Revision not found");
        }
    }

}
