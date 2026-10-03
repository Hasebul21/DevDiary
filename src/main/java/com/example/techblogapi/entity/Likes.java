package com.example.techblogapi.entity;

import javax.persistence.*;

// one user can like one story only one time
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "story_id"}))
public class Likes {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Storys story;

    public Likes() {
    }

    public Likes(Users user, Storys story) {
        this.user = user;
        this.story = story;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
