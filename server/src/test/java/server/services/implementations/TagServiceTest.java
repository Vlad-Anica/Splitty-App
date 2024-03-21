package server.services.implementations;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private TagServiceImpl tagService;
    private TagRepository repo;
    private Tag tag1;
    private Tag tag2;
    @BeforeEach
    public void setup() {
        repo = mock(TagRepository.class);
        tagService = new TagServiceImpl(repo);
        tag1 = new Tag("purple", "party");
        tag2 = new Tag("yellow", "burgers");
        List<Tag> tags = new ArrayList<>();
    }
    @Test
    void findAll() {

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