package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.TagDto;
import fu.edu.sba301.assignment02.entity.Tag;
import fu.edu.sba301.assignment02.exception.BusinessException;
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
}

