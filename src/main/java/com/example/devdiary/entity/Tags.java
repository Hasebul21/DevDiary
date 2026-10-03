package com.example.devdiary.entity;

import javax.persistence.*;

@Entity
public class Tags {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    public Tags() {
    }

    // so that json like "tags":["java","spring"] also works
    public Tags(String name) {
        this.name = name;
    }

    public Tags(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
