package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import com.example.practice_MongoDB.Service.DeleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteServiceTest {

    @Mock
    private MyObjectRepository myObjectRepository;

    @Mock
    private RevisionRepository revisionRepository;

    @InjectMocks
    private DeleteService deleteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteRevision() {
        MyObject myObject = new MyObject(new HashSet<>(), new HashSet<>());
        myObject.setId("object1");
        Revision revision1 = new Revision(new HashSet<>(Collections.singletonList(myObject)), new HashSet<>(), LocalDate.now(), LocalDate.now(), 134, "red");
        revision1.setId("revision1");
        Revision revision2 = new Revision(new HashSet<>(Collections.singletonList(myObject)), new HashSet<>(), LocalDate.now(), LocalDate.now(), 1544, "blue");
        revision2.setId("revision2");

        myObject.getRevisions().add(revision1);
        myObject.getRevisions().add(revision2);

        when(revisionRepository.findById("revision1")).thenReturn(Optional.of(revision1));
        when(myObjectRepository.saveAll(any())).thenReturn(Collections.singletonList(myObject));
        when(myObjectRepository.save(any())).thenReturn(myObject);

        deleteService.deleteRevision("revision1");

        verify(revisionRepository, times(1)).findById("revision1");
        verify(revisionRepository, times(1)).delete(revision1);
        verify(myObjectRepository, times(1)).saveAll(any());
        verify(myObjectRepository, times(1)).save(myObject);
    }

    @Test
    void testDeleteObject() {
        String objectId = "object1";
        MyObject myObject = new MyObject();
        myObject.setId(objectId);
        Revision revision = new Revision();
        revision.setId("revision1");
        myObject.setRevisions(new HashSet<>(Collections.singletonList(revision)));
        revision.setChildObjects(new HashSet<>(Collections.singletonList(myObject)));

        when(myObjectRepository.findById(objectId)).thenReturn(Optional.of(myObject));

        deleteService.deleteObject(objectId);

        verify(myObjectRepository, times(1)).findById(objectId);
        verify(myObjectRepository, times(1)).delete(myObject);
        verify(revisionRepository, times(1)).deleteAll(any());
    }

    @Test
    void testDeleteObjectFromRevision() {
        String revisionId = "revision1";
        String objectId = "object1";
        MyObject myObject = new MyObject();
        myObject.setId(objectId);
        Revision revision = new Revision();
        revision.setId(revisionId);
        revision.setChildObjects(new HashSet<>(Collections.singletonList(myObject)));
        myObject.setParentRevision(new HashSet<>(Collections.singletonList(revision)));

        when(revisionRepository.findById(revisionId)).thenReturn(Optional.of(revision));
        when(myObjectRepository.findById(objectId)).thenReturn(Optional.of(myObject));

        deleteService.deleteObjectFromRevision(revisionId, objectId);

        verify(revisionRepository, times(1)).findById(revisionId);
        verify(myObjectRepository, times(1)).findById(objectId);
        verify(revisionRepository, times(1)).save(revision);
        verify(myObjectRepository, times(1)).save(myObject);
    }

    @Test
    void testDeleteRevisionFromObject() {
        String objectId = "object1";
        String revisionId = "revision1";
        MyObject myObject = new MyObject();
        myObject.setId(objectId);
        Revision revision = new Revision();
        revision.setId(revisionId);
        myObject.setRevisions(new HashSet<>(Collections.singletonList(revision)));
        revision.setMyObject(new HashSet<>(Collections.singletonList(myObject)));
        MyObject anotherObject = new MyObject();
        anotherObject.setId("object2");
        revision.getMyObject().add(anotherObject);

        when(revisionRepository.findById(revisionId)).thenReturn(Optional.of(revision));
        when(myObjectRepository.findById(objectId)).thenReturn(Optional.of(myObject));

        deleteService.deleteRevisionFromObject(objectId, revisionId);

        verify(revisionRepository, times(1)).findById(revisionId);
        verify(myObjectRepository, times(1)).findById(objectId);
        verify(revisionRepository, times(1)).save(revision);
        verify(myObjectRepository, times(1)).save(myObject);
    }
}
