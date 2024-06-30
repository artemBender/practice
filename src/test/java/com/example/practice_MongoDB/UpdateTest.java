package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import com.example.practice_MongoDB.Service.UpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateTest {

    @Mock
    private MyObjectRepository myObjectRepository;

    @Mock
    private RevisionRepository revisionRepository;

    @InjectMocks
    private UpdateService updateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddObjectToRevision_Success() {
        MyObject myObject = new MyObject(false, new HashSet<>(), new HashSet<>());
        myObject.setId("object1");
        Revision revision = new Revision(new HashSet<>(), new HashSet<>(), LocalDate.now(), LocalDate.now(), 134, "color");
        revision.setId("revision1");

        when(myObjectRepository.findById("object1")).thenReturn(Optional.of(myObject));
        when(revisionRepository.findById("revision1")).thenReturn(Optional.of(revision));

        updateService.addObjectToRevision("revision1", "object1");

        verify(revisionRepository, times(1)).save(revision);
        verify(myObjectRepository, times(1)).save(myObject);
    }

    @Test
    public void testAddObjectToRevision_CycleError() {
        MyObject myObject = new MyObject(false, new HashSet<>(), new HashSet<>());
        myObject.setId("object1");
        myObject.setRoot(true);
        Revision revision = new Revision(new HashSet<>(), new HashSet<>(), LocalDate.now(), LocalDate.now(), 134, "color");
        revision.setId("revision1");
        revision.getMyObject().add(myObject);

        when(myObjectRepository.findById("object1")).thenReturn(Optional.of(myObject));
        when(revisionRepository.findById("revision1")).thenReturn(Optional.of(revision));

        updateService.addObjectToRevision("revision1", "object1");

        verify(revisionRepository, never()).save(revision);
        verify(myObjectRepository, never()).save(myObject);
    }

    @Test
    public void testAddRevisionToObject_Success() {
        MyObject myObject = new MyObject(false, new HashSet<>(), new HashSet<>());
        myObject.setId("object1");
        Revision revision = new Revision(new HashSet<>(), new HashSet<>(), LocalDate.now(), LocalDate.now(), 134, "color");
        revision.setId("revision1");

        when(myObjectRepository.findById("object1")).thenReturn(Optional.of(myObject));
        when(revisionRepository.findById("revision1")).thenReturn(Optional.of(revision));

        updateService.addRevisionToObject("object1", "revision1");

        verify(revisionRepository, times(1)).save(revision);
        verify(myObjectRepository, times(1)).save(myObject);
    }

    @Test
    public void testAddRevisionToObject_CycleError() {
        MyObject myObject = new MyObject(false, new HashSet<>(), new HashSet<>());
        myObject.setId("object1");
        Revision revision = new Revision(new HashSet<>(), new HashSet<>(), LocalDate.now(), LocalDate.now(), 134, "color");
        revision.setId("revision1");
        myObject.getRevisions().add(revision);

        when(myObjectRepository.findById("object1")).thenReturn(Optional.of(myObject));
        when(revisionRepository.findById("revision1")).thenReturn(Optional.of(revision));

        updateService.addRevisionToObject("object1", "revision1");

        verify(revisionRepository, never()).save(revision);
        verify(myObjectRepository, never()).save(myObject);
    }

    @Test
    public void testUpdateRevision_Success() {
        Revision revision = new Revision();
        revision.setId("revision1");
        revision.setStartDate(LocalDate.of(2020, 1, 1));
        revision.setEndDate(LocalDate.of(2020, 12, 31));
        revision.setAmount(100);
        revision.setColor("red");

        when(revisionRepository.findById("revision1")).thenReturn(Optional.of(revision));

        updateService.updateRevision("revision1", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 31), 200, "blue");

        assertEquals(LocalDate.of(2021, 1, 1), revision.getStartDate());
        assertEquals(LocalDate.of(2021, 12, 31), revision.getEndDate());
        assertEquals(200, revision.getAmount());
        assertEquals("blue", revision.getColor());
        verify(revisionRepository, times(1)).save(revision);
    }

    @Test
    public void testUpdateRevision_NoChanges() {
        Revision revision = new Revision();
        revision.setId("revision1");
        revision.setStartDate(LocalDate.of(2020, 1, 1));
        revision.setEndDate(LocalDate.of(2020, 12, 31));
        revision.setAmount(100);
        revision.setColor("red");

        when(revisionRepository.findById("revision1")).thenReturn(Optional.of(revision));

        updateService.updateRevision("revision1", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31), 100, "red");

        verify(revisionRepository, never()).save(revision);
    }
}
