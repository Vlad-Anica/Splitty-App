package server.api;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TagRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private final TagRepository db;

    private final List<Tag> defaultTags;
    public TagController(TagRepository db) {
        this.db = db;
        defaultTags = List.of(
                new Tag("green", "Food"),
                new Tag("blue", "Entrance Fees"),
                new Tag("red", "Travel")
        );
       if (db.findAll().isEmpty())
         db.saveAll(defaultTags);
    }

    @GetMapping(path = {"", "/"})
    public List<Tag> getAll() {
        return db.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable  long id) {
        if (id < 0 || !db.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Tag> tag = db.findById(id);
        if (tag.isPresent())
            return ResponseEntity.ok(tag.get());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {

        if (tag == null || isNullOrEmpty(tag.getColor()) ||
        isNullOrEmpty(tag.getType()) || db.existsById(tag.getId()) ||
        defaultTags.contains(tag))
            return ResponseEntity.badRequest().build();

        Tag saved = db.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable long id, @RequestBody Tag updatedTag) {

        if (updatedTag.getId() != id)
            return ResponseEntity.badRequest().build();
        if (!db.existsById(id))
            return add(updatedTag);
        return ResponseEntity.ok(db.save(updatedTag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> delete(@PathVariable long id) {

        if (!db.existsById(id))
            return ResponseEntity.badRequest().build();
        Tag tag = getById(id).getBody();
        if (defaultTags.contains(tag) || tag == null)
            return ResponseEntity.badRequest().build();
        db.delete(tag);
        return ResponseEntity.ok(tag);
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
