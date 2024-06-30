package com.example.practice_MongoDB;

import com.example.practice_MongoDB.Entity.MyObject;
import com.example.practice_MongoDB.Entity.Revision;
import com.example.practice_MongoDB.Repository.MyObjectRepository;
import com.example.practice_MongoDB.Repository.RevisionRepository;
import com.example.practice_MongoDB.Service.CreateTreeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateTreeServiceTest {

    @Mock
    private MyObjectRepository myObjectRepository;

    @Mock
    private RevisionRepository revisionRepository;

    @InjectMocks
    private CreateTreeService createTreeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateTree() {
        MyObject root = new MyObject(true, null, new HashSet<>());
        when(myObjectRepository.save(any())).thenReturn(root);

        createTreeService.generateTree();

        verify(myObjectRepository, times(1)).deleteAll();
        verify(revisionRepository, times(1)).deleteAll();
        verify(revisionRepository, atLeastOnce()).save(any(Revision.class));
        verify(myObjectRepository, atLeastOnce()).save(any(MyObject.class));

        // Additional verification if needed
        assertEquals(100, createTreeService.getCurrentNodeCount()); // Assuming MAX_NODES is 100
        assertNotNull(root.getRevisions());

        // Verify that revisions are properly linked to objects
        for (Revision revision : root.getRevisions()) {
            assertNotNull(revision.getMyObject());
            assertTrue(revision.getMyObject().contains(root));
        }
    }
}
