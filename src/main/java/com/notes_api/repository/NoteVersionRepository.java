package com.notes_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes_api.model.NoteVersion;
import java.util.List;

@Repository
public interface NoteVersionRepository extends JpaRepository<NoteVersion, Long> {
    List<NoteVersion> findByNoteIdAndDeletedFalseOrderByVersionDateDesc(Long noteId);
}

