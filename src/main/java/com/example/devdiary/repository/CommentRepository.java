package com.example.devdiary.repository;

import com.example.devdiary.entity.Comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Integer> {

    public List<Comments> findByStory_IdOrderByIdAsc(int storyId);
}
