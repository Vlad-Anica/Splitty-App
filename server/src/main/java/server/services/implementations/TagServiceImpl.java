package server.services.implementations;

import commons.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.database.TagRepository;
import server.services.interfaces.TagService;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository db;
    private final List<Tag> defaultTags;
    @Autowired
    public TagServiceImpl(TagRepository db) {
        this.db = db;
        defaultTags = List.of(
                new Tag("green", "Food"),
                new Tag("blue", "Entrance Fees"),
                new Tag("red", "Travel")
        );
        if (db.findAll().isEmpty())
            db.saveAll(defaultTags);
    }

    public List<Tag> findAll() {
        return db.findAll();
    }

    public ResponseEntity<Tag> findById(long id) {
        if (id < 0 || !db.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Tag> tag = db.findById(id);
        if (tag.isPresent())
            return ResponseEntity.ok(tag.get());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Override
    public Tag save(Tag tag) {
        return db.save(tag);
    }

    @Override
    public boolean existsById(long id) {
        return db.existsById(id);
    }

    public ResponseEntity<Tag> add(Tag tag) {
        if (tag == null || isNullOrEmpty(tag.getColor()) ||
                isNullOrEmpty(tag.getType()) ||
                defaultTags.contains(tag))
            return ResponseEntity.badRequest().build();

        Tag saved = db.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<Tag> update(long id, Tag updatedTag) {
        if (updatedTag.getId() != id)
            return ResponseEntity.badRequest().build();
        if (!db.existsById(id))
            return add(updatedTag);
        return ResponseEntity.ok(db.save(updatedTag));
    }

    public ResponseEntity<Tag> delete(long id) {
        if (!db.existsById(id))
            return ResponseEntity.badRequest().build();
        Tag tag = findById(id).getBody();
        if (defaultTags.contains(tag) || tag == null)
            return ResponseEntity.badRequest().build();
        db.delete(tag);
        return ResponseEntity.ok(tag);
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @Override
    public Tag getReferenceById(long id) throws EntityNotFoundException{
        if(id < 0) {
            throw new EntityNotFoundException();
        }
        return this.db.getReferenceById(id);
    }
}
