package com.notes_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes_api.model.Note;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserIdAndDeletedFalse(Long userId);
    Optional<Note> findByIdAndUserIdAndDeletedFalse(Long id, Long userId);
    Optional<Note> findByIdAndDeletedFalse(Long id);
}

