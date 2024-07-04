package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class PrintTest {
    @Autowired
    private TreeTraversalService treeTraversalService;

// Тест с проверкой методов обхода, фильтрации, агрегирования
    @Autowired
    private FilterService filterService;

    @Autowired
    private MyObjectRepository myObjectRepository;

    @Autowired
    private AggregationService aggregationService;
    @Test
    public void Test() {

        // обходы
//        List<MyObject> rootObjects = treeTraversalService.findRootObjects();
//        for (MyObject rootObject : rootObjects)
//        {
//            treeTraversalService.traverseTree(rootObject);
//        }

//       treeTraversalService.Lookup();


        // замер времени на запрос
        long start = System.currentTimeMillis();
        List<MyObject> rootObjects = filterService.findObjectsWithGreenRevisions();
        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println(total + "ms");
        start = System.currentTimeMillis();
        for(MyObject root : rootObjects)
        {
            System.out.println(aggregationService.countObjects(root));
        }
        end = System.currentTimeMillis();
        total = end - start;
        System.out.println(total + "ms");

    }
}

