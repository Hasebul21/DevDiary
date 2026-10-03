package com.example.devdiary.service;

import com.example.devdiary.Utils.IsValidStory;
import com.example.devdiary.dto.StoryDto;
import com.example.devdiary.dto.StoryDtoConverter;
import com.example.devdiary.dto.StoryPageDto;
import com.example.devdiary.entity.Storys;
import com.example.devdiary.entity.Tags;
import com.example.devdiary.entity.Users;
import com.example.devdiary.exception.AccessDeniedException;
import com.example.devdiary.exception.EntityNotFoundException;
import com.example.devdiary.repository.CommentRepository;
import com.example.devdiary.repository.LikeRepository;
import com.example.devdiary.repository.StoryRepository;
import com.example.devdiary.repository.TagRepository;
import com.example.devdiary.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class StoryService {

    private static final int DEFAULT_PAGE_SIZE = 6;
    private static final int MAX_PAGE_SIZE = 50;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final IsValidStory checkAuth;
    private final StoryDtoConverter storyDtoConverter;

    public StoryService(
            StoryRepository storyRepository,
            UserRepository userRepository,
            TagRepository tagRepository,
            CommentRepository commentRepository,
            LikeRepository likeRepository,
            IsValidStory checkAuth,
            StoryDtoConverter storyDtoConverter) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.checkAuth = checkAuth;
        this.storyDtoConverter = storyDtoConverter;
    }

    public List<StoryDto> getAllStory() {
        List<Storys> stories = storyRepository.findAll();
        Collections.reverse(stories);
        return toDtos(stories);
    }

    public StoryDto getSingleStory(int id) {
        return storyDtoConverter.getDetails(findStory(id));
    }

    public StoryPageDto getStoryPage(int pageNo, int pageSize) {
        int page = Math.max(pageNo, 0);
        int size = pageSize < 1 || pageSize > MAX_PAGE_SIZE ? DEFAULT_PAGE_SIZE : pageSize;
        Page<Storys> storyPage =
                storyRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));

        StoryPageDto pageDto = new StoryPageDto();
        pageDto.setStories(toDtos(storyPage.getContent()));
        pageDto.setPageNo(storyPage.getNumber());
        pageDto.setPageSize(storyPage.getSize());
        pageDto.setTotalElements(storyPage.getTotalElements());
        pageDto.setTotalPages(storyPage.getTotalPages());
        pageDto.setLast(storyPage.isLast());
        return pageDto;
    }

    public List<StoryDto> searchStory(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllStory();
        }
        String term = keyword.trim();
        return toDtos(
                storyRepository
                        .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByIdDesc(
                                term, term));
    }

    public List<StoryDto> getMyStory() {
        return toDtos(storyRepository.findByAuthorid_EmailOrderByIdDesc(checkAuth.getAuthName()));
    }

    public List<StoryDto> getStoryByUser(int userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException(Users.class, "id", String.valueOf(userId));
        }
        return toDtos(storyRepository.findByAuthorid_IdOrderByIdDesc(userId));
    }

    public List<StoryDto> getStoryByTag(String tagName) {
        return toDtos(storyRepository.findByTags_NameOrderByIdDesc(tagName.trim().toLowerCase()));
    }

    public StoryDto postStory(Storys story) {
        String email = checkAuth.getAuthName();
        Users author =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new EntityNotFoundException(Users.class, "email", email));

        story.setId(0);
        story.setAuthorid(author);
        story.setCreatedDate(new Date());
        story.setTags(resolveTags(story.getTags()));
        storyRepository.save(story);
        return storyDtoConverter.getDetails(story);
    }

    public StoryDto updateStory(int id, Storys story) {
        Storys existing = findEditableStory(id);
        existing.setTitle(story.getTitle());
        existing.setDescription(story.getDescription());
        if (story.getTags() != null) {
            existing.setTags(resolveTags(story.getTags()));
        }
        storyRepository.save(existing);
        return storyDtoConverter.getDetails(existing);
    }

    public void deleteStory(int id) {
        findEditableStory(id);
        commentRepository.deleteAll(commentRepository.findByStory_IdOrderByIdAsc(id));
        likeRepository.deleteAll(likeRepository.findByStory_Id(id));
        storyRepository.deleteById(id);
    }

    private Storys findStory(int id) {
        return storyRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Storys.class, "id", String.valueOf(id)));
    }

    private Storys findEditableStory(int id) {
        Optional<Storys> story = storyRepository.findById(id);
        if (story.isEmpty()) {
            throw new EntityNotFoundException(Storys.class, "id", String.valueOf(id));
        }
        if (!checkAuth.isValid(story) && !checkAuth.isAdmin()) {
            throw new AccessDeniedException("Unauthorized user");
        }
        return story.get();
    }

    private List<Tags> resolveTags(List<Tags> tags) {
        if (tags == null) {
            return List.of();
        }
        Map<String, Tags> resolved = new LinkedHashMap<>();
        tags.stream()
                .filter(Objects::nonNull)
                .map(Tags::getName)
                .filter(Objects::nonNull)
                .map(name -> name.trim().toLowerCase())
                .filter(name -> !name.isEmpty())
                .forEach(
                        name ->
                                resolved.computeIfAbsent(
                                        name,
                                        n ->
                                                tagRepository
                                                        .findByName(n)
                                                        .orElseGet(
                                                                () ->
                                                                        tagRepository.save(
                                                                                new Tags(n)))));
        return List.copyOf(resolved.values());
    }

    private List<StoryDto> toDtos(List<Storys> stories) {
        return stories.stream().map(storyDtoConverter::getDetails).toList();
    }
}
