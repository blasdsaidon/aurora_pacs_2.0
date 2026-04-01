package com.aurora.aurora_pacs.service;

import com.aurora.aurora_pacs.dto.StudySummaryDto;
import com.aurora.aurora_pacs.integration.OrthancService;
import com.aurora.aurora_pacs.model.Study;
import com.aurora.aurora_pacs.model.StudyAccess;
import com.aurora.aurora_pacs.model.UserAccount;
import com.aurora.aurora_pacs.repo.StudyAccessRepository;
import com.aurora.aurora_pacs.repo.StudyRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyAccessRepository studyAccessRepository;
    private final OrthancService orthancService;

    public StudyService(StudyRepository studyRepository,
                        StudyAccessRepository studyAccessRepository,
                        OrthancService orthancService) {
        this.studyRepository = studyRepository;
        this.studyAccessRepository = studyAccessRepository;
        this.orthancService = orthancService;
    }

    public List<StudySummaryDto> listAll() {
        return studyRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public StudySummaryDto findByStudyInstanceUid(String studyInstanceUid) {
        return studyRepository.findByStudyInstanceUid(studyInstanceUid)
                .map(this::toDto)
                .orElse(null);
    }

    public List<?> listForPatient(UserAccount user) {
        Map<String, Object> unique = new LinkedHashMap<>();

        if (user.getPatientId() != null && !user.getPatientId().isBlank()) {
            studyRepository.findByPatientId(user.getPatientId()).stream()
                    .map(this::toDto)
                    .forEach(dto -> unique.put(dto.getStudyInstanceUid(), dto));
        }

        List<StudyAccess> manualAccesses = studyAccessRepository.findByUserId(user.getId());
        for (StudyAccess access : manualAccesses) {
            String uid = access.getStudyInstanceUid();
            StudySummaryDto dto = findByStudyInstanceUid(uid);

            if (dto != null) {
                unique.put(uid, dto);
            } else {
                unique.put(uid, Map.of(
                        "studyInstanceUid", uid,
                        "viewerUrl", orthancService.buildStoneViewerUrl(uid),
                        "availableInCatalog", false
                ));
            }
        }

        return unique.values().stream().toList();
    }

    public boolean userCanAccess(UserAccount user, String studyInstanceUid) {
        if (user.getPatientId() != null && !user.getPatientId().isBlank()) {
            Study study = studyRepository.findByStudyInstanceUid(studyInstanceUid).orElse(null);
            if (study != null && user.getPatientId().equals(study.getPatientId())) {
                return true;
            }
        }

        return studyAccessRepository.existsByUserIdAndStudyInstanceUid(user.getId(), studyInstanceUid);
    }

    public Study upsert(UpsertStudyCommand command) {
        Study study = studyRepository.findByStudyInstanceUid(command.studyInstanceUid())
                .orElseGet(Study::new);

        study.setStudyInstanceUid(command.studyInstanceUid().trim());
        study.setPatientId(blankToNull(command.patientId()));
        study.setPatientName(blankToNull(command.patientName()));
        study.setStudyDate(blankToNull(command.studyDate()));
        study.setModality(blankToNull(command.modality()));
        study.setStudyDescription(blankToNull(command.studyDescription()));
        study.setClinicCode(blankToNull(command.clinicCode()));
        study.setSourceType(blankToNull(command.sourceType()));
        study.setStorageType(blankToNull(command.storageType()));
        study.setStorageKey(blankToNull(command.storageKey()));
        study.setViewerReady(command.viewerReady());
        study.setSyncStatus(blankToNull(command.syncStatus()));

        return studyRepository.save(study);
    }

    public String buildViewerUrl(String studyInstanceUid) {
        return orthancService.buildStoneViewerUrl(studyInstanceUid);
    }

   private StudySummaryDto toDto(Study study) {
    return new StudySummaryDto(
            study.getStudyInstanceUid(),
            study.getPatientName(),
            study.getPatientId(),
            study.getStudyDate(),
            study.getModality(),
            study.getStudyDescription(),
            study.isViewerReady()
    );
}

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public record UpsertStudyCommand(
            String studyInstanceUid,
            String patientId,
            String patientName,
            String studyDate,
            String modality,
            String studyDescription,
            String clinicCode,
            String sourceType,
            String storageType,
            String storageKey,
            boolean viewerReady,
            String syncStatus
    ) {}
}