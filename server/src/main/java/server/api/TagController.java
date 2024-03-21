package server.api;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.interfaces.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping(path = {"", "/"})
    public List<Tag> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable  long id) {
        return service.findById(id);
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        return service.add(tag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable long id, @RequestBody Tag updatedTag) {

        return service.update(id, updatedTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> delete(@PathVariable long id) {

        return service.delete(id);
    }


}
