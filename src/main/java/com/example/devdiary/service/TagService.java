package com.example.devdiary.service;

import com.example.devdiary.entity.Tags;
import com.example.devdiary.repository.TagRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<String> getAllTag() {
        return tagRepository.findAllByOrderByNameAsc().stream().map(Tags::getName).toList();
    }
}
