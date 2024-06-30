package com.example.practice_MongoDB;
import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;

import com.example.practice_MongoDB.Service.FilterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterServiceTest {

    @InjectMocks
    private FilterService filterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFilterRevisions_AllCriteriaMatch() {
        MyObject root = createTestRoot();

        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);
        Integer amount = 100;
        String color = "red";

        List<MyObject> result = filterService.filterRevisions(root, startDate, endDate, amount, color);

        assertEquals(1, result.size());
        MyObject filteredObject = result.get(0);
        assertEquals(1, filteredObject.getRevisions().size());

        Revision filteredRevision = filteredObject.getRevisions().iterator().next();
        assertEquals("revision1", filteredRevision.getId());
    }

    @Test
    public void testFilterRevisions_NoCriteriaMatch() {
        MyObject root = createTestRoot();

        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 12, 31);
        Integer amount = 200;
        String color = "blue";

        List<MyObject> result = filterService.filterRevisions(root, startDate, endDate, amount, color);

        assertEquals(0, result.size());
    }

    @Test
    public void testFilterRevisions_PartialCriteriaMatch() {
        MyObject root = createTestRoot();

        LocalDate startDate = LocalDate.of(2020, 1, 1);
        String color = "red";

        List<MyObject> result = filterService.filterRevisions(root, startDate, null, null, color);

        assertEquals(1, result.size());
        MyObject filteredObject = result.get(0);
        assertEquals(1, filteredObject.getRevisions().size());

        Revision filteredRevision = filteredObject.getRevisions().iterator().next();
        assertEquals("revision1", filteredRevision.getId());
    }

    private MyObject createTestRoot() {
        MyObject root = new MyObject(true, new HashSet<>(), new HashSet<>());
        root.setId("root");

        Revision revision1 = new Revision(new HashSet<>(), new HashSet<>(), LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31), 100, "red");
        revision1.setId("revision1");

        root.getRevisions().add(revision1);

        return root;
    }
}