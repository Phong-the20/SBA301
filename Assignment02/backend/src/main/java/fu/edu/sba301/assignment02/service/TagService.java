package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.TagDto;
import fu.edu.sba301.assignment02.entity.Tag;
import fu.edu.sba301.assignment02.exception.BusinessException;
import fu.edu.sba301.assignment02.exception.ResourceNotFoundException;
import fu.edu.sba301.assignment02.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService implements ITagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
            .map(t -> new TagDto(t.getTagId(), t.getTagName(), t.getNote()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto getTagById(Integer id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return new TagDto(tag.getTagId(), tag.getTagName(), tag.getNote());
    }

    @Override
    public TagDto createTag(TagDto dto) {
        if (dto == null || dto.getTagName() == null || dto.getTagName().trim().isEmpty()) {
            throw new BusinessException("Tag name cannot be empty.");
        }
        String name = dto.getTagName().trim();
        if (tagRepository.existsByTagNameIgnoreCase(name)) {
            throw new BusinessException("Tag '" + name + "' already exists.");
        }
        Tag tag = new Tag();
        tag.setTagName(name);
        tag.setNote(dto.getNote());
        Tag saved = tagRepository.save(tag);
        return new TagDto(saved.getTagId(), saved.getTagName(), saved.getNote());
    }

    @Override
    public TagDto updateTag(Integer id, TagDto dto) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        if (dto == null || dto.getTagName() == null || dto.getTagName().trim().isEmpty()) {
            throw new BusinessException("Tag name cannot be empty.");
        }
        String name = dto.getTagName().trim();
        if (!tag.getTagName().equalsIgnoreCase(name) && tagRepository.existsByTagNameIgnoreCase(name)) {
            throw new BusinessException("Tag '" + name + "' already exists.");
        }
        tag.setTagName(name);
        tag.setNote(dto.getNote());
        Tag updated = tagRepository.save(tag);
        return new TagDto(updated.getTagId(), updated.getTagName(), updated.getNote());
    }

    @Override
    public void deleteTag(Integer id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        tagRepository.delete(tag);
    }
}

