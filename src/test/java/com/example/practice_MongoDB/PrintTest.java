package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class PrintTest {
    @Autowired
    private TreeTraversalService treeTraversalService;


    @Test
    public void Test() {
        Set<MyObject> rootObjects = treeTraversalService.findRootObjects();
        for (MyObject rootObject : rootObjects) {
            treeTraversalService.traverseTreeAndMeasureTime(rootObject);
        }
    }
}

