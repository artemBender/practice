package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import com.example.practice_MongoDB.Service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class PrintTest {
    @Autowired
    private TreeTraversalService printTreeService;

    @Autowired
    private DeleteService deleteService;

    @Autowired
    private FilterService filterService;
    @Autowired
    private UpdateService updateService;

    @Autowired
    private AggregationService aggregationService;

    @Test
    public void Test() {
        Set<MyObject> rootObjects = printTreeService.findRootObjects();
        for (MyObject rootObject : rootObjects) {
            printTreeService.traverseTreeAndMeasureTime(rootObject);
        }
    }
}


