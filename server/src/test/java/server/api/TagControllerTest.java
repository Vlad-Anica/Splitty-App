package server.api;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.interfaces.TagService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagControllerTest {

    @InjectMocks
    private TagController tagController;

    @MockBean
    private TagService tagService;
    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tagService = mock(TagService.class);
        tagController = new TagController(tagService);
        tag1 = Tag.builder().id(1L).color("red").type("Food").build();
        tag2 = Tag.builder().id(2L).color("blue").type("Drinks").build();
    }

    @Test
    public void testGetAll() {
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        when(tagService.findAll()).thenReturn(tags);

        List<Tag> result = tagController.getAll();
        assertEquals(2, result.size());
        assertEquals("red", result.get(0).getColor());
        assertEquals("blue", result.get(1).getColor());
    }

    @Test
    public void testGetById() {

        when(tagService.findById(1L)).thenReturn(
                ResponseEntity.ok(tag1));
        tagController.add(tag1);
        ResponseEntity<Tag> response = tagController.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("red", response.getBody().getColor());
        assertEquals("Food", response.getBody().getType());
    }

    @Test
    public void testAdd() {

        when(tagService.add(tag1)).thenReturn(ResponseEntity.ok(tag1));
        when(tagService.add(tag2)).thenReturn(ResponseEntity.ok(tag2));
        ResponseEntity<Tag> response = tagController.add(tag1);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = tagController.add(tag2);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        when(tagService.findById(1L)).thenReturn(ResponseEntity.ok(tag1));
        when(tagService.findById(2L)).thenReturn(ResponseEntity.ok(tag2));
        when(tagService.findAll()).thenReturn(List.of(tag1, tag2));
        List<Tag> tags = tagController.getAll();
        assertEquals(2, tags.size());
        assertEquals(tag1, tagController.getById(1L).getBody());
        assertEquals(tag2, tagController.getById(2L).getBody());
    }

    /*
    @Test
    public void testUpdate() {

        when(tagService.add(tag1)).thenReturn(ResponseEntity.ok(tag1));
        tagController.add(tag1);
        when (tagService.update(tag1.getId(), tag2)).thenReturn(ResponseEntity.ok(tag2));
        tagController.update(tag1.getId(), tag2);
        when(tagService.findById(1L)).thenReturn(ResponseEntity.ok(tag2));
        assertEquals(tag2, tagController.getById(1L).getBody());
    }
    */

    @Test
    public void testDelete() {

        when(tagService.add(tag1)).thenReturn(ResponseEntity.ok(tag1));
        when(tagService.add(tag2)).thenReturn(ResponseEntity.ok(tag2));
        tagController.add(tag1);
        tagController.add(tag2);

        when(tagService.delete(1L)).thenReturn(ResponseEntity.ok(tag1));
        tagController.delete(1L);
        when(tagService.findAll()).thenReturn(List.of(tag2));
        assertEquals(1, tagController.getAll().size());
    }
}