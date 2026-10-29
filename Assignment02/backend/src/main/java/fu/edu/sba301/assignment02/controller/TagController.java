package fu.edu.sba301.assignment02.controller;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.TagDto;
import fu.edu.sba301.assignment02.service.ITagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final ITagService tagService;

    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable Integer id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto dto) {
        TagDto created = tagService.createTag(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(@PathVariable Integer id, @Valid @RequestBody TagDto dto) {
        TagDto updated = tagService.updateTag(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}

