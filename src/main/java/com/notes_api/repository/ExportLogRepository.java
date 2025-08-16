package com.notes_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notes_api.model.ExportLog;
import java.util.List;

@Repository
public interface ExportLogRepository extends JpaRepository<ExportLog, Long> {
    List<ExportLog> findByUserIdAndDeletedFalse(Long userId);
}

