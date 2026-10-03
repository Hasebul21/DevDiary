package com.example.devdiary.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoryDto {

    private int id;
    private String author;
    private String title;
    private String description;
    private Date CreatedDate;
    private List<String> tags = new ArrayList<>();

    public StoryDto() {}

    public StoryDto(int id, String author, String title, String description) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }
}
