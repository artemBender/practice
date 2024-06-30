package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Service.AggregationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AggregationTest {

    @Mock
    private MyObject mockRoot;

    @InjectMocks
    private AggregationService aggregationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMaxRevision() {

        MyObject object1 = new MyObject();
        object1.setId("object1");
        MyObject object2 = new MyObject();
        object2.setId("object2");

        Revision revision1 = new Revision(new HashSet<>(Collections.singletonList(mockRoot)), new HashSet<>(List.of(object1, object2)), null, null, 100, null);
        Revision revision2 = new Revision(new HashSet<>(Collections.singletonList(object1)), new HashSet<>(), null, null, 200, null);
        Revision revision3 = new Revision(new HashSet<>(Collections.singletonList(object2)), new HashSet<>(), null, null, 200, null);

        object1.setRevisions(new HashSet<>(Collections.singletonList(revision2)));
        object2.setRevisions(new HashSet<>(Collections.singletonList(revision3)));

        when(mockRoot.getRevisions()).thenReturn(new HashSet<>(List.of(revision1)));

        List<MyObject> result = aggregationService.maxRevision(mockRoot);

        // Verify result
        assertEquals(2, result.size());
        assertEquals("object1", result.get(0).getId());
        assertEquals("object2", result.get(1).getId());
    }

    @Test
    void testMinRevision() {
        MyObject object1 = new MyObject();
        object1.setId("object1");
        MyObject object2 = new MyObject();
        object2.setId("object2");

        Revision revision1 = new Revision(new HashSet<>(Collections.singletonList(mockRoot)), new HashSet<>(List.of(object1, object2)), null, null, 100, null);
        Revision revision2 = new Revision(new HashSet<>(Collections.singletonList(object1)), new HashSet<>(), null, null, 200, null);
        Revision revision3 = new Revision(new HashSet<>(Collections.singletonList(object2)), new HashSet<>(), null, null, 200, null);

        object1.setRevisions(new HashSet<>(Collections.singletonList(revision2)));
        object2.setRevisions(new HashSet<>(Collections.singletonList(revision3)));

        when(mockRoot.getRevisions()).thenReturn(new HashSet<>(List.of(revision1)));
        when(mockRoot.getId()).thenReturn("root");


        List<MyObject> result = aggregationService.minRevision(mockRoot);

        // Verify result
        assertEquals(1, result.size());
        assertEquals("root", result.get(0).getId());
    }

    @Test
    void testPrintAverage() {
        MyObject object1 = new MyObject();
        object1.setId("object1");
        MyObject object2 = new MyObject();
        object2.setId("object2");

        Revision revision1 = new Revision(new HashSet<>(Collections.singletonList(mockRoot)), new HashSet<>(List.of(object1, object2)), null, null, 100, null);
        Revision revision2 = new Revision(new HashSet<>(Collections.singletonList(object1)), new HashSet<>(), null, null, 200, null);
        Revision revision3 = new Revision(new HashSet<>(Collections.singletonList(object2)), new HashSet<>(), null, null, 200, null);
        Revision revision4 = new Revision(new HashSet<>(Collections.singletonList(object1)), new HashSet<>(), null, null, 700, null);
        Revision revision5 = new Revision(new HashSet<>(Collections.singletonList(object2)), new HashSet<>(), null, null, 40, null);

        object1.setRevisions(new HashSet<>(List.of(revision2,revision4)));
        object2.setRevisions(new HashSet<>(List.of(revision3,revision5)));

        when(mockRoot.getRevisions()).thenReturn(new HashSet<>(List.of(revision1)));
        when(mockRoot.getId()).thenReturn("root");


        aggregationService.printAverage(mockRoot);
    }

    @Test
    void testPrintSum() {
        MyObject object1 = new MyObject();
        object1.setId("object1");
        MyObject object2 = new MyObject();
        object2.setId("object2");

        Revision revision1 = new Revision(new HashSet<>(Collections.singletonList(mockRoot)), new HashSet<>(List.of(object1, object2)), null, null, 100, null);
        Revision revision2 = new Revision(new HashSet<>(Collections.singletonList(object1)), new HashSet<>(), null, null, 200, null);
        Revision revision3 = new Revision(new HashSet<>(Collections.singletonList(object2)), new HashSet<>(), null, null, 200, null);
        Revision revision4 = new Revision(new HashSet<>(Collections.singletonList(object1)), new HashSet<>(), null, null, 700, null);
        Revision revision5 = new Revision(new HashSet<>(Collections.singletonList(object2)), new HashSet<>(), null, null, 40, null);

        object1.setRevisions(new HashSet<>(List.of(revision2,revision4)));
        object2.setRevisions(new HashSet<>(List.of(revision3,revision5)));

        when(mockRoot.getRevisions()).thenReturn(new HashSet<>(List.of(revision1)));
        when(mockRoot.getId()).thenReturn("root");

        aggregationService.printSum(mockRoot);
    }
}

