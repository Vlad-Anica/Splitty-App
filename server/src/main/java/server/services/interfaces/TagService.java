package server.services.interfaces;

import commons.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TagService {

    public List<Tag> findAll();
    public ResponseEntity<Tag> findById(long id);
    public ResponseEntity<Tag> add(Tag tag);
    public ResponseEntity<Tag> update(long id, Tag updatedTag);
    public ResponseEntity<Tag> delete(long id);
    public Tag getReferenceById(long id);

}
