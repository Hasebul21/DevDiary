package com.example.techblogapi.service;

import com.example.techblogapi.entity.Tags;
import com.example.techblogapi.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    // return only the tag names
    public List<String> getAllTag() {

        List<Tags> allTag=tagRepository.findAllByOrderByNameAsc();
        List<String> names=new ArrayList<>();
        for(Tags tag : allTag){
            names.add(tag.getName());
        }
        return names;
    }
}
