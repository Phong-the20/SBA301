package fu.edu.sba301.assignment02.service;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import fu.edu.sba301.assignment02.dto.TagDto;
import java.util.List;

public interface ITagService {
    List<TagDto> getAllTags();
    TagDto createTag(TagDto dto);
}

