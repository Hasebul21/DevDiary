package com.example.techblogapi.repository;

import com.example.techblogapi.entity.Storys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Storys,Integer> {

    // find all story of a user by user email
    public List<Storys> findByAuthorid_EmailOrderByIdDesc(String email);

    // find all story of a user by user id
    public List<Storys> findByAuthorid_IdOrderByIdDesc(int id);

}
