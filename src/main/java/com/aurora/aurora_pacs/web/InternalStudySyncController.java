package com.aurora.aurora_pacs.web;

import com.aurora.aurora_pacs.model.Study;
import com.aurora.aurora_pacs.service.StudyService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/internal/studies")
public class InternalStudySyncController {

    private final StudyService studyService;

    @Value("${internal.agent.api-key}")
    private String internalAgentApiKey;

    public InternalStudySyncController(StudyService studyService) {
        this.studyService = studyService;
    }

    @PostMapping("/upsert")
    public StudySyncResponse upsert(@RequestHeader("X-Agent-Key") String agentKey,
                                    @RequestBody UpsertStudyRequest request) {
        checkAgentKey(agentKey);

        Study saved = studyService.upsert(new StudyService.UpsertStudyCommand(
                request.studyInstanceUid(),
                request.patientId(),
                request.patientName(),
                request.studyDate(),
                request.modality(),
                request.studyDescription(),
                request.clinicCode(),
                request.sourceType(),
                request.storageType(),
                request.storageKey(),
                request.viewerReady(),
                request.syncStatus()
        ));

        return toResponse(saved);
    }

    @PostMapping("/batch-upsert")
    public List<StudySyncResponse> batchUpsert(@RequestHeader("X-Agent-Key") String agentKey,
                                               @RequestBody List<UpsertStudyRequest> requests) {
        checkAgentKey(agentKey);

        return requests.stream()
                .map(request -> studyService.upsert(new StudyService.UpsertStudyCommand(
                        request.studyInstanceUid(),
                        request.patientId(),
                        request.patientName(),
                        request.studyDate(),
                        request.modality(),
                        request.studyDescription(),
                        request.clinicCode(),
                        request.sourceType(),
                        request.storageType(),
                        request.storageKey(),
                        request.viewerReady(),
                        request.syncStatus()
                )))
                .map(this::toResponse)
                .toList();
    }

    private void checkAgentKey(String agentKey) {
        if (internalAgentApiKey == null || internalAgentApiKey.isBlank() || !internalAgentApiKey.equals(agentKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid agent key");
        }
    }

    private StudySyncResponse toResponse(Study study) {
        return new StudySyncResponse(
                study.getId(),
                study.getStudyInstanceUid(),
                study.getSyncStatus(),
                study.isViewerReady(),
                study.getUpdatedAt()
        );
    }

    public record UpsertStudyRequest(
            @NotBlank String studyInstanceUid,
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

    public record StudySyncResponse(
            Long id,
            String studyInstanceUid,
            String syncStatus,
            boolean viewerReady,
            OffsetDateTime updatedAt
    ) {}
}
