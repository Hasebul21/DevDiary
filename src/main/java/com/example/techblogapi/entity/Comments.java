package com.example.techblogapi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Comment can not be empty")
    @Size(max = 1000, message = "Comment can have maximum 1000 character")
    @Column(length = 1000)
    private String text;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Storys story;

    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedDate=new Date(System.currentTimeMillis());

    public Comments() {
    }

    public Comments(String text) {
        this.text = text;
    }

    public Comments(int id, String text, Users user, Storys story) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.story = story;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Storys getStory() {
        return story;
    }

    public void setStory(Storys story) {
        this.story = story;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }
}
