package com.example.devdiary.repository;

import com.example.devdiary.entity.Storys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Storys, Integer> {

    public List<Storys> findByAuthorid_EmailOrderByIdDesc(String email);

    public List<Storys> findByAuthorid_IdOrderByIdDesc(int id);

    public List<Storys> findByTags_NameOrderByIdDesc(String name);

    public List<Storys>
            findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByIdDesc(
                    String title, String description);
}
