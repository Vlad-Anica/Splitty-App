package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.services.implementations.TagServiceImpl;
import server.services.interfaces.TagService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagControllerTest {

    private ObjectMapper objectMapper;

    private TestTagRepository repo;

    private TagService service;
    private TagController sut;
    private Tag tag;
    @BeforeEach
    public void setup() {

        repo = new TestTagRepository();
        service = new TagServiceImpl(repo);
        sut = new TagController(service);
        tag = new Tag("purple", "Drinks");
    }
    @Test
    void getAll() {

        sut.add(new Tag("red", "drinks"));
        sut.add(new Tag("blue", "food"));
        assertEquals(2, sut.getAll().size());
    }

    @Test
    void getById() {
        sut.add(tag);
        assertEquals(0, tag.getId());
        assertEquals(tag, repo.getById(0L));
    }

    @Test
    void add() {
        Tag tag1 = sut.add(new Tag("purple", "Drinks")).getBody();
        assertEquals(tag, tag1);
        assertEquals(1, sut.getAll().size());
    }

    @Test
    void update() {
        sut.add(tag);
        Tag newTag = new Tag("purple", "newName");
        assertEquals(newTag, sut.update(tag.getId(), newTag).getBody());
    }

    @Test
    void delete() {

        sut.add(tag);
        sut.delete(tag.getId());
        assertEquals(0, sut.getAll().size());
    }
}