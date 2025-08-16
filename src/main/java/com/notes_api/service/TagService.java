package com.notes_api.service;

import org.springframework.stereotype.Service;

import com.notes_api.model.Note;
import com.notes_api.model.Tag;
import com.notes_api.model.UserAccount;
import com.notes_api.repository.NoteRepository;
import com.notes_api.repository.TagRepository;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final NoteRepository noteRepository;
    
    public TagService(TagRepository tagRepository, NoteRepository noteRepository) {
    	this.tagRepository = tagRepository;
    	this.noteRepository = noteRepository;
    }

    public Tag createTag(Long userId, String name) {
        if (tagRepository.findByUserIdAndName(userId, name).isPresent()) {
            throw new IllegalArgumentException("Tag name already exists for this user");
        }
        
        Tag tag = new Tag();
        UserAccount user = new UserAccount();
        user.setId(userId);  
        tag.setUser(user);
        tag.setName(name);
        
        return tagRepository.save(tag);
    }

    public List<Tag> getTagsByUser(Long userId) {
        return tagRepository.findByUserId(userId);
    }

    public void deleteTag(Long userId, Long tagId, Long reassignTagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
        if (!tag.getUser().getId().equals(userId)) {
            throw new SecurityException("User cannot delete this tag");
        }

        if (reassignTagId != null) {
            Tag reassignTag = tagRepository.findById(reassignTagId)
                    .orElseThrow(() -> new IllegalArgumentException("Reassign tag not found"));
            
            var notes = noteRepository.findAll().stream()
                    .filter(note -> note.getTags().contains(tag))
                    .toList();
            for (Note note : notes) {
                note.getTags().remove(tag);
                note.getTags().add(reassignTag);
                noteRepository.save(note);
            }
        } else {
            var notes = noteRepository.findAll().stream()
                    .filter(note -> note.getTags().contains(tag))
                    .toList();
            for (Note note : notes) {
                note.getTags().remove(tag);
                noteRepository.save(note);
            }
        }
        tagRepository.delete(tag);
    }
}

