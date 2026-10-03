package com.example.devdiary.repository;

import com.example.devdiary.entity.Likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Integer> {

    public List<Likes> findByStory_Id(int storyId);

    public Optional<Likes> findByStory_IdAndUser_Email(int storyId, String email);
}
