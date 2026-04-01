package com.aurora.aurora_pacs.repo;

import com.aurora.aurora_pacs.model.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {

    Optional<Study> findByStudyInstanceUid(String studyInstanceUid);

    List<Study> findByPatientId(String patientId);

    boolean existsByStudyInstanceUid(String studyInstanceUid);
}