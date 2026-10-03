package com.example.techblogapi.repository;

import com.example.techblogapi.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tags,Integer> {

    public Optional<Tags> findByName(String name);

    public List<Tags> findAllByOrderByNameAsc();

}
