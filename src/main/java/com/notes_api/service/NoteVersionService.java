package com.notes_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.notes_api.model.NoteVersion;
import com.notes_api.repository.NoteVersionRepository;

@Service
public class NoteVersionService {

    private final NoteVersionRepository noteVersionRepository;

    public NoteVersionService(NoteVersionRepository noteVersionRepository) {
        this.noteVersionRepository = noteVersionRepository;
    }

    public List<NoteVersion> getVersionsByNoteId(Long noteId) {
        return noteVersionRepository.findByNoteIdAndDeletedFalseOrderByVersionDateDesc(noteId);
    }

    public Optional<NoteVersion> getVersionById(Long versionId) {
        return noteVersionRepository.findById(versionId);
    }
}

