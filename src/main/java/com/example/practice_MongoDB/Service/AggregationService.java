package com.example.practice_MongoDB.Service;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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
                        objectsWithMaxAmount.clear();
                        objectsWithMaxAmount.add(current);
                    } else if (revision.getAmount() == maxAmount) {
                        objectsWithMaxAmount.add(current);
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


    public List<MyObject> minRevisionPath(MyObject root) {
        List<MyObject> path = new ArrayList<>();
        MyObject current = root;

        while (current != null) {
            path.add(current);

            if (current.getRevisions() == null || current.getRevisions().isEmpty()) {
                break;
            }

            Revision minRevision = null;
            int minAmount = Integer.MAX_VALUE;
            for (Revision revision : current.getRevisions()) {
                if (revision.getAmount() < minAmount) {
                    minAmount = revision.getAmount();
                    minRevision = revision;
                }
            }

            if (minRevision == null || minRevision.getChildObjects() == null || minRevision.getChildObjects().isEmpty()) {
                break;
            }

            MyObject nextObject = null;
            minAmount = Integer.MAX_VALUE;
            for (MyObject child : minRevision.getChildObjects()) {
                if (child.getRevisions() != null && !child.getRevisions().isEmpty()) {
                    int childMinAmount = child.getRevisions().stream()
                            .mapToInt(Revision::getAmount)
                            .min()
                            .orElse(Integer.MAX_VALUE);
                    if (childMinAmount < minAmount) {
                        minAmount = childMinAmount;
                        nextObject = child;
                    }
                }
            }

            current = nextObject;
        }

        return path;
    }

    public List<MyObject> maxRevisionPath(MyObject root) {
        List<MyObject> path = new ArrayList<>();
        MyObject current = root;

        while (current != null) {
            path.add(current);

            if (current.getRevisions() == null || current.getRevisions().isEmpty()) {
                break;
            }

            Revision maxRevision = null;
            int maxAmount = Integer.MIN_VALUE;
            for (Revision revision : current.getRevisions()) {
                if (revision.getAmount() > maxAmount) {
                    maxAmount = revision.getAmount();
                    maxRevision = revision;
                }
            }

            if (maxRevision == null || maxRevision.getChildObjects() == null || maxRevision.getChildObjects().isEmpty()) {
                break;
            }

            // Найти child объект с минимальным amount в ревизии
            MyObject nextObject = null;
            maxAmount = Integer.MIN_VALUE;
            for (MyObject child : maxRevision.getChildObjects()) {
                if (child.getRevisions() != null && !child.getRevisions().isEmpty()) {
                    int childMinAmount = child.getRevisions().stream()
                            .mapToInt(Revision::getAmount)
                            .min()
                            .orElse(Integer.MIN_VALUE);
                    if (childMinAmount > maxAmount) {
                        maxAmount = childMinAmount;
                        nextObject = child;
                    }
                }
            }

            current = nextObject;
        }

        return path;
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


    public void printMinPathRevision (MyObject root)
    {
        List<MyObject> objectsWithMinAmount = minRevisionPath(root);
        for (MyObject myObject : objectsWithMinAmount)
        {
            System.out.println("-------------");
            System.out.println("MyObjectID: " + myObject.getId());
            if(myObject.getParentRevision() != null)
                for (Revision revision : myObject.getParentRevision())
                    System.out.println("Parent RevisionID: " + revision.getId());
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


    public void printMaxPathRevision (MyObject root)
    {
        List<MyObject> objectsWithMaxAmount = maxRevisionPath(root);
        for (MyObject myObject : objectsWithMaxAmount)
        {
            System.out.println("-------------");
            System.out.println("MyObjectID: " + myObject.getId());
            if(myObject.getParentRevision() != null)
                for (Revision revision : myObject.getParentRevision())
                    System.out.println("Parent RevisionID: " + revision.getId());
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

    public void printAverage(MyObject root) {
        Deque<MyObject> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            MyObject current = stack.pop();
            Set<Revision> revisions = current.getRevisions();

            if (revisions != null) {
                int totalAmount = 0;
                int count = 0;

                for (Revision revision : revisions) {
                    int amount = revision.getAmount();
                    totalAmount += amount;
                    count++;
                    Set<MyObject> childObjects = revision.getChildObjects();
                    if (childObjects != null) {
                        for (MyObject child : childObjects) {
                            stack.push(child);
                        }
                    }
                }

                double averageAmount = count == 0 ? 0 : (double) totalAmount / count;
                System.out.println("Object: " + current.getId() + ", Average amount: " + averageAmount);
            }
        }
    }

    public void printSum(MyObject root) {
        Deque<MyObject> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            MyObject current = stack.pop();
            Set<Revision> revisions = current.getRevisions();

            if (revisions != null) {
                int totalAmount = 0;

                for (Revision revision : revisions) {
                    int amount = revision.getAmount();
                    totalAmount += amount;
                    Set<MyObject> childObjects = revision.getChildObjects();
                    if (childObjects != null) {
                        for (MyObject child : childObjects) {
                            stack.push(child);
                        }
                    }
                }
                System.out.println("Object: " + current.getId() + ", Average amount: " + totalAmount);
            }
        }
    }


    public int countObjects (MyObject root) {
        Deque<MyObject> stack = new ArrayDeque<>();
        stack.push(root);
        int q = 1;
        while (!stack.isEmpty()) {
            MyObject current = stack.pop();
            if (current.getRevisions() != null) {
                for (Revision revision : current.getRevisions()) {
                    if (revision.getChildObjects() != null) {
                        for (MyObject child : revision.getChildObjects()) {
                            stack.push(child);
                            q++;
                        }
                    }
                }
            }
        }
        return q;
    }
}
