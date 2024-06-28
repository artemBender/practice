package com.example.practice_MongoDB.Service;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AggregationService {

    public List<MyObject> maxRevision(MyObject root) {
        Deque<MyObject> stack = new ArrayDeque<>();
        stack.push(root);

        List<MyObject> objectsWithMaxAmount = new ArrayList<>();
        int maxAmount = Integer.MIN_VALUE;

        while (!stack.isEmpty()) {
            MyObject current = stack.pop();
            if (current.getRevisions() != null) {
                for (Revision revision : current.getRevisions()) {
                    if (revision.getAmount() > maxAmount) {
                        maxAmount = revision.getAmount();
                        objectsWithMaxAmount.clear(); // Clear the list if a new max is found
                        objectsWithMaxAmount.add(current);
                    } else if (revision.getAmount() == maxAmount) {
                        objectsWithMaxAmount.add(current); // Add to the list if it's the same max
                    }
                    if (revision.getChildObjects() != null) {
                        for (MyObject child : revision.getChildObjects()) {
                            stack.push(child);
                        }
                    }
                }
            }
        }
        return objectsWithMaxAmount;
    }

    public void printMaxRevision (MyObject root)
    {
        List<MyObject> objectsWithMaxAmount = maxRevision(root);
        for (MyObject myObject : objectsWithMaxAmount)
        {
            System.out.println("-------------");
            System.out.println("MyObjectID: " + myObject.getId());
            System.out.println("Revisions:");
            System.out.println();
            for (Revision revision : myObject.getRevisions())
            {
                System.out.println("RevisionID: " + revision.getId());
                System.out.println("Amount: " + revision.getAmount());
            }
            System.out.println("-------------");
            System.out.println();
        }
    }

    public List<MyObject> minRevision(MyObject root) {
        Deque<MyObject> stack = new ArrayDeque<>();
        stack.push(root);
        List<MyObject> objectsWithMinAmount = new ArrayList<>();
        int minAmount = Integer.MAX_VALUE;
        while (!stack.isEmpty()) {
            MyObject current = stack.pop();
            if (current.getRevisions() != null) {
                for (Revision revision : current.getRevisions()) {
                    if (revision.getAmount() < minAmount) {
                        minAmount = revision.getAmount();
                        objectsWithMinAmount.clear(); // Clear the list if a new min is found
                        objectsWithMinAmount.add(current);
                    } else if (revision.getAmount() == minAmount) {
                        objectsWithMinAmount.add(current); // Add to the list if it's the same min
                    }
                    if (revision.getChildObjects() != null) {
                        for (MyObject child : revision.getChildObjects()) {
                            stack.push(child);
                        }
                    }
                }
            }
        }
        return objectsWithMinAmount;
    }


    public void printMinRevision (MyObject root)
    {
        List<MyObject> objectsWithMinAmount = minRevision(root);
        for (MyObject myObject : objectsWithMinAmount)
        {
            System.out.println("-------------");
            System.out.println("MyObjectID: " + myObject.getId());
            System.out.println("Revisions:");
            System.out.println();
            for (Revision revision : myObject.getRevisions())
            {
                System.out.println("RevisionID: " + revision.getId());
                System.out.println("Amount: " + revision.getAmount());
            }
            System.out.println("-------------");
            System.out.println();
        }
    }
}
