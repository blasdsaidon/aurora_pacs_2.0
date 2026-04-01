package com.aurora.aurora_pacs.repo;

import com.aurora.aurora_pacs.model.StudyAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyAccessRepository extends JpaRepository<StudyAccess, Long> {
    List<StudyAccess> findByUserId(Long userId);
    boolean existsByUserIdAndStudyInstanceUid(Long userId, String studyInstanceUid);
}
