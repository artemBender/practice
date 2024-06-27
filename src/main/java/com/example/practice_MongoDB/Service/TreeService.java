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
public class TreeService {

    private static final int MAX_NODES = 100000;

    private final Random random = new Random();

    @Autowired
    private MyObjectRepository myObjectRepository;

    @Autowired
    private RevisionRepository revisionRepository;

    private int currentNodeCount = 0;

    public void generateTree() {
        myObjectRepository.deleteAll();
        revisionRepository.deleteAll();
        MyObject root = new MyObject(true, null, new HashSet<>());
        revisionRepository.saveAll(generateRevisions(root));
        myObjectRepository.save(root);
        currentNodeCount++;
        Deque<Revision> stack = new ArrayDeque<>(root.getRevisions());
        while (!stack.isEmpty() && currentNodeCount < MAX_NODES) {
            Revision currentRevision = stack.pop();
            Set<MyObject> childObjects = new HashSet<>();
            for (int j = 0; j < 10; j++) {
                if (currentNodeCount >= MAX_NODES) {
                    break;
                }
                MyObject childObject = new MyObject(false, new HashSet<>(), new HashSet<>());
                childObject.getParentRevision().add(currentRevision);
                childObjects.add(childObject);
                Set<Revision> childRevisions = new HashSet<>(generateRevisions(childObject));
                stack.addAll(childRevisions);
                revisionRepository.saveAll(childRevisions);
                myObjectRepository.save(childObject);
                currentNodeCount++;
            }
            currentRevision.setChildObjects(childObjects);
            revisionRepository.save(currentRevision);
        }
    }


    private List<Revision> generateRevisions(MyObject parentObject) {
        List<Revision> revisions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LocalDate startDate = LocalDate.of(2020,6,10).plusDays(random.nextInt(1460));
            Revision revision = new Revision(new HashSet<>(), new HashSet<>(), startDate, startDate.plusMonths(6), random.nextInt(1000), getRandomColor());
            revision.getMyObject().add(parentObject);
            revisions.add(revision);
        }
        parentObject.setRevisions(new HashSet<>(revisions));
        return revisions;
    }

    private String getRandomColor() {
        String[] colors = {"red", "green", "blue", "yellow", "black", "white"};
        return colors[random.nextInt(colors.length)];
    }
}
