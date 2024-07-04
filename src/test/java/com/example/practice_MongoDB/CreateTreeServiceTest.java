package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import com.example.practice_MongoDB.Service.CreateTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CreateTreeServiceTest {

    @Autowired
    private CreateTreeService treeGeneratorService;

    @Autowired
    private MyObjectRepository myObjectRepository;

    @Autowired
    private RevisionRepository revisionRepository;

    @Test
    public void testGenerateTree() {
        treeGeneratorService.generateTree();

        long objectCount = myObjectRepository.count();
        long revisionCount = revisionRepository.count();

        assertTrue(objectCount > 0);
        assertTrue(revisionCount > 0);
    }
}