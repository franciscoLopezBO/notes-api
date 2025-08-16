package com.notes_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.notes_api.dto.request.DeleteTagRequest;
import com.notes_api.dto.request.TagRequest;
import com.notes_api.dto.response.TagResponse;
import com.notes_api.model.Tag;
import com.notes_api.service.TagService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody TagRequest request) {
        Tag tag = tagService.createTag(request.userId, request.name);
        TagResponse response = new TagResponse(
        		tag.getId(),
        		tag.getName()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TagResponse>> getTagsByUser(@PathVariable Long userId) {
    	List<Tag> tags = tagService.getTagsByUser(userId);
    	List<TagResponse> response = tags
                .stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    	
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@RequestBody DeleteTagRequest request) {
        tagService.deleteTag(request.userId, request.id, request.replacementTagId);
        return ResponseEntity.noContent().build();
    }
}

