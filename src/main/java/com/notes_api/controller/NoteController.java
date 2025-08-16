package com.notes_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.notes_api.dto.request.NoteRequest;
import com.notes_api.dto.response.NoteResponse;
import com.notes_api.dto.response.TagResponse;
import com.notes_api.model.Note;
import com.notes_api.service.NoteService;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest note) {
        Note created = noteService.createNote(
        		note.userId,
        		note.title,
        		note.content,
        		note.tags
        		);
        
        List<TagResponse> tagDTOs = created.getTags()
                .stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        
        NoteResponse response = new NoteResponse(
        		created.getId(),
        		created.getUser() != null ? created.getUser().getId() : 0,
        		created.getTitle(),
        		created.getContent(),
        		tagDTOs,
        		created.getUpdatedAt(),
        		created.getArchived()
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteResponse>> getNotesByUser(@PathVariable Long userId) {
    	List<Note> notes = noteService.getNotesByUser(userId);
    	List<NoteResponse> response = new LinkedList<NoteResponse>();
    	
    	for (Note note : notes) {
    		List<TagResponse> tagDTOs = note.getTags()
	                .stream()
	                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
	                .collect(Collectors.toList());
    		
			NoteResponse newNote = new NoteResponse(
					note.getId(),
					note.getUser() != null ? note.getUser().getId() : 0,
					note.getTitle(),
	        		note.getContent(),
	        		tagDTOs,
	        		note.getUpdatedAt(),
	        		note.getArchived()
			);
			
			response.add(newNote);
		}
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable Long id) {
        Optional<Note> noteOpt = noteService.getNoteDetails(id);
        if(!noteOpt.isPresent()) {
        	 return ResponseEntity.notFound().build();
        }
        
        List<TagResponse> tagDTOs = noteOpt.get().getTags()
                .stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        
        return noteOpt
                .map(note -> new NoteResponse(
                	note.getId(),
            		note.getUser() != null ? note.getUser().getId() : 0,
					note.getTitle(),
	        		note.getContent(),
	        		tagDTOs,
	        		note.getUpdatedAt(),
	        		note.getArchived()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping()
    public ResponseEntity<NoteResponse> updateNote(@RequestBody NoteRequest note) {
        Note updated = noteService.updateNote(note.userId, note.id, note.title, note.content, note.tags);
        List<TagResponse> tagDTOs = updated.getTags()
                .stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        
        NoteResponse response = new NoteResponse(
        		updated.getId(),
        		updated.getUser() != null ? updated.getUser().getId() : 0,
				updated.getTitle(),
				updated.getContent(),
        		tagDTOs,
        		updated.getUpdatedAt(),
        		updated.getArchived()
        );
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/archive/{userId}/{id}")
    public ResponseEntity<NoteResponse> archiveNote(@PathVariable Long userId, @PathVariable Long id) {
        Note updated = noteService.archiveNote(userId, id);
        List<TagResponse> tagDTOs = updated.getTags()
                .stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        
        NoteResponse response = new NoteResponse(
        		updated.getId(),
        		updated.getUser() != null ? updated.getUser().getId() : 0,
				updated.getTitle(),
				updated.getContent(),
        		tagDTOs,
        		updated.getUpdatedAt(),
        		updated.getArchived()
        );
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/unarchive/{userId}/{id}")
    public ResponseEntity<NoteResponse> unarchiveNote(@PathVariable Long userId, @PathVariable Long id) {
        Note updated = noteService.unarchiveNote(userId, id);
        List<TagResponse> tagDTOs = updated.getTags()
                .stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
        
        NoteResponse response = new NoteResponse(
        		updated.getId(),
        		updated.getUser() != null ? updated.getUser().getId() : 0,
				updated.getTitle(),
				updated.getContent(),
        		tagDTOs,
        		updated.getUpdatedAt(),
        		updated.getArchived()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long userId, @PathVariable Long id) {
        noteService.deleteNote(userId, id);
        return ResponseEntity.noContent().build();
    }
}

