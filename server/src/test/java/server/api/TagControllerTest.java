package server.api;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.services.interfaces.TagService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    private TagController tagController;
    private TagService tagService;
    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    public void setUp() {
        tagService = mock(TagService.class);
        tagController = new TagController(tagService);
        tag1 = new Tag(1L,"purple", "party");
        tag2 = new Tag(2L,"yellow", "burgers");
    }

    @Test
    public void testGetAll() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(1L, "green", "Food"));
        tags.add(new Tag(2L, "blue", "Entrance Fees"));

        when(tagService.findAll()).thenReturn(tags);

        List<Tag> result = tagController.getAll();
        assertEquals(2, result.size());
        assertEquals("green", result.get(0).getColor());
        assertEquals("blue", result.get(1).getColor());
    }

    @Test
    public void testGetById() {

        when(tagService.findById(1L)).thenReturn(
                ResponseEntity.ok(new Tag("green", "Food")));

        assertEquals(new Tag("green", "Food"), tagController.getById(1L).getBody());
    }

    @Test
    public void testAdd() {

        when(tagService.add(tag1)).thenReturn(ResponseEntity.ok(tag1));
        when(tagService.add(tag2)).thenReturn(ResponseEntity.ok(tag2));
        tagController.add(tag1);
        tagController.add(tag2);

        when(tagService.findById(1L)).thenReturn(ResponseEntity.ok(tag1));
        when(tagService.findById(2L)).thenReturn(ResponseEntity.ok(tag2));

        assertEquals(tag1, tagController.getById(1L).getBody());
        assertEquals(tag2, tagController.getById(2L).getBody());
    }

    @Test
    public void testUpdate() {

        when(tagService.add(tag1)).thenReturn(ResponseEntity.ok(tag1));
        tagController.add(tag1);
        when (tagService.update(tag1.getId(), tag2)).thenReturn(ResponseEntity.ok(tag2));
        tagController.update(tag1.getId(), tag2);
        when(tagService.findById(1L)).thenReturn(ResponseEntity.ok(tag2));
        assertEquals(tag2, tagController.getById(1L).getBody());
    }

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