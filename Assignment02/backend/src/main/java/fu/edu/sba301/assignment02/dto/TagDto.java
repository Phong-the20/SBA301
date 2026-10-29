package fu.edu.sba301.assignment02.dto;

/**
 * Author: Tran The Phong
 * Student ID: CE190157
 * Assignment: 02 - FUNewsManagementSystem
 */

import jakarta.validation.constraints.NotBlank;

public class TagDto {

    private Integer tagId;

    @NotBlank(message = "Tag name is required")
    private String tagName;

    private String note;

    public TagDto() {
    }

    public TagDto(Integer tagId, String tagName, String note) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.note = note;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

