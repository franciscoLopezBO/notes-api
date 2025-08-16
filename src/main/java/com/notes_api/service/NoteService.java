package com.notes_api.service;

import org.springframework.stereotype.Service;

import com.notes_api.model.Note;
import com.notes_api.model.NoteVersion;
import com.notes_api.model.Tag;
import com.notes_api.model.UserAccount;
import com.notes_api.repository.NoteRepository;
import com.notes_api.repository.NoteVersionRepository;
import com.notes_api.repository.TagRepository;
import com.notes_api.repository.UserAccountRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final NoteVersionRepository noteVersionRepository;
    private final UserAccountRepository userRepository;
    
    public NoteService(NoteRepository noteRepository, TagRepository tagRepository, NoteVersionRepository noteVersionRepository, UserAccountRepository userRepository) {
        this.noteRepository = noteRepository;
        this.tagRepository = tagRepository;
        this.noteVersionRepository = noteVersionRepository;
        this.userRepository = userRepository;
    }

    public Note createNote(Long userId, String title, String content, List<String> tags) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Note note = new Note();
        note.setUser(user);
        note.setTitle(title);
        note.setContent(content);
        note.setArchived(false);
        note.setDeleted(false);
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        
        List<Tag> tagsEntity = new LinkedList<Tag>(); 
        // Check if the tags have already registered
        for (String tag : tags) {
        	if(!tagRepository.findByUserIdAndName(userId, tag).isPresent()) {
        		// Register new tag
        		Tag newTag = new Tag(user, tag);
        		newTag = tagRepository.save(newTag);
        		tagsEntity.add(newTag);
        	} else {
        		Tag relatedTag = tagRepository.findByUserIdAndName(userId, tag).get();
        		tagsEntity.add(relatedTag);
        	}        	
		}
        
        note.setTags(Set.copyOf(tagsEntity));

        note = noteRepository.save(note);
        
        saveNoteVersion(note, user);

        return note;
    }

    public List<Note> getNotesByUser(Long userId) {
        return noteRepository.findByUserIdAndDeletedFalse(userId);
    }

    public Optional<Note> getNoteDetails(Long noteId) {
        return noteRepository.findByIdAndDeletedFalse(noteId);
    }

    public Note updateNote(Long userId, Long noteId, String title, String content, List<String> tags) {
        Note note = noteRepository.findByIdAndUserIdAndDeletedFalse(noteId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        note.setTitle(title);
        note.setContent(content);
        note.setUpdatedAt(LocalDateTime.now());
        
        List<Tag> tagsEntity = new LinkedList<Tag>(); 
        // Check if the tags have already registered
        for (String tag : tags) {
        	if(!tagRepository.findByUserIdAndName(userId, tag).isPresent()) {
        		// Register new tag
        		UserAccount user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
        		Tag newTag = new Tag(user, tag);
        		newTag = tagRepository.save(newTag);
        		tagsEntity.add(newTag);
        	} else {
        		Tag relatedTag = tagRepository.findByUserIdAndName(userId, tag).get();
        		tagsEntity.add(relatedTag);
        	}        	
		}
        note.setTags(new HashSet<>(tagsEntity));
        Note updatedNote = noteRepository.save(note);
        saveNoteVersion(updatedNote, note.getUser());

        return updatedNote;
    }

    public void deleteNote(Long userId, Long noteId) {
        Note note = noteRepository.findByIdAndUserIdAndDeletedFalse(noteId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));

        note.setDeleted(true);
        noteRepository.save(note);
    }

    public Note archiveNote(Long userId, Long noteId) {
        Note note = noteRepository.findByIdAndUserIdAndDeletedFalse(noteId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));
        note.setArchived(true);
        return noteRepository.save(note);
    }

    public Note unarchiveNote(Long userId, Long noteId) {
        Note note = noteRepository.findByIdAndUserIdAndDeletedFalse(noteId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found"));
        note.setArchived(false);
        return noteRepository.save(note);
    }

    private void saveNoteVersion(Note note, UserAccount user) {
    	NoteVersion version = new NoteVersion();
    	version.setNote(note);
    	version.setTitle(note.getTitle());
    	version.setContent(note.getContent());
    	version.setUser(user);
    	version.setVersionDate(LocalDateTime.now());
    	version.setDeleted(false);
        noteVersionRepository.save(version);
    }
}

