package com.example.devdiary.entity;


import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Storys {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Users authorid;

    @NotBlank
    @NotEmpty
    @NotNull
    private String title;

    @NotBlank
    @NotEmpty
    @NotNull
    @Size(min=0,max = 10000)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "story_tags",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tags> tags=new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date CreatedDate=new Date(System.currentTimeMillis());

    public Storys() {
    }

    public Storys(int id, Users authorid, String title, String description) {
        this.id = id;
        this.authorid = authorid;
        this.title = title;
        this.description = description;
    }
    public Storys(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
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

    public Users getAuthorid() {

        return authorid;
    }

    public void setAuthorid(Users authorid) {

        this.authorid = authorid;
    }
    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    @Override
    public int hashCode() {
        int hash=7;
        return id*hash + title.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        Storys other = (Storys) obj;
        if (other.id == this.id&& Objects.equals(other.title, this.title)) return true;
        return false;
    }
}
