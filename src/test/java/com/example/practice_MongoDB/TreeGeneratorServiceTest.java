package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import com.example.practice_MongoDB.Service.TreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TreeGeneratorServiceTest {

    @Autowired
    private TreeService treeGeneratorService;

    @Autowired
    private MyObjectRepository myObjectRepository;

    @Autowired
    private RevisionRepository revisionRepository;

    @Test
    public void testGenerateTree() {
        treeGeneratorService.generateTree();
        // Check if the root object exists
        long objectCount = myObjectRepository.count();
        long revisionCount = revisionRepository.count();

        // Ensure that objects and revisions have been created
        assertTrue(objectCount > 0);
        assertTrue(revisionCount > 0);
    }
}