package com.example.techblogapi.service;

import com.example.techblogapi.Utils.IsValidStory;
import com.example.techblogapi.dto.StoryDto;
import com.example.techblogapi.dto.StoryDtoConverter;
import com.example.techblogapi.entity.Storys;
import com.example.techblogapi.entity.Users;
import com.example.techblogapi.exception.AccessDeniedException;
import com.example.techblogapi.exception.EntityNotFoundException;
import com.example.techblogapi.repository.StoryRepository;
import com.example.techblogapi.repository.UserRepository;
import com.example.techblogapi.dto.StoryPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IsValidStory checkAuth;

    @Autowired
    private StoryDtoConverter storyDtoConverter;



    public List<StoryDto> getAllStory() {

        List<Storys>allStudent=storyRepository.findAll();
        Collections.reverse(allStudent);
        return allStudent.stream().map(x->storyDtoConverter.getDetails(x)).toList();
    }

    public StoryDto getSingleStory(int id) {

        Optional<Storys> checkStory=storyRepository.findById(id);
        if(checkStory.isEmpty()) throw new EntityNotFoundException(Storys.class,"id",String.valueOf(id));
        return storyDtoConverter.getDetails(checkStory.get());

    }

    public StoryPageDto getStoryPage(int pageNo, int pageSize) {

        if(pageNo<0) pageNo=0;
        if(pageSize<1 || pageSize>50) pageSize=6;

        // newest story first
        Pageable pageable=PageRequest.of(pageNo,pageSize,Sort.by("id").descending());
        Page<Storys> storyPage=storyRepository.findAll(pageable);

        List<StoryDto> stories=new ArrayList<>();
        for(Storys story : storyPage.getContent()){
            stories.add(storyDtoConverter.getDetails(story));
        }

        StoryPageDto storyPageDto=new StoryPageDto();
        storyPageDto.setStories(stories);
        storyPageDto.setPageNo(storyPage.getNumber());
        storyPageDto.setPageSize(storyPage.getSize());
        storyPageDto.setTotalElements(storyPage.getTotalElements());
        storyPageDto.setTotalPages(storyPage.getTotalPages());
        storyPageDto.setLast(storyPage.isLast());
        return storyPageDto;
    }

    public List<StoryDto> getMyStory() {

        String userEmail= checkAuth.getAuthName();
        List<Storys> myStory=storyRepository.findByAuthorid_EmailOrderByIdDesc(userEmail);
        List<StoryDto> result=new ArrayList<>();
        for(Storys story : myStory){
            result.add(storyDtoConverter.getDetails(story));
        }
        return result;
    }

    public List<StoryDto> getStoryByUser(int userId) {

        Optional<Users> user=userRepository.findById(userId);
        if(user.isEmpty()) throw new EntityNotFoundException(Users.class,"id",String.valueOf(userId));
        List<Storys> userStory=storyRepository.findByAuthorid_IdOrderByIdDesc(userId);
        List<StoryDto> result=new ArrayList<>();
        for(Storys story : userStory){
            result.add(storyDtoConverter.getDetails(story));
        }
        return result;
    }

    public StoryDto postStory(Storys story)  {

        String userEmail= checkAuth.getAuthName();
        Optional<Users> currentUser=userRepository.findByEmail(userEmail);
        story.setAuthorid(currentUser.get());
        storyRepository.save(story);
        return storyDtoConverter.getDetails(story);
    }

    public StoryDto updateStory(int id, Storys story) {

        Optional<Storys> newStory=storyRepository.findById(id);
        if(newStory.isEmpty())  throw new EntityNotFoundException(Storys.class,"id",String.valueOf(id));
        if(checkAuth.isValid(newStory)){

            Storys checkStory=newStory.get();
            checkStory.setTitle(story.getTitle());
            checkStory.setDescription(story.getDescription());
            storyRepository.save(checkStory);
            return storyDtoConverter.getDetails(checkStory);
        }
        throw new AccessDeniedException("Unauthorized user");

    }

    public void deleteStory(int id) {

        Optional<Storys> newStory=storyRepository.findById(id);
        if(newStory.isEmpty()) throw new EntityNotFoundException(Storys.class,"id",String.valueOf(id));
        if(checkAuth.isValid(newStory)) {

            storyRepository.deleteById(id);
            return;
        }
        throw new AccessDeniedException("Unauthorized user");
    }
}
