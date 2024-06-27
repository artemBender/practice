package com.example.practice_MongoDB.Service;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilterService {

    @Autowired
    private MyObjectRepository myObjectRepository;

    @Autowired
    private RevisionRepository revisionRepository;

    public void filterRevisions(MyObject root, LocalDate startDate, LocalDate endDate, Integer amount, String color) {
        Deque<MyObject> stack = new ArrayDeque<>();
        stack.push(root);

        List<MyObject> resultObjects = new ArrayList<>();

        while (!stack.isEmpty()) {
            MyObject current = stack.pop();
            List<Revision> filteredRevisions = current.getRevisions().stream()
                    .filter(revision -> (startDate == null || !revision.getStartDate().isBefore(startDate)))
                    .filter(revision -> (endDate == null || !revision.getEndDate().isAfter(endDate)))
                    .filter(revision -> (amount == null || revision.getAmount() == amount))
                    .filter(revision -> (color == null || revision.getColor().equals(color)))
                    .toList();

            if (!filteredRevisions.isEmpty()) {
                MyObject filteredObject = new MyObject(current.isRoot(), current.getParentRevision(), new HashSet<>(filteredRevisions));
                resultObjects.add(filteredObject);
            }

            if (current.getRevisions() != null) {
                for (Revision revision : current.getRevisions()) {
                    if (revision.getChildObjects() != null) {
                        for (MyObject child : revision.getChildObjects()) {
                            stack.push(child);
                        }
                    }
                }
            }
        }
        if (!resultObjects.isEmpty())
        {
            for (MyObject myObject : resultObjects)
            {
                System.out.println("-------------");
                System.out.println("MyObjectID: " + myObject.getId());
                System.out.println("Revisions:");
                System.out.println();
                for (Revision revision : myObject.getRevisions())
                {
                    System.out.println("RevisionID: " + revision.getId());
                    System.out.println("Start date: " + revision.getStartDate());
                    System.out.println("End date: " + revision.getEndDate());
                    System.out.println("Amount: " + revision.getAmount());
                    System.out.println("Color: " + revision.getColor());
                    System.out.println("-------------");
                }
                System.out.println();
            }
        }
        else System.out.println("No revisions found");
    }
}

