package server.services.implementations;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestTagRepository;

import static org.junit.jupiter.api.Assertions.*;

class TagServiceTest {

    private TagServiceImpl tagService;
    private TestTagRepository repo;
    private Tag tag1;
    private Tag tag2;
    @BeforeEach
    public void setup() {
        repo = new TestTagRepository();
        tagService = new TagServiceImpl(repo);
        tag1 = new Tag("purple", "party");
        tag2 = new Tag("yellow", "burgers");
    }
    @Test
    void findAll() {
        tagService.add(tag1);
        assertEquals(0, tag1.getId());
        tagService.add(tag2);
        assertEquals(2, tagService.findAll().size());
    }

    @Test
    void findById() {
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}