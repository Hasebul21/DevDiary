package com.example.devdiary.dto;

import java.util.ArrayList;
import java.util.List;

public class LikeDto {

    private int count;
    // email of the users who liked the story
    private List<String> users=new ArrayList<>();

    public LikeDto() {

    }

    public LikeDto(int count, List<String> users) {
        this.count = count;
        this.users = users;
    }

    public int getCount() {

        return count;
    }

    public void setCount(int count) {

        this.count = count;
    }

    public List<String> getUsers() {

        return users;
    }

    public void setUsers(List<String> users) {

        this.users = users;
    }
}
