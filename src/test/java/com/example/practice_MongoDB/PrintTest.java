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

//        Set<MyObject> rootObjects = printTreeService.findRootObjects();
//        for (MyObject rootObject : rootObjects) {
//            List<MyObject> myObjects = aggregationService.maxRevision(rootObject);
//            for (MyObject myObject : myObjects)
//            {
//                System.out.println(myObject.getId());
//                for (Revision revision : myObject.getRevisions())
//                    System.out.println(revision.getAmount());
//            }
//
//
//        }
//        Set<MyObject> rootObjects = printTreeService.findRootObjects();
//        for (MyObject rootObject : rootObjects) {
//            filterService.filterRevisions(rootObject, null, null, 250, "black");
//        }


//        Set<MyObject> rootObjects = printTreeService.findRootObjects();
//        for (MyObject rootObject : rootObjects) {
//            aggregationService.maxRevision(rootObject);
//        }

//        for (Revision revision : revisionList)
//        {
//            System.out.println(revision.getId());
//            System.out.println(revision.getStartDate());
//            System.out.println(revision.getEndDate());
//            System.out.println(revision.getAmount());
//            System.out.println(revision.getColor());
//            System.out.println("-------------");
//        }
 //       deleteService.deleteRevision("19365d04-a74c-494b-ab49-28ec50cac42e");
//        deleteService.deleteRevision("005620ea-0f67-416e-917b-df37aa125038");
//        deleteService.deleteRevision("af80aac1-add2-4e80-991a-9e9950b8ce04");
   //     deleteService.deleteObject("31b58467-d17e-49f3-b3a2-5dffd135e85c");
//        deleteService.deleteObjectFromRevision("6ae97a08-a580-45b9-8c13-370a880280c7", "31b58467-d17e-49f3-b3a2-5dffd135e85c");

       // updateService.addObjectToRevision("6091f167-b656-4e35-9b69-cdd124541c54", "adc3ab2f-447f-4c4b-9fc4-c9796f1dfe04");

        //updateService.addRevisionToObject("d8737f5f-8840-4e7b-b621-18aecc22af9e", "0907b8a6-8759-43e2-95f1-98ff9b0181de");
        //deleteService.deleteRevisionFromObject("e7f6228a-794d-4271-87c9-58416896a81b","0907b8a6-8759-43e2-95f1-98ff9b0181de");
    }

}


