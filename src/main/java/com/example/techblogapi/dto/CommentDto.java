package com.example.techblogapi.dto;

import java.util.Date;

public class CommentDto {

    private int id;
    private String text;
    private String author;
    private int storyId;
    private Date CreatedDate;

    public CommentDto() {

    }

    public CommentDto(int id, String text, String author, int storyId) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.storyId = storyId;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }

    public int getStoryId() {

        return storyId;
    }

    public void setStoryId(int storyId) {

        this.storyId = storyId;
    }

    public Date getCreatedDate() {

        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {

        CreatedDate = createdDate;
    }
}
