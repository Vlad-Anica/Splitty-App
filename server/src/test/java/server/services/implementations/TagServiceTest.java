package server.services.implementations;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.TagRepository;
import server.services.interfaces.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private TagService tagService;
    @MockBean
    private TagRepository repo;
    private Tag tag1;
    private Tag tag2;
    private List<Tag> tags;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        repo = mock(TagRepository.class);
        tagService = new TagServiceImpl(repo);
        tag1 = Tag.builder().id(501L).color("red").type("Food").build();
        tag2 = Tag.builder().id(502L).color("blue").type("Drinks").build();
        tags = new ArrayList<>();
    }
    @Test
    void findAllTest() {

        tags.add(tag1);
        tags.add(tag2);

        when(repo.findAll()).thenReturn(tags);

        List<Tag> result = tagService.findAll();
        assertEquals(2, result.size());
        assertEquals("red", result.get(0).getColor());
        assertEquals("blue", result.get(1).getColor());
    }

    @Test
    void findByIdTest() {

        when(repo.save(tag1)).thenReturn(tag1);
        when(repo.existsById(501L)).thenReturn(false);
        when(repo.findById(501L)).thenReturn(Optional.of(tag1));

        ResponseEntity<Tag> response = tagService.add(tag1);
        when(repo.existsById(501L)).thenReturn(true);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertTrue(repo.existsById(501L));

        response = tagService.findById(501L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(501L, response.getBody().getId());
        assertEquals("red", response.getBody().getColor());
        assertEquals("Food", response.getBody().getType());
    }

    @Test
    void findByIdNonExistentTest() {

        when(repo.save(tag1)).thenReturn(tag1);
        when(repo.existsById(503L)).thenReturn(false);

        tagService.add(tag1);
        ResponseEntity<Tag> response = tagService.findById(503L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(repo.existsById(503L));
        assertNull(response.getBody());
    }

    @Test
    void findByNegativeIdTest() {

        when(repo.existsById(-2L)).thenReturn(false);

        ResponseEntity<Tag> response = tagService.findById(-2L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(repo.existsById(-2L));
        assertNull(response.getBody());

    }
    @Test
    void addTest() {

        when(repo.save(tag1)).thenReturn(tag1);
        when(repo.save(tag2)).thenReturn(tag2);

        ResponseEntity<Tag> response = tagService.add(tag1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = tagService.add(tag2);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(repo.existsById(501L)).thenReturn(true);
        when(repo.findById(501L)).thenReturn(Optional.of(tag1));

        when(repo.existsById(502L)).thenReturn(true);
        when(repo.findById(502L)).thenReturn(Optional.of(tag2));

        when(repo.findAll()).thenReturn(List.of(tag1, tag2));
        List<Tag> tags = tagService.findAll();

        assertEquals(2, tags.size());
        assertEquals(tag1, tagService.findById(501L).getBody());
        assertEquals(tag2, tagService.findById(502L).getBody());
    }

    @Test
    void addNullTest() {

        Tag improperTag = new Tag(1L, null, null);
        when(repo.existsById(1L)).thenReturn(false);

        ResponseEntity<Tag> response = tagService.add(improperTag);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertFalse(repo.existsById(1L));
        assertNull(response.getBody());
    }

    @Test
    void addExistentTest() {

        when(repo.save(tag1)).thenReturn(tag1);

        ResponseEntity<Tag> response = tagService.add(tag1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tag1, response.getBody());

        when(repo.existsById(501L)).thenReturn(true);
        when(repo.findById(501L)).thenReturn(Optional.of(tag1));

        response = tagService.add(tag1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(repo.findAll()).thenReturn(List.of(tag1));
        List<Tag> tags = tagService.findAll();

        assertEquals(1, tags.size());
        assertEquals(tag1, tagService.findById(501L).getBody());
    }

    @Test
    void addDefaultTest() {

        Tag defaultTag = new Tag("green", "Food");

        ResponseEntity<Tag> response = tagService.add(defaultTag);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

    }
    @Test
    void updateTest() {

        Tag newTag = new Tag(501L, "purple", "Foods");

        when(repo.save(tag1)).thenReturn(tag1);
        tagService.add(tag1);

        when(repo.save(newTag)).thenReturn(newTag);
        ResponseEntity<Tag> response = tagService.update(tag1.getId(), newTag);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        when(repo.existsById(501L)).thenReturn(true);
        when(repo.findById(501L)).thenReturn(Optional.of(newTag));

        response  = tagService.findById(501L);

        assertTrue(repo.existsById(501L));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newTag, response.getBody());
    }

    @Test
    void updateMismatchTest() {
        Tag newTag = new Tag(502L, "purple", "Foods");

        when(repo.save(tag1)).thenReturn(tag1);
        tagService.add(tag1);

        ResponseEntity<Tag> response = tagService.update(tag1.getId(), newTag);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        when(repo.existsById(501L)).thenReturn(true);
        when(repo.findById(501L)).thenReturn(Optional.of(tag1));

        response  = tagService.findById(501L);

        assertTrue(repo.existsById(501L));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag1, response.getBody());
    }
    @Test
    void deleteTest() {

        when(repo.save(tag1)).thenReturn(tag1);
        when(repo.save(tag2)).thenReturn(tag2);

        tagService.add(tag1);
        tagService.add(tag2);

        when(repo.existsById(tag1.getId())).thenReturn(true);
        when(repo.findById(501L)).thenReturn(Optional.of(tag1));

        ResponseEntity<Tag> response = tagService.delete(tag1.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag1, response.getBody());

        when(repo.findAll()).thenReturn(List.of(tag2));

        List<Tag> tags = tagService.findAll();
        assertEquals(1, tags.size());
        assertEquals(tag2, tags.get(0));
    }

    @Test
    void deleteNonExistentTest() {

        when(repo.existsById(1L)).thenReturn(false);

        ResponseEntity<Tag> response = tagService.delete(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteDefaultTest() {

        Tag defaultTag = new Tag("green", "Food");

        when(repo.existsById(defaultTag.getId())).thenReturn(true);
        when(repo.findById(defaultTag.getId())).thenReturn(Optional.of(defaultTag));

        ResponseEntity<Tag> response = tagService.delete(defaultTag.getId());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

    }
}