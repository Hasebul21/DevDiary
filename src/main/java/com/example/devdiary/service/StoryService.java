package com.example.devdiary.service;

import com.example.devdiary.Utils.IsValidStory;
import com.example.devdiary.dto.StoryDto;
import com.example.devdiary.dto.StoryDtoConverter;
import com.example.devdiary.entity.Storys;
import com.example.devdiary.entity.Tags;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.entity.Comments;
import com.example.devdiary.entity.Likes;
import com.example.devdiary.repository.CommentRepository;
import com.example.devdiary.repository.LikeRepository;
import com.example.devdiary.repository.StoryRepository;
import com.example.devdiary.repository.TagRepository;
import com.example.devdiary.repository.UserRepository;
import com.example.devdiary.dto.StoryPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

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

    public List<StoryDto> searchStory(String keyword) {

        // nothing to search, so return everything
        if(keyword==null || keyword.trim().isEmpty()) return getAllStory();

        keyword=keyword.trim();
        List<Storys> found=storyRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByIdDesc(keyword,keyword);
        List<StoryDto> result=new ArrayList<>();
        for(Storys story : found){
            result.add(storyDtoConverter.getDetails(story));
        }
        return result;
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
        // id 0 means new story, otherwise save() will overwrite the story with that id
        story.setId(0);
        story.setCreatedDate(new Date());
        story.setTags(saveTags(story.getTags()));
        storyRepository.save(story);
        return storyDtoConverter.getDetails(story);
    }

    public StoryDto updateStory(int id, Storys story) {

        Optional<Storys> newStory=storyRepository.findById(id);
        if(newStory.isEmpty())  throw new EntityNotFoundException(Storys.class,"id",String.valueOf(id));
        if(checkAuth.isValid(newStory) || checkAuth.isAdmin()){

            Storys checkStory=newStory.get();
            checkStory.setTitle(story.getTitle());
            checkStory.setDescription(story.getDescription());
            // only change tags if client sent tags
            if(story.getTags()!=null) checkStory.setTags(saveTags(story.getTags()));
            storyRepository.save(checkStory);
            return storyDtoConverter.getDetails(checkStory);
        }
        throw new AccessDeniedException("Unauthorized user");

    }

    public void deleteStory(int id) {

        Optional<Storys> newStory=storyRepository.findById(id);
        if(newStory.isEmpty()) throw new EntityNotFoundException(Storys.class,"id",String.valueOf(id));
        if(checkAuth.isValid(newStory) || checkAuth.isAdmin()) {

            // first delete comments and likes of this story, otherwise database will not allow to delete the story
            List<Comments> comments=commentRepository.findByStory_IdOrderByIdAsc(id);
            commentRepository.deleteAll(comments);
            List<Likes> likes=likeRepository.findByStory_Id(id);
            likeRepository.deleteAll(likes);

            storyRepository.deleteById(id);
            return;
        }
        throw new AccessDeniedException("Unauthorized user");
    }

    public List<StoryDto> getStoryByTag(String tagName) {

        List<Storys> tagStory=storyRepository.findByTags_NameOrderByIdDesc(tagName.trim().toLowerCase());
        List<StoryDto> result=new ArrayList<>();
        for(Storys story : tagStory){
            result.add(storyDtoConverter.getDetails(story));
        }
        return result;
    }

    // find tag by name, if not found then create a new tag
    private List<Tags> saveTags(List<Tags> tags) {

        List<Tags> result=new ArrayList<>();
        if(tags==null) return result;

        List<String> addedNames=new ArrayList<>();
        for(Tags tag : tags){
            if(tag==null || tag.getName()==null) continue;
            String name=tag.getName().trim().toLowerCase();
            if(name.isEmpty() || addedNames.contains(name)) continue;

            Optional<Tags> oldTag=tagRepository.findByName(name);
            if(oldTag.isPresent()){
                result.add(oldTag.get());
            }
            else{
                Tags newTag=new Tags(name);
                result.add(tagRepository.save(newTag));
            }
            addedNames.add(name);
        }
        return result;
    }
}
