package com.example.practice_MongoDB.Service;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TreeTraversalService {

    @Autowired
    private MyObjectRepository myObjectRepository;

    public void traverseTreeAndMeasureTime(MyObject root) {
        // Measure time for iterative traversal
        long startTimeIterative = System.currentTimeMillis();
        traverseTree(root);
        long endTimeIterative = System.currentTimeMillis();
        long durationIterative = endTimeIterative - startTimeIterative;
        System.out.println("Traversal time: " + durationIterative + " ms");
        }

    private void traverseTree(MyObject root) {
        Deque<MyObject> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            MyObject current = stack.pop();
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
    }

    public Set<MyObject> findRootObjects() {
        List<MyObject> rootObjects = myObjectRepository.findRootObjects();
        return new HashSet<>(rootObjects);
    }

}
